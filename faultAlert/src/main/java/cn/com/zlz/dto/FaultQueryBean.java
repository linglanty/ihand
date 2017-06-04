package cn.com.zlz.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is designed for fetching the fault info between the db and web
 * client
 * 
 * @author Jiang Du machineId : the machine that was detected the fault
 *         faultReason:the reason that load to the fault faultPhenomenon:the
 *         phenomenon of the fault repairmanMajor: the function of the repairman
 *         need to repair the fault status: 1:mean the fault haven't been
 *         handled 2:the fault have been handled vars: all of the variables'
 *         value about the fault
 */
public class FaultQueryBean {

	private long startTime;

	private long endTime;

	private String machinename;
	
	private String siteName;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	private List<Integer> levels;

	private List<Integer> status;

	public FaultQueryBean() {
		this.status = new ArrayList<Integer>();
		this.levels=new ArrayList<Integer>();
		this.startTime = 0;
		this.endTime = 0;
		this.machinename = "";

	}

	public List<Integer> getLevels() {
		return levels;
	}

	public void setLevels(List<Integer> levels) {
		this.levels = levels;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<Integer> getStatus() {
		return status;
	}

	public void setStatus(List<Integer> status) {
		this.status = status;
	}

	public String getMachinename() {
		return machinename;
	}

	public void setMachinename(String machinename) {
		this.machinename = machinename;
	}

}
