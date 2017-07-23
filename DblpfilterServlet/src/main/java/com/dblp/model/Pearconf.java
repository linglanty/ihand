package com.dblp.model;

public class Pearconf {
	
	private int pid;

	private String abbr;
	
	private String dblpabbr;
	
	private String fullname;
	
	private int area;
	
	private int type;
	
	private int grade;
	
	private String publisher;

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getDblpabbr() {
		return dblpabbr;
	}

	public void setDblpabbr(String dblpabbr) {
		this.dblpabbr = dblpabbr;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
}
