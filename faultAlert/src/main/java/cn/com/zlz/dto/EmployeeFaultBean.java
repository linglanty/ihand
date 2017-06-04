package cn.com.zlz.dto;

import java.util.Set;

import org.bson.types.ObjectId;

import cn.com.zlz.model.Employee;

public class EmployeeFaultBean {
	
	private String _id;
	 private String id;
	 private String certId;
	 private ObjectId oId;
	 private String name;
	 private ObjectId userId;
	 private String userName;
	 private String location;
	 private String phone;
	 
	 private Set<String> majors;
	 
	 private float longitude;
	 private float latitude;
	 
	 private float distance;
	
	public EmployeeFaultBean(Employee employee) {
		id = employee.getId();
		certId = employee.getCertId();
		oId = employee.getoId();
		name = employee.getName();
		userId = employee.getUserId();
		userName = employee.getUserName();
		location = employee.getLocation();
		longitude = employee.getLongitude();
		latitude = employee.getLatitude();
		majors = employee.getMajors();
	}

	
	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public ObjectId getoId() {
		return oId;
	}

	public void setoId(ObjectId oId) {
		this.oId = oId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
	
	public Set<String> getMajors() {
		return majors;
	}

	public void setMajors(Set<String> majors) {
		this.majors = majors;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "EmployeeFaultBean [id=" + id + ", certId=" + certId + ", oId="
				+ oId + ", name=" + name + ", userId=" + userId + ", userName="
				+ userName + ", location=" + location + ", longitude="
				+ longitude + ", latitude=" + latitude + ", distance="
				+ distance + "]";
	}
	
	

}
