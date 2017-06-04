package cn.com.zlz.dto;

import java.util.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeCreateBean {
	
	private String userName;
	 private String certId;
	 private ObjectId oId;
	 private String name;
	 private ObjectId userId;
	 private String location;
	 private Set<String> majors;
	 
	 private float longitude;
	 private float latitude;
	 
	public String getCertId() {
		return certId;
	}
	public void setCertId(String certId) {
		this.certId = certId;
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
	
	public Set<String> getMajors() {
		return majors;
	}
	public void setMajors(Set<String> majors) {
		this.majors = majors;
	}
	public ObjectId getoId() {
		return oId;
	}
	public void setoId(ObjectId oId) {
		this.oId = oId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "EmployeeCreateBean [certId=" + certId + ", oId=" + oId
				+ ", name=" + name + ", userId=" + userId + ", userName="
				+ userName + ", location=" + location + ", majors=" + majors
				+ ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	
	 

}
