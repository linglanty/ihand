package cn.com.dj.dto;

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
public class FaultDump {

	@Id
    @JsonProperty("_id")
    private ObjectId id;
	private ObjectId ruleId;
	private Long createTime;
	private Integer reasonId;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public ObjectId getRuleId() {
		return ruleId;
	}
	public void setRuleId(ObjectId ruleId) {
		this.ruleId = ruleId;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	
	



}
