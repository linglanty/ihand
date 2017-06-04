package cn.com.dj.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SiteBean {

	@Id
	@JsonProperty("_id")
	private ObjectId id;
	private String name;
	private String address;
	private int businessState;
	private int online;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBusinessState() {
		return businessState;
	}

	public void setBusinessState(int businessState) {
		this.businessState = businessState;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

}
