package cn.com.dj.dto;

import java.util.List;

public class FaultReason {

	private int id;
	private String name;
	private String handleMethod;
	private String repairmanMajor;
	private Integer duration;//持续时间
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	private List<FaultParameter> vars;
	
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
	public List<FaultParameter> getVars() {
		return vars;
	}
	public void setVars(List<FaultParameter> vars) {
		this.vars = vars;
	}
	
	
}
