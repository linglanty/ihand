package cn.com.dj.dto;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
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
	private Integer level;
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	private ObjectId machineId;
	private ObjectId ruleId;
	private ObjectId pumpId;
	private String pumpName;
	
	private String machineName;
	private String siteName;
	private String machineAddress;
	private String faultPhenomenon;
	private String faultReason;
	private String handleMethod;
	private String repairmanMajor;
	private Long createTime;
	private Location location;
	
	private int status;
	
	private List<InfoVarValue> vars;
	
	public List<InfoVarValue> getVars() {
		return vars;
	}

	public void setVars(List<InfoVarValue> vars) {
		this.vars = vars;
	}

	public String getPumpName() {
		return pumpName;
	}

	public void setPumpName(String pumpName) {
		this.pumpName = pumpName;
	}
	
	public ObjectId getPumpId() {
		return pumpId;
	}

	public void setPumpId(ObjectId pumpId) {
		this.pumpId = pumpId;
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
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
	// faultPhenomenon:the phenomenon of the fault
	public String getFaultPhenomenon() {
		return faultPhenomenon;
	}
	public void setFaultPhenomenon(String faultPhenomenon) {
		this.faultPhenomenon = faultPhenomenon;
	}
	//faultReason:the reason that load to the fault
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
	//repairmanMajor: the function of the repairman need to repair the fault
	public String getRepairmanMajor() {
		return repairmanMajor;
	}
	public void setRepairmanMajor(String repairmanMajor) {
		this.repairmanMajor = repairmanMajor;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	//status: 1:mean the fault haven't been handled 2:the fault have been handled
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	


}
