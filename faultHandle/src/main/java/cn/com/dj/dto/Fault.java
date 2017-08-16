package cn.com.dj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;
/**
 * This class is designed for fetching the fault info between the db and web client 
 * @author Jiang Du
 * machineId : the machine that was detected the fault
 * faultReason:the reason that load to the fault
 * faultPhenomenon:the phenomenon of the fault
 * repairmanMajor: the function of the repairman need to repair the fault
 * status: 1:mean the fault haven't been handled 2:the fault have been handled
 * vars: all of the variables' value about the fault
 */
public class Fault {

	@Id
    @JsonProperty("_id")
    private ObjectId id;

	//故障级别
	private Integer level;

	//
	private ObjectId machineId;

	//触发了哪个规则
	private ObjectId ruleId;

	//是哪个泵
	private ObjectId pumpId;

	//泵信息
	private String pumpName;

	//机器名称
	private String machineName;

	//网关名称
	private String siteName;

	//机器地址
	private String machineAddress;

	// 故障现象描述
	private String faultPhenomenon;

	//故障原因描述
	private String faultReason;

	//处理方法
	private String handleMethod;

	//修复故障应具备的技能
	private String repairmanMajor;

	//创建时间
	private Long createTime;

	//
	private Location location;

	//状态: 故障是否已经被处理, 1:未被处理,2:已处理
	private int status;

	//故障发生时记录的参数值,(名称、真实参数、规则参数)
	private List<InfoVarValue> vars;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public ObjectId getMachineId() {
		return machineId;
	}

	public void setMachineId(ObjectId machineId) {
		this.machineId = machineId;
	}

	public ObjectId getRuleId() {
		return ruleId;
	}

	public void setRuleId(ObjectId ruleId) {
		this.ruleId = ruleId;
	}

	public ObjectId getPumpId() {
		return pumpId;
	}

	public void setPumpId(ObjectId pumpId) {
		this.pumpId = pumpId;
	}

	public String getPumpName() {
		return pumpName;
	}

	public void setPumpName(String pumpName) {
		this.pumpName = pumpName;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getMachineAddress() {
		return machineAddress;
	}

	public void setMachineAddress(String machineAddress) {
		this.machineAddress = machineAddress;
	}

	public String getFaultPhenomenon() {
		return faultPhenomenon;
	}

	public void setFaultPhenomenon(String faultPhenomenon) {
		this.faultPhenomenon = faultPhenomenon;
	}

	public String getFaultReason() {
		return faultReason;
	}

	public void setFaultReason(String faultReason) {
		this.faultReason = faultReason;
	}

	public String getHandleMethod() {
		return handleMethod;
	}

	public void setHandleMethod(String handleMethod) {
		this.handleMethod = handleMethod;
	}

	public String getRepairmanMajor() {
		return repairmanMajor;
	}

	public void setRepairmanMajor(String repairmanMajor) {
		this.repairmanMajor = repairmanMajor;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<InfoVarValue> getVars() {
		return vars;
	}

	public void setVars(List<InfoVarValue> vars) {
		this.vars = vars;
	}
}
