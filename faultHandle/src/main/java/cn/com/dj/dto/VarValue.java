package cn.com.dj.dto;

/**
 * This class is used for noting the variables' value when the fault is found
 * @author Jiang Du
 *id: the sign
 *name:is the variable name
 *value :the value of the var when the fault happened
 */
public class VarValue {

	
	private int id;
	private String name;
	private double value;
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
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

}
