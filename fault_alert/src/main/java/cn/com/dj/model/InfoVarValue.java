package cn.com.dj.model;

/**
 * This class is used for noting the variables' value when the fault is found
 * @author Jiang Du
 *id: the sign
 *name:is the variable name
 *value :the value of the var when the fault happened
 */
public class InfoVarValue {

	private int id;
	private String name;
	private double ratevalue;
	private double realvalue;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getRatevalue() {
		return ratevalue;
	}
	public void setRatevalue(double ratevalue) {
		this.ratevalue = ratevalue;
	}
	public double getRealvalue() {
		return realvalue;
	}
	public void setRealvalue(double realvalue) {
		this.realvalue = realvalue;
	}
	

}
