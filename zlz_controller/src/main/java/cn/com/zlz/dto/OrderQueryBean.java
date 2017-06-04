package cn.com.zlz.dto;

import java.util.List;
import org.bson.types.ObjectId;

public class OrderQueryBean {
	
	private String name;
	private String sn;
	private String certId;
	private Integer[] state;
	private Long startTime;
	private Long endTime;
	private List<ObjectId> resourceIds;
	private List<ObjectId> groupIds;
	private Integer roleType;

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public Integer getRoleType() {
		return this.roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}

	public List<ObjectId> getGroupIds() {
		return this.groupIds;
	}

	public void setGroupIds(List<ObjectId> groupIds) {
		this.groupIds = groupIds;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return this.sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer[] getState() {
		return this.state;
	}

	public void setState(Integer[] state) {
		this.state = state;
	}

	public List<ObjectId> getResourceIds() {
		return this.resourceIds;
	}

	public void setResourceIds(List<ObjectId> resourceIds) {
		this.resourceIds = resourceIds;
	}

	public Long getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

}