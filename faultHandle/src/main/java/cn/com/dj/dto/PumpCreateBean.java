package cn.com.dj.dto;

import java.util.List;

import org.bson.types.ObjectId;

public class PumpCreateBean {

	private ObjectId modelId;
	public ObjectId getModelId() {
		return modelId;
	}
	public void setModelId(ObjectId modelId) {
		this.modelId = modelId;
	}
	private String manufacturer;
	private String pumpType;
	private String pumpName;
	private List<PumpVal> vars;
	private List<PumpVal> otherVars;
	
	
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
	public void setOthervars(List<PumpVal> otherVars) {
		this.otherVars = otherVars;
	}
	
	
	
}
