 package cn.com.zlz.dto;
 
import java.util.*;

import org.bson.types.ObjectId;
 
 public class OrderUpdateBean
 {
 
		private ObjectId id; //派工单id
		private ObjectId machineId; //设备id
		private String machineName; //设备名
		private ObjectId pumpId;
		private String pumpName;
		private ObjectId siteId; //设备id
		private String siteName; //设备名
		private String employeeCertId; //员工id
		private String employeeName; //员工姓名
		private Long updateTime;
		private Long createTime; //派工单时间
		private Long receiveTime;
		private Long refuseTime;
		private Long finishTime;
		private Long faultTime;//故障时间
		private String location; //派工地点
		private String reason; //部件损坏部位
		private String phenomenon;//现象
		
		private boolean received; //是否接受
		private boolean finished; //是否完成
		private boolean refused;
		private List<ObjectId> reportIds; //维修报告
		private String reportContent;
		private Long reportUpdateTime;
		
		private Set<String> faultType;
		private Set<String> faultReason;


		public ObjectId getId() {
			return id;
		}
		public void setId(ObjectId id) {
			this.id = id;
		}
		public ObjectId getMachineId() {
			return machineId;
		}
		public void setMachineId(ObjectId machineId) {
			this.machineId = machineId;
		}
		public String getMachineName() {
			return machineName;
		}
		public void setMachineName(String machineName) {
			this.machineName = machineName;
		}
		public ObjectId getSiteId() {
			return siteId;
		}
		public void setSiteId(ObjectId siteId) {
			this.siteId = siteId;
		}
		public String getSiteName() {
			return siteName;
		}
		public void setSiteName(String siteName) {
			this.siteName = siteName;
		}
		public String getEmployeeCertId() {
			return employeeCertId;
		}
		public void setEmployeeCertId(String employeeCertId) {
			this.employeeCertId = employeeCertId;
		}
		public String getEmployeeName() {
			return employeeName;
		}
		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}
		public Long getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Long updateTime) {
			this.updateTime = updateTime;
		}
		public Long getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Long createTime) {
			this.createTime = createTime;
		}
		public Long getFaultTime() {
			return faultTime;
		}
		public void setFaultTime(Long faultTime) {
			this.faultTime = faultTime;
		}
		
		public ObjectId getPumpId() {
			return pumpId;
		}
		public void setPumpId(ObjectId pumpId) {
			this.pumpId = pumpId;
		}
		public String getPumpName() {
			return pumpName;
		}
		public void setPumpName(String pumpName) {
			this.pumpName = pumpName;
		}
		public Long getFinishTime() {
			return finishTime;
		}
		public void setFinishTime(Long finishTime) {
			this.finishTime = finishTime;
		}
		public Long getReceiveTime() {
			return receiveTime;
		}
		public void setReceiveTime(Long receiveTime) {
			this.receiveTime = receiveTime;
		}
		public Long getRefuseTime() {
			return refuseTime;
		}
		public void setRefuseTime(Long refuseTime) {
			this.refuseTime = refuseTime;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getPhenomenon() {
			return phenomenon;
		}
		public void setPhenomenon(String phenomenon) {
			this.phenomenon = phenomenon;
		}
		
		public boolean isReceived() {
			return received;
		}
		public void setReceived(boolean received) {
			this.received = received;
		}
		public boolean isFinished() {
			return finished;
		}
		public void setFinished(boolean finished) {
			this.finished = finished;
		}
		public boolean isRefused() {
			return refused;
		}
		public void setRefused(boolean refused) {
			this.refused = refused;
		}
		public List<ObjectId> getReportIds() {
			return reportIds;
		}
		public void setReportIds(List<ObjectId> reportIds) {
			this.reportIds = reportIds;
		}
		public String getReportContent() {
			return reportContent;
		}
		public void setReportContent(String reportContent) {
			this.reportContent = reportContent;
		}
		public Long getReportUpdateTime() {
			return reportUpdateTime;
		}
		public void setReportUpdateTime(Long reportUpdateTime) {
			this.reportUpdateTime = reportUpdateTime;
		}
		
		public Set<String> getFaultType() {
			return faultType;
		}
		public void setFaultType(Set<String> faultType) {
			this.faultType = faultType;
		}
		public Set<String> getFaultReason() {
			return faultReason;
		}
		public void setFaultReason(Set<String> faultReason) {
			this.faultReason = faultReason;
		}
		@Override
		public String toString() {
			return "OrderUpdateBean [id=" + id + ", machineId=" + machineId
					+ ", machineName=" + machineName + ", pumpId=" + pumpId
					+ ", pumpName=" + pumpName + ", siteId=" + siteId
					+ ", siteName=" + siteName + ", employeeCertId="
					+ employeeCertId + ", employeeName=" + employeeName
					+ ", updateTime=" + updateTime + ", createTime="
					+ createTime + ", receiveTime=" + receiveTime
					+ ", refuseTime=" + refuseTime + ", finishTime="
					+ finishTime + ", faultTime=" + faultTime + ", location="
					+ location + ", reason=" + reason + ", phenomenon="
					+ phenomenon + ", received=" + received + ", finished="
					+ finished + ", refused=" + refused + ", reportIds="
					+ reportIds + ", reportContent=" + reportContent
					+ ", reportUpdateTime=" + reportUpdateTime + ", faultType="
					+ faultType + ", faultReason=" + faultReason + "]";
		}
		
		
		

		
 }
