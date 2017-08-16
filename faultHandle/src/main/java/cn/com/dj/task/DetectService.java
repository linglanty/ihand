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
import org.apache.commons.collections.MapUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
        //无在线网关的时候退出
		if(onlineDevice == null) {
            return;
        }

		try {
            ObjectId oid=new ObjectId(oId);

            //获取在线设备 并设置上面的Map以加速查询
            List<Machine> onlineMachines = getOnlineMachines(onlineDevice);

            if (CollectionUtils.isEmpty(onlineMachines)) {
                //没有在线设备时, 直接返回
                return;
            }
            Map<ObjectId, ObjectId> machineId2ModelId = Maps.newHashMap();
            Map<ObjectId, Machine> machineId2Machine = Maps.newHashMap();
            for(Machine machine : onlineMachines) {
                machineId2ModelId.put(machine.getId(), machine.getModelId());
                machineId2Machine.put(machine.getId(), machine);
            }

            //如果没有在线的设备返回
            if (MapUtils.isEmpty(machineId2Machine)) {
                return;
            }

            //用于通过 modelId 查询 rule 并获得由 machineID 到 device 的映射
            Set<ObjectId> modelIds = Sets.newHashSet();
            Map<ObjectId, Device> machineId2Device= Maps.newHashMap();
            for(Machine machine : onlineMachines) {
                modelIds.add(machine.getModelId());
                machineId2Device.put(machine.getId(), onlineDevice);
            }

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

            // 由ModelId来获取rule列表
            Map<ObjectId,List<Rule>> modelId2Rule = queryRules(modelIds);

            //获取实时数据
            Map<ObjectId, Map<String, RealTimeVariable>> machineId2realTimeDataMap = realTimeDataService.getAllRealTimeMap(oid, Lists.newArrayList(machineId2Machine.keySet()));

            //规则匹配  对匹配成功的故障进行插入数据库
            for(Map.Entry<ObjectId, Map<String, RealTimeVariable>> machineId2realTimeData : machineId2realTimeDataMap.entrySet()) {
                //对于每个设备的数据
                Map<String,RealTimeVariable> realTimeVariables = machineId2realTimeData.getValue();
                //对于每个设备的Rule
                List<Rule> rules = modelId2Rule.get(machineId2ModelId.get(machineId2realTimeData.getKey()));
                if(CollectionUtils.isEmpty(rules)) {
                    continue;
                }
                //对于同种设备的每个Rule
                for(Rule eachRule : rules) {
                    List<FaultParameter> faultPhenomenon = eachRule.getFaultPhenomenon();
                    //如果故障现象中没有参数就不进行扫描此故障
                    if(CollectionUtils.isEmpty(faultPhenomenon)) {
                        continue;
                    }
                    //当这个泵已经处在故障状态那就直接跳过
                    if(errorPumps.get(eachRule.getPumpId()) == LevelEnum.FAULT) {
                        continue;
                    }

                    //规则判断 是否出现了这种故障类型的故障。
                    if (LogicJudg.isFault(faultPhenomenon, realTimeVariables)) {

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
                        saveFaultInfo(onlineDevice, oid, machineId2Machine, machineId2realTimeData, realTimeVariables, eachRule, faultReason);
                        errorPumps.put(eachRule.getPumpId(), LevelEnum.findLevel(eachRule.getLevel()));
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("故障检测失败" ,e);
        }
    }

    private void saveFaultInfo(Device onlineDevice, ObjectId oid, Map<ObjectId, Machine> machineId2Machine, Map.Entry<ObjectId, Map<String, RealTimeVariable>> machineId2realTimeData,
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
        Machine plc = machineId2Machine.get(machineId2realTimeData.getKey());
        fault.setMachineId(machineId2realTimeData.getKey());
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
        List<InfoVarValue> infovars = DetectService.returnRealRateValue(eachRule, pump, realTimeVariables);
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
	public static List<InfoVarValue> returnRealRateValue(Rule eachRule, Pump pump, Map<String,RealTimeVariable> realtimeVariables) {
		 
		 Set<String> allFaultParameterSet=new HashSet<String>();
		 List<FaultParameter> phenomenonFaultParameters=eachRule.getFaultPhenomenon();
		 for(FaultParameter parameter:phenomenonFaultParameters){
			 List<FaultVar>faultVars=parameter.getVars();
			 for(FaultVar var:faultVars){
				 allFaultParameterSet.add(var.get_id());
			 }
		 }
		 List<FaultReason> reasonsFaultParameters=eachRule.getFaultReason();
		 for(FaultReason reason:reasonsFaultParameters){
			 List<FaultParameter> reasonParameters=reason.getVars();
			 for(FaultParameter parameter:reasonParameters){
				 List<FaultVar>faultVars=parameter.getVars();
				 for(FaultVar var:faultVars){
					 allFaultParameterSet.add(var.get_id());
				 }
			 }
		 }
		 List<InfoVarValue> vars=new ArrayList<InfoVarValue>();
		 for(String param:allFaultParameterSet){
			 InfoVarValue info=new InfoVarValue();
			 info.setId(param);
			 info.setRatevalue("0");
			 for(PumpVal pumpVal:pump.getVars()) {
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
	
	private Map<ObjectId,List<Rule>> queryRules(Set<ObjectId> modelIds){
        Map<ObjectId, List<Rule>> modelId2Rules = Maps.newHashMap();
		List<Rule> rules = ruleService.getRulesByRuleIds(Lists.newArrayList(modelIds),new ObjectId(oId));
		for(Rule rule : rules) {
			List<Rule> newRule=modelId2Rules.get(rule.getModelId());
			if(newRule==null){
				newRule = Lists.newArrayList();
				newRule.add(rule);
			}else {
				newRule.add(rule);
			}
            modelId2Rules.put(rule.getModelId(), newRule);
		}
		Iterator<ObjectId> iterator = modelId2Rules.keySet().iterator();
		while(iterator.hasNext()) {
			ObjectId id=iterator.next();
			if(modelId2Rules.get(id) == null)
				iterator.remove();
		}
        return modelId2Rules;
	}
	
	private List<Machine> getOnlineMachines(Device device)
	{
		// 通过device来筛选出在线machine
		List<Machine> machines = this.machineService.getOnlineMachines(new ObjectId(oId), Lists.newArrayList(device.getId()));
        return machines;
	}
}


