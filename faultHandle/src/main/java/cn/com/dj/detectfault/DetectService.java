package cn.com.dj.detectfault;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.com.dj.dao.DataDao;
import cn.com.dj.dao.DeviceDao;
import cn.com.dj.dao.FaultDao;
import cn.com.dj.dao.FaultDumpDao;
import cn.com.dj.dao.MachineDao;
import cn.com.dj.dao.PumpDao;
import cn.com.dj.dao.RealTimeDataDao;
import cn.com.dj.dao.RuleDao;
import cn.com.dj.dao.SiteDao;
import cn.com.dj.dto.Data;
import cn.com.dj.dto.Fault;
import cn.com.dj.dto.FaultDump;
import cn.com.dj.dto.FaultParameter;
import cn.com.dj.dto.FaultReason;
import cn.com.dj.dto.FaultVar;
import cn.com.dj.dto.InfoVarValue;
import cn.com.dj.dto.Limit;
import cn.com.dj.dto.Location;
import cn.com.dj.dto.Pump;
import cn.com.dj.dto.PumpVal;
import cn.com.dj.dto.RealTimeVariable;
import cn.com.dj.dto.Rule;
import cn.com.dj.dto.SiteBean;
import cn.com.inhand.common.model.Device;
import cn.com.inhand.common.model.Machine;
import cn.com.inhand.dn4.utils.DateUtils;

@Service
public class DetectService {
	
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
	
	public void testDetecting() {
		System.out.println("Detecting can work!!!"+new Date().getTime());
	}
	
	private ObjectId deviceId;//设备ID
	
	private String variableId;//变量ID
	public ObjectId getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(ObjectId deviceId) {
		this.deviceId = deviceId;
	}

	public String getVariableId() {
		return variableId;
	}

	public void setVariableId(String variableId) {
		this.variableId = variableId;
	}

	
	
	public void detectFaults()
	{
		
		// 获取在线 device
		Map<ObjectId,Device> gatewayIds=findOnlineNetwork();
		if(gatewayIds.size()==0) return;//无在线网关的时候退出
		
		//*************初始化********** 机构ID 从配置文件里获取
		ObjectId oid=new ObjectId(oId);
		Map<ObjectId, ObjectId> machineId2ModelId=new HashMap<ObjectId, ObjectId>();//machineId to modelId
		Map<ObjectId, Machine> machineId2Machine=new HashMap<ObjectId, Machine>();//machineId to machine
		Map<ObjectId, Device> machineId2Device=new HashMap<ObjectId, Device>();//machineId to device
		Map<ObjectId,List<Rule>> modelId2Rule=new HashMap<ObjectId, List<Rule>>();//modelId to rule//通过modelId去找故障规则
		LogicJudg logicJudg=new LogicJudg();
		//*************初始化结束********** 
		//获取在线设备 并设置上面的Map以加速查询。
		getOnlineMachines(machineId2ModelId, machineId2Machine, gatewayIds,machineId2Device, modelId2Rule);
		if (machineId2Machine.size()==0) return;//如果没有在线的设备返回
		
		//找出所有出故障的pump并放在map中以便后面故障检测时剔除 （当是预警时，希望可以再进行故障检测；当是故障时不能在进行故障检测）
		Map<ObjectId,Boolean> errorPumpMap=new HashMap<ObjectId,Boolean>();
		List<Fault> faults=this.faultService.getAllunHandledFaultList(new ObjectId(oId));
		for(Fault fault:faults){
			if(!errorPumpMap.containsKey(fault.getPumpId())){
				errorPumpMap.put(fault.getPumpId(),fault.getLevel()==2);
			}else{
				if(errorPumpMap.get(fault.getPumpId())==false){
					errorPumpMap.put(fault.getPumpId(),true);
				}
			}
		}
		
		// 由ModelId来获取rule列表
		getRuleMap(modelId2Rule);
		
		//获取实时数据   
		Map<ObjectId, Map<String, RealTimeVariable>> machineId2realTimeDataMap=realTimeDataService.getAllRealTimeMap(oid, new ArrayList<ObjectId>(machineId2Machine.keySet()));
		
		//规则匹配  对匹配成功的故障进行插入数据库
		for(Map.Entry<ObjectId, Map<String, RealTimeVariable>> machineId2realTimeDataEntry:machineId2realTimeDataMap.entrySet())//对于每个设备的数据
		{
			Map<String,RealTimeVariable> realtimeVariables=machineId2realTimeDataEntry.getValue();
			
			List<Rule> rulesByModelId=modelId2Rule.get(machineId2ModelId.get(machineId2realTimeDataEntry.getKey()));//对于每个设备的Rule
			
			//**********设置DeviceID;****************
			this.setDeviceId(machineId2realTimeDataEntry.getKey());
			
			if(rulesByModelId==null) continue;
			for(Rule eachRule:rulesByModelId){//对于同种设备的每个Rule
				
				if(eachRule.getFaultPhenomenon()==null||eachRule.getFaultPhenomenon().size()<1){
					continue;			//如果故障现象中没有参数就不进行扫描此故障。
				}
				if(errorPumpMap.containsKey(eachRule.getPumpId())&&errorPumpMap.get(eachRule.getPumpId())==true)
					continue;				//当这个泵已经处在故障状态那就直接跳过。
				
				 List<FaultParameter> faultPhenomenon=eachRule.getFaultPhenomenon();

				 if (logicJudg.isFault(faultPhenomenon, realtimeVariables)) {//规则判断  是否出现了这种故障类型的故障。
					 
					 
					 if(errorPumpMap.containsKey(eachRule.getPumpId())&&errorPumpMap.get(eachRule.getPumpId())==false&&eachRule.getLevel()<2)
						continue;				//当这个泵仍然在报警状态是则跳过
					 Fault fault=new Fault();
					 
					 FaultReason reasonType=logicJudg.findReason(eachRule.getFaultReason(), realtimeVariables);//寻找故障原因
					 
					//需要进行进行原因变量持续时间检测
					 if(reasonType!=null&&reasonType.getDuration()!=null&&reasonType.getDuration()!=0){
						 FaultDump dump=this.faultDumpService.getFaultDumpById(eachRule.getId(), reasonType.getId(), oid);
						 if(dump==null){//当之前没出现过的时候，存入数据表备份中
							 dump=new FaultDump();
							 dump.setRuleId(eachRule.getId());
							 dump.setReasonId(reasonType.getId());
							 this.faultDumpService.createFaultDump(dump, oid);
							 continue;
						 }
						 long duration_time=DateUtils.getUTC()-dump.getCreateTime();
						 if(duration_time<reasonType.getDuration()){  //当间隔小于这个设定的期限时，推迟判断
							 continue;
						 }else {
							 if(duration_time>Math.max(reasonType.getDuration()*5,300)){//过大时,更新数据库
								 this.faultDumpService.updateFaultDump(dump, oid);
								 continue;
							 }else {
								
							 }
						 }
					 }
					 
					 if(reasonType!=null){
						 fault.setFaultReason(reasonType.getName());
						 fault.setHandleMethod(reasonType.getHandleMethod());
						 fault.setRepairmanMajor(reasonType.getRepairmanMajor());
					 }
					 else {
						 fault.setFaultReason("不明原因");
						 fault.setHandleMethod("待寻找");
						 fault.setRepairmanMajor("电气,机械,自控");
					 }
					 
					 //设置ruleid plcid和plc名称
					 Machine curMachine=machineId2Machine.get(machineId2realTimeDataEntry.getKey());
					 Device curDevice=gatewayIds.get(curMachine.getGatewayId());
					 fault.setMachineId(machineId2realTimeDataEntry.getKey()); 
					 fault.setMachineName(curMachine.getName());
					 fault.setRuleId(eachRule.getId());
					 
					 /*将现场信息（包括位置）录入到故障信息中*/
					 fault.setSiteName(curDevice.getSiteName());//是哪个现场的泵出现了问题
					 if(curMachine.getSiteId()!=null){
						 SiteBean bean=siteService.getSiteById(curMachine.getSiteId(), oid);
						 if(bean!=null&&bean.getAddress()!=null)
							 fault.setMachineAddress(bean.getAddress());
					 }
					 
					 //将泵信息录入到故障信息中
					 fault.setPumpId(eachRule.getPumpId());
					 Pump pump=pumpService.getPumpById(eachRule.getPumpId(), oid);
					 
					// 获得 故障参数的  额定值 和 实际值
					 List<InfoVarValue> infovars=DetectService.returnRealRateValue(eachRule, pump, realtimeVariables);
					 fault.setVars(infovars);
					 
					 
					 fault.setPumpName(pump.getPumpName());//设置泵名
					 fault.setLevel(eachRule.getLevel());//设置级别
					 
					 errorPumpMap.put(pump.getId(),eachRule.getLevel()==2);//更新 已处于故障的Map
					 
					 fault.setFaultPhenomenon(eachRule.getFaultDescription());
					 
					 if(curDevice.getLocation()!=null){
						 Location location=new Location();
						 location.setLatitude(curDevice.getLocation().getLatitude());
						 location.setLongitude(curDevice.getLocation().getLongitude());
						 fault.setLocation(location);
					 }
					 this.faultService.createFault(fault, oid);

				 }
			}
		}
	}
	
	
	//获取所有实时值 包括现象参数和所有原因里的参数。
	public static List<InfoVarValue> returnRealRateValue(Rule eachRule,Pump pump,Map<String,RealTimeVariable> realtimeVariables) {
		 
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
			 for(PumpVal pumpVal:pump.getVars()){
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
	
	private void getRuleMap(Map<ObjectId,List<Rule>> modelId2Rule){
		List<Rule> ruleList=ruleService.getRuleListByRuleIds(new ArrayList<ObjectId>(modelId2Rule.keySet()),new ObjectId(oId));
		for(Rule rule:ruleList){
			List<Rule> newRule=modelId2Rule.get(rule.getModelId());
			if(newRule==null){
				newRule=new ArrayList<Rule>();
				newRule.add(rule);
			}else {
				newRule.add(rule);
			}
			modelId2Rule.put(rule.getModelId(), newRule);
		}
		Iterator<ObjectId> iterator=modelId2Rule.keySet().iterator();
		while(iterator.hasNext()){
			ObjectId id=iterator.next();
			if(modelId2Rule.get(id)==null)
				iterator.remove();
		}
	}
	
	private Map<ObjectId,Device> findOnlineNetwork()
	{
		// p162 获取在线 device
		List<Device> devices=deviceService.getOnlineDevices(new ObjectId(oId));
		Map<ObjectId,Device> gatewayIds=new HashMap<ObjectId,Device>();
		if (devices==null||devices.size()==0) return gatewayIds;//end when no machine is online 当无网关在线的时候结束
		for(Device device:devices){
			gatewayIds.put(device.getId(),device);
		}
		return gatewayIds;
	}
	
	private void getOnlineMachines(Map<ObjectId, ObjectId> machineIdMap,Map<ObjectId, Machine> machineDetailInfos,Map<ObjectId,Device> gatewayIds,Map<ObjectId,Device> machineId2Device,Map<ObjectId,List<Rule>> rules)
	{
		//通过device来筛选出在线machine
		List<Machine> machines=this.machineService.getOnlineMachines(new ObjectId(oId), new ArrayList<ObjectId>(gatewayIds.keySet()));
		if(machines==null||machines.size()==0) return;//当 没有在线machine的时候返回
		
		for(Machine machine:machines){
			machineIdMap.put(machine.getId(),machine.getModelId());
			machineDetailInfos.put(machine.getId(), machine);
		}
		
		//用于通过modelId 查询 rule  并获得由machineID到device的映射。
		for(Machine machine:machines){
			rules.put(machine.getModelId(), null);//
			machineId2Device.put(machine.getId(), gatewayIds.get(machine.getGatewayId()));	//machineId2Device
		}
	}
	
	class LogicJudg {
		
		//find the reason of the fault
		public FaultReason findReason(List<FaultReason> faultReasons,Map<String,RealTimeVariable> realtimeVariables)
		{
			for(FaultReason reason:faultReasons)
			{
				List<FaultParameter> vars=reason.getVars();
				if(vars==null||vars.size()<1) continue;
				if (isFault(vars, realtimeVariables)) {
					return reason;
				}
			}
			return null;
		}
		
		//and all the parameters satisfied
		public boolean isFault( List<FaultParameter> parameters,Map<String,RealTimeVariable> realtimeVariables)
		{
			for(FaultParameter parameter:parameters)
			{
				//对于每个参数都满足的时候可以确定是这种错误
				if (!hasProblemByGroup(parameter,realtimeVariables)) {
					return false;
				}
			}
			return true;
		}
		// or :one of the parameters satisfied
		private boolean hasProblemByGroup(FaultParameter parameter,Map<String,RealTimeVariable> realtimeVariables)
		{
			List<FaultVar> vars=parameter.getVars();
			for(FaultVar var:vars)
			{
				if (realtimeVariables.containsKey(var.get_id())) {
					RealTimeVariable realTimeVariable=realtimeVariables.get(var.get_id());
					if(var.getLimit().getFluctuation()!=null&&Float.compare(var.getLimit().getFluctuation(), 0)!=0){
						//获取上一时刻数据
						Data lastData=DetectService.this.dataService.getlastestData(deviceId, var.get_id(), new ObjectId(oId));
						
						if(lastData!=null){
							
							float cur_value=realTimeVariable.getValue();
							float last_value=Float.parseFloat(lastData.getValue());
							//当值变化没超过某个范围时：返回false;
							if(last_value!=0&&!(Float.compare((Math.abs(last_value-cur_value))/last_value, var.getLimit().getFluctuation())>0)){
								return false;
							}
						}
					}
					if(hasProblem(var.getLimit(), realTimeVariable)){
						return true;
					}
				}
			}
			return false;
		}
		/*
		 * 故障规则设置：
			1，故障规则可设置 上下界边界是否包含异常情况
			3, 当是一个固定值时，上下界相等时 如果相等则返回是，否则返回否
			2，当实时数据在故障设置的范围内是发出故障提醒
		 * */
		private boolean hasProblem(Limit limit,RealTimeVariable variable)
		{
			float value=(Float)variable.getValue();
			if(Float.compare(limit.getMinValue(), limit.getMaxValue())==0)//if the range is a value then we should compare the value with that value not the range
				return Float.compare(limit.getMinValue(), value)==0;
			if (limit.getMinAlert()) {//value<limit.getMinValue()
				if (limit.getMinEqual()==true&&Float.compare(value,limit.getMinValue())<0) {
					return false;
				}else if(limit.getMinEqual()==false&&Float.compare(value,limit.getMinValue())<=0){
					return false;
				}
				
			}
			if (limit.getMaxAlert()&&Float.compare(value,limit.getMaxValue())>0) {
				if (limit.getMaxEqual()==true&&Float.compare(value,limit.getMaxValue())>0) {
					return false;
				}else if(limit.getMaxEqual()==false&&Float.compare(value,limit.getMaxValue())>=0){
					return false;
				}
			}
			return true;
		}
	}
}


