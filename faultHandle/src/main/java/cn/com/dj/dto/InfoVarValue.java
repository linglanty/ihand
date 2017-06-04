package cn.com.dj.dto;

/**
 * This class is used for noting the variables' value when the fault is found
 * @author Jiang Du
 *id: the sign
 *name:is the variable name
 *value :the value of the var when the fault happened
 */
public class InfoVarValue {

	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String name;
	
	private String ratevalue;
	private String realvalue;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRatevalue() {
		return ratevalue;
	}
	public void setRatevalue(String ratevalue) {
		this.ratevalue = ratevalue;
	}
	public String getRealvalue() {
		return realvalue;
	}
	public void setRealvalue(String realvalue) {
		this.realvalue = realvalue;
	}

}
