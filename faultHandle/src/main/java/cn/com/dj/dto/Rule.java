package cn.com.dj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;
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
public class Rule {

	@Id
    @JsonProperty("_id")
    private ObjectId id;

	//级别 报警、故障
	private Integer level;

	// 故障描述 名称
	private String faultDescription;

	private ObjectId modelId;

	//泵ID
	private ObjectId pumpId;

	private String modelName;

	//故障现象参数 多组参数同时满足一定限制时, 说明是这种故障
	private List<FaultParameter> faultPhenomenon;

	// 导致这种故障的原因(可能有多个原因导致这种故障)
	private List<FaultReason> faultReason;

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
