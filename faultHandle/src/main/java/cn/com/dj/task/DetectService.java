package cn.com.dj.task;

import cn.com.dj.dao.DataDao;
import cn.com.dj.dao.DeviceDao;
import cn.com.dj.dao.FaultDao;
import cn.com.dj.dao.FaultDumpDao;
import cn.com.dj.dao.MachineDao;
import cn.com.dj.dao.PumpDao;
import cn.com.dj.dao.RealTimeDataDao;
import cn.com.dj.dao.RuleDao;
import cn.com.dj.dao.SiteDao;
import cn.com.dj.dto.Fault;
import cn.com.dj.dto.FaultDump;
import cn.com.dj.dto.FaultParameter;
import cn.com.dj.dto.FaultReason;
import cn.com.dj.dto.FaultVar;
import cn.com.dj.dto.InfoVarValue;
import cn.com.dj.dto.LevelEnum;
import cn.com.dj.dto.Location;
import cn.com.dj.dto.Pump;
import cn.com.dj.dto.PumpVal;
import cn.com.dj.dto.RealTimeVariable;
import cn.com.dj.dto.Rule;
import cn.com.dj.dto.SiteBean;
import cn.com.dj.util.LogicJudg;
import cn.com.inhand.common.model.Device;
import cn.com.inhand.common.model.Machine;
import cn.com.inhand.dn4.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 执行定时任务
 */

/**
 * 解释 Device、Machine、Pump、Rule相互之间的关系
 *  Device 是 网关设备 网关设备用来发送数据到服务端(就像是手机的通讯芯片一样) ,Device可能会发送多个Machine(PLC)的数据到服务端 一般一个Device 对应一个 Machine
 *  Machine 是 PLC 设备 PLC设备用来实时获取泵的数据 一个PLC可以跟多个泵相连接。它返回的数据一般是(10002, 122), (10001,101)
 *  Model 是Machine的模型信息( 一一对应的关系 ), 它里面用来定义 Machine里的二进制 对应的是什么数据(描述) 保存到数据库中的数据就转换成了 <变量名,值>, 不再是plc里的原生数据
 *  Pump 泵 (温度、电压、电流、水压、发动机转速等等) 配置泵的信息时, 会从model中筛选出是其自己的参数
 *  Rule : 本来应该是 根据泵设定的参数设置 故障规则; 但是实际上是根据Model的参数来设置故障规则,这些参数和泵的参数一致。 Rule上有 modelId 和 ruleId
 *
 *  原则上,是应该有泵类型pumpType的,它定义一种类型的泵,然后每个Pump都带有泵类型信息。
 */
@Service
public class DetectService {

    private static Logger logger = LoggerFactory.getLogger(DetectService.class);

    @Autowired
	DeviceDao deviceService;
	
	@Autowired 
	MachineDao machineService;
	
	@Autowired 
	FaultDao faultService;
	
	@Autowired
	FaultDumpDao faultDumpService;
	
	@Autowired
	SiteDao siteService;
	
	@Autowired 	
	RuleDao ruleService;
	
	@Autowired
	PumpDao pumpService;
	
	@Autowired
	DataDao dataService;
	
	@Autowired
	RealTimeDataDao realTimeDataService;
	
	@Value("${config.detect.oid}")
	private String oId;

	private String variableId;//变量ID

	public void detectFaults(Device onlineDevice)
	{
        if(onlineDevice == null) {
            return;
        }
        ObjectId oid=new ObjectId(oId);
		try {
            //获取在线设备 并设置上面的Map以加速查询
            Machine onlineMachine = queryMachine(onlineDevice);
            if (onlineMachine == null) {
                //没有在线设备时, 直接返回
                return;
            }

            //目前已经故障的泵
            Map<ObjectId, LevelEnum> errorPumps = getHasFaultedPumps();
            //获取plc下的所有泵
            List<Pump> pumps = pumpService.getPumpListByModelId(onlineMachine.getModelId(), oid);

            //获取 plc上报的实时数据
            Map<String, RealTimeVariable> realTimeVariables = realTimeDataService.getAllRealTimeMap(oid, onlineMachine.getId());
            for (Pump pump : pumps) {
                //过滤出泵的参数数据
                Map<String, RealTimeVariable> pumpRealTimeVariables = filterPumpInfos(realTimeVariables, pump);
                if(errorPumps.get(pump.getId()) == LevelEnum.FAULT) {
                    continue;
                }
                List<Rule> rules = ruleService.getRulesByPumpId(pump.getId(), oid);
                for (Rule eachRule : rules) {
                    List<FaultParameter> faultPhenomenon = eachRule.getFaultPhenomenon();
                    //如果故障现象中没有参数就不进行扫描此故障
                    if(CollectionUtils.isEmpty(faultPhenomenon)) {
                        continue;
                    }
                    //规则判断 是否出现了这种故障类型的故障。
                    if (LogicJudg.isFault(faultPhenomenon, pumpRealTimeVariables)) {
                        //当这个泵仍然在报警状态是则跳过
                        if(errorPumps.get(eachRule.getPumpId()) == LevelEnum.WARN && LevelEnum.findLevel(eachRule.getLevel()) == LevelEnum.WARN) {
                            continue;
                        }
                        //寻找故障原因
                        FaultReason faultReason = LogicJudg.findReason(eachRule.getFaultReason(), realTimeVariables);
                        // 带有持续时间的规则
                        if(faultReason != null && faultReason.getDuration() != null && faultReason.getDuration() != 0) {
                            //查询上次出现的时间
                            FaultDump dump = this.faultDumpService.getFaultDumpById(eachRule.getId(), faultReason.getId(), oid);
                            if(dump == null) {
                                //当之前没出现过的时候，存入数据表备份中
                                dump=new FaultDump();
                                dump.setRuleId(eachRule.getId());
                                dump.setReasonId(faultReason.getId());
                                this.faultDumpService.createFaultDump(dump, oid);
                                continue;
                            }
                            //获取持续的时间
                            long duration_time = DateUtils.getUTC() - dump.getCreateTime();
                            //当间隔小于这个设定的期限时，推迟判断
                            if(duration_time < faultReason.getDuration()){
                                continue;
                            }else {
                                //过大时,更新数据库。很久之前记录的信息,需要清洗掉
                                if(duration_time > Math.max(faultReason.getDuration() * 5, 300)){
                                    this.faultDumpService.updateFaultDump(dump, oid);
                                    continue;
                                }
                            }
                        }
                        // 记录故障信息
                        saveFaultInfo(onlineDevice, oid, onlineMachine, realTimeVariables, eachRule, faultReason);
                        errorPumps.put(eachRule.getPumpId(), LevelEnum.findLevel(eachRule.getLevel()));
                    }

                }
            }
        } catch (Exception e) {
            logger.warn("故障检测失败" ,e);
        }
    }

    //获取已经是故障状态的泵
    private Map<ObjectId, LevelEnum> getHasFaultedPumps() {
        //找出所有出故障的 pump 并放在map中以便后面故障检测时剔除
        // （当是预警时，希望可以再进行故障检测；当是故障时不能在进行故障检测）
        List<Fault> faults = this.faultService.getAllunHandledFaultList(new ObjectId(oId));
        Map<ObjectId, LevelEnum> errorPumps = Maps.newHashMap();
        for(Fault fault : faults) {
            if(!errorPumps.containsKey(fault.getPumpId())) {
                //报警可以被故障覆盖
                errorPumps.put(fault.getPumpId(), LevelEnum.findLevel(fault.getLevel()));
            } else {
                if(errorPumps.get(fault.getPumpId()) == LevelEnum.WARN) {
                    errorPumps.put(fault.getPumpId(), LevelEnum.FAULT);
                }
            }
        }
        return errorPumps;
    }

    private Map<String, RealTimeVariable> filterPumpInfos(Map<String, RealTimeVariable> realTimeVariables, Pump pump) {
        Map<String, RealTimeVariable> result = Maps.newHashMapWithExpectedSize(realTimeVariables.size());
        List<PumpVal> vars = pump.getVars();
        List<PumpVal> copyVars = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(vars)) {
            copyVars.addAll(vars);
        }
        List<PumpVal> otherVars = pump.getOtherVars();
        if (CollectionUtils.isNotEmpty(otherVars)) {
            copyVars.addAll(otherVars);
        }
        for (PumpVal pumpVal : copyVars) {
            if (realTimeVariables.containsKey(pumpVal.get_id())) {
                result.put(pumpVal.get_id(), realTimeVariables.get(pumpVal.get_id()));
            }
        }
        return result;
    }



    private void saveFaultInfo(Device onlineDevice, ObjectId oid, Machine plc,
            Map<String, RealTimeVariable> realTimeVariables, Rule eachRule, FaultReason faultReason) {
        Fault fault=new Fault();
        if(faultReason != null) {
            fault.setFaultReason(faultReason.getName());
            fault.setHandleMethod(faultReason.getHandleMethod());
            fault.setRepairmanMajor(faultReason.getRepairmanMajor());
        } else {
            fault.setFaultReason("不明原因");
            fault.setHandleMethod("待寻找");
            fault.setRepairmanMajor("电气,机械,自控");
        }
        //设置ruleId plcId 和 plc名称
        fault.setMachineId(plc.getId());
        fault.setMachineName(plc.getName());
        fault.setRuleId(eachRule.getId());

        //将现场信息（包括位置）录入到故障信息中
        //作用:是哪个现场的泵出现了问题
        fault.setSiteName(onlineDevice.getSiteName());
        if(plc.getSiteId()!=null) {
            SiteBean bean = siteService.getSiteById(plc.getSiteId(), oid);
            if(bean != null && bean.getAddress()!=null)
                fault.setMachineAddress(bean.getAddress());
        }

        //将泵信息录入到故障信息中
        fault.setPumpId(eachRule.getPumpId());
        Pump pump = pumpService.getPumpById(eachRule.getPumpId(), oid);
        // 获得故障参数的额定值和实际值
        List<InfoVarValue> infovars = DetectService.generateRealRateValue(eachRule, pump, realTimeVariables);
        fault.setVars(infovars);
        //设置泵名
        fault.setPumpName(pump.getPumpName());
        //设置级别
        fault.setLevel(eachRule.getLevel());
        //更新 已处于故障的Map
        fault.setFaultPhenomenon(eachRule.getFaultDescription());
        if(onlineDevice.getLocation()!=null){
            Location location=new Location();
            location.setLatitude(onlineDevice.getLocation().getLatitude());
            location.setLongitude(onlineDevice.getLocation().getLongitude());
            fault.setLocation(location);
        }
        this.faultService.createFault(fault, oid);
    }


    //获取所有实时值 包括现象参数和所有原因里的参数。
	public static List<InfoVarValue> generateRealRateValue(Rule eachRule, Pump pump, Map<String,RealTimeVariable> realtimeVariables) {
		 Set<String> allFaultParameterSet = Sets.newHashSet();
		 List<FaultParameter> phenomenonFaultParameters = eachRule.getFaultPhenomenon();
		 for(FaultParameter parameter : phenomenonFaultParameters){
			 List<FaultVar>faultVars = parameter.getVars();
			 for(FaultVar var:faultVars){
				 allFaultParameterSet.add(var.get_id());
			 }
		 }
		 List<FaultReason> reasonsFaultParameters = eachRule.getFaultReason();
		 for(FaultReason reason : reasonsFaultParameters) {
			 List<FaultParameter> reasonParameters = reason.getVars();
			 for(FaultParameter parameter : reasonParameters) {
				 List<FaultVar>faultVars = parameter.getVars();
				 for(FaultVar var : faultVars) {
					 allFaultParameterSet.add(var.get_id());
				 }
			 }
		 }
		 List<InfoVarValue> vars = Lists.newArrayList();
		 for(String param : allFaultParameterSet){
			 InfoVarValue info = new InfoVarValue();
			 info.setId(param);
			 info.setRatevalue("0");
			 for(PumpVal pumpVal : pump.getVars()) {
				 if(pumpVal.get_id().equals(param)){
					 info.setRatevalue(pumpVal.getValue());
					 info.setName(pumpVal.getName());
				 }
			 }
			 if(realtimeVariables.get(param)!=null)
				 info.setRealvalue(realtimeVariables.get(param).getValue().toString());
			 else 
				 info.setRealvalue("值未找到");
			 vars.add(info);
		 }
		 
		 return vars;
	}

	private Machine queryMachine(Device device)
	{
		// 通过device来筛选出在线machine
		List<Machine> machines = this.machineService.getOnlineMachines(new ObjectId(oId), Lists.newArrayList(device.getId()));
        if(CollectionUtils.isEmpty(machines)) {
            return null;
        }
        return machines.get(0);
	}
}


