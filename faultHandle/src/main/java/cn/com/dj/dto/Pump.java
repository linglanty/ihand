package cn.com.dj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 *  水泵
 */
public class Pump {

	@Id
    @JsonProperty("_id")
    private ObjectId id;

	private ObjectId modelId;

	private String manufacturer;

	private String pumpType;

	private String pumpName;

	private List<PumpVal> vars;

	private List<PumpVal> otherVars;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getModelId() {
		return modelId;
	}

	public void setModelId(ObjectId modelId) {
		this.modelId = modelId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getPumpType() {
		return pumpType;
	}

	public void setPumpType(String pumpType) {
		this.pumpType = pumpType;
	}

	public String getPumpName() {
		return pumpName;
	}

	public void setPumpName(String pumpName) {
		this.pumpName = pumpName;
	}

	public List<PumpVal> getVars() {
		return vars;
	}

	public void setVars(List<PumpVal> vars) {
		this.vars = vars;
	}

	public List<PumpVal> getOtherVars() {
		return otherVars;
	}

	public void setOtherVars(List<PumpVal> otherVars) {
		this.otherVars = otherVars;
	}
}
