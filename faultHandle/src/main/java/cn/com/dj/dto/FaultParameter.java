package cn.com.dj.dto;

import java.util.List;


public class FaultParameter {

	private int group;
	private List<FaultVar> vars;
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public List<FaultVar> getVars() {
		return vars;
	}
	public void setVars(List<FaultVar> vars) {
		this.vars = vars;
	}

}
