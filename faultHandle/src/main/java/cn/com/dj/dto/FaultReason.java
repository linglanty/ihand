package cn.com.dj.dto;

import java.util.List;

public class FaultReason {

	private int id;

	private String name;

	// 判断是这种原因的参数
	private List<FaultParameter> vars;

	// 处理办法
	private String handleMethod;

	//维修人员应该剧本的技能
	private String repairmanMajor;

	//持续时间
	private Integer duration;

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

	public List<FaultParameter> getVars() {
		return vars;
	}

	public void setVars(List<FaultParameter> vars) {
		this.vars = vars;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
