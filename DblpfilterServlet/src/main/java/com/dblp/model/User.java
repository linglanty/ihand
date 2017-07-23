package com.dblp.model;

public class User {

	private int id;
	
	private String name;
	
	private String pwd;
	
	private String email;
	
	private int isauto;
	
	private String areaother;
	
	private String resfield;

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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsauto() {
		return isauto;
	}

	public void setIsauto(int isauto) {
		this.isauto = isauto;
	}

	public String getResfield() {
		return resfield;
	}

	public void setResfield(String resfield) {
		this.resfield = resfield;
	}

	public String getAreaother() {
		return areaother;
	}

	public void setAreaother(String areaother) {
		this.areaother = areaother;
	}
}
