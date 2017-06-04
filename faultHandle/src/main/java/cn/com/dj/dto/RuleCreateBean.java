package cn.com.dj.dto;

import java.util.List;

import org.bson.types.ObjectId;
/**
 * 
 * @author Jiang Du 
 * id:the key
 * faultDescription:the fault Description
 * modelId:the id of model
 * modelName:the name of model from the Model DB
 * faultPhenomenon : the Parameter of the Fault by group the parameter 
 * in the same group means or,the different groups means and
 * faultReason:the reason that lead to the fault
 */
public class RuleCreateBean {

	private String faultDescription;
	private ObjectId modelId;
	private ObjectId pumpId;
	private String modelName;
	private Integer level;
	//private Integer duration;//持续时间
	private List<FaultParameter> faultPhenomenon;
	private List<FaultReason> faultReason;
	
	
	
	public String getFaultDescription() {
		return faultDescription;
	}
	public void setFaultDescription(String faultDescription) {
		this.faultDescription = faultDescription;
	}
	public ObjectId getModelId() {
		return modelId;
	}
	public void setModelId(ObjectId modelId) {
		this.modelId = modelId;
	}
	public ObjectId getPumpId() {
		return pumpId;
	}
	public void setPumpId(ObjectId pumpId) {
		this.pumpId = pumpId;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	/*public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}*/
	public List<FaultParameter> getFaultPhenomenon() {
		return faultPhenomenon;
	}
	public void setFaultPhenomenon(List<FaultParameter> faultPhenomenon) {
		this.faultPhenomenon = faultPhenomenon;
	}
	public List<FaultReason> getFaultReason() {
		return faultReason;
	}
	public void setFaultReason(List<FaultReason> faultReason) {
		this.faultReason = faultReason;
	}
	
	

	
}
