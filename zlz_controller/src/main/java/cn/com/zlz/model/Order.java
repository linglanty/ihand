package cn.com.zlz.model;

import java.util.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
	
	@Id
	@JsonProperty("_id")
	private ObjectId id;
	private ObjectId oid; //派工单id
	private ObjectId pumpId;
	private String pumpName;
	private ObjectId machineId; //设备id
	private String machineName; //设备名
	private ObjectId siteId; //设备id
	private String siteName; //设备名
	private String employeeCertId; //员工id
	private String employeeName; //员工姓名
	private Long updateTime;
	private Long receiveTime;
	private Long refuseTime;
	private Long finishTime;
	private Long createTime; //派工单时间
	private ObjectId faultId;
	private Long faultTime;//故障时间
	private Set<String> faultType;//故障类型
	private Set<String> faultReason;//故障原因
	private String location; //派工地点
	private String reason; //部件损坏部位
	private String phenomenon;//现象
	private String remark;
	
	private boolean received; //是否接受
	private boolean finished; //是否完成
	private boolean refused;//是否拒绝
	private String refuseReason;
	private List<ObjectId> reportIds; //维修报告
	private String reportContent;
	private Long reportUpdateTime;
	
	
	public ObjectId getId() {
		return id;
	}
	public Order setId(ObjectId id) {
		this.id = id;
		return this;
	}
	public ObjectId getOid() {
		return oid;
	}
	public Order setOid(ObjectId oid) {
		this.oid = oid;
		return this;
	}
	public String getEmployeeCertId() {
		return employeeCertId;
	}
	public Order setEmployeeCertId(String employeeCertId) {
		this.employeeCertId = employeeCertId;
		return this;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public Order setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
		return this;
	}
	public Long getCreateTime() {
		return createTime;
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
	public Long getReceiveTime() {
		return receiveTime;
	}
	public Order setReceiveTime(Long receiveTime) {
		this.receiveTime = receiveTime;
		return this;
	}
	public Long getRefuseTime() {
		return refuseTime;
	}
	public Order setRefuseTime(Long refuseTime) {
		this.refuseTime = refuseTime;
		return this;
	}
	public Long getFaultTime() {
		return faultTime;
	}
	public Order setFaultTime(Long faultTime) {
		this.faultTime = faultTime;
		return this;
	}
	
	public ObjectId getFaultId() {
		return faultId;
	}
	public void setFaultId(ObjectId faultId) {
		this.faultId = faultId;
	}
	public Long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}
	public Order setCreateTime(Long createTime) {
		this.createTime = createTime;
		return this;
	}
	public String getLocation() {
		return location;
	}
	public Order setLocation(String location) {
		this.location = location;
		return this;
	}
	
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public String getPhenomenon() {
		return phenomenon;
	}
	public Order setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
		return this;
	}
	public Order setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
		return this;
	}

	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Order addReportId(ObjectId id) {
		if(this.reportIds == null)
			this.reportIds = new ArrayList<ObjectId>();
		this.reportIds.add(id);
		return this;
	}
	public List<ObjectId> getReportIds() {
		return reportIds;
	}
	public Order setReportIds(List<ObjectId> reportIds) {
		this.reportIds = reportIds;
		return this;
	}
	public ObjectId getMachineId() {
		return machineId;
	}
	public Order setMachineId(ObjectId machineId) {
		this.machineId = machineId;
		return this;
	}
	public String getMachineName() {
		return machineName;
	}
	public Order setMachineName(String machineName) {
		this.machineName = machineName;
		return this;
	}
	public ObjectId getSiteId() {
		return siteId;
	}
	public Order setSiteId(ObjectId siteId) {
		this.siteId = siteId;
		return this;
	}
	public String getSiteName() {
		return siteName;
	}
	public Order setSiteName(String siteName) {
		this.siteName = siteName;
		return this;
	}
	public String getReason() {
		return reason;
	}
	public Order setReason(String reason) {
		this.reason = reason;
		return this;
	}
	
	public boolean isReceived() {
		return received;
	}
	public Order setReceived(boolean received) {
		this.received = received;
		return this;
	}
	public boolean isFinished() {
		return finished;
	}
	public Order setFinished(boolean finished) {
		this.finished = finished;
		return this;
	}
	public boolean isRefused() {
		return refused;
	}
	public Order setRefused(boolean refused) {
		this.refused = refused;
		return this;
	}
	public String getReportContent() {
		return reportContent;
	}
	public Order setReportContent(String reportContent) {
		this.reportContent = reportContent;
		return this;
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
	public Long getReportUpdateTime() {
		return reportUpdateTime;
	}
	public Order setReportUpdateTime(Long reportUpdateTime) {
		this.reportUpdateTime = reportUpdateTime;
		return this;
	}
	public Order copy() {
		Order order = new Order();
		
		order.id = this.id;
		order.oid = this.oid; //派工单id
		order.machineId = this.machineId; //设备id
		order.machineName = this.machineName; //设备名
		order.employeeCertId = this.employeeCertId; //员工id
		order.employeeName = this.employeeName; //员工姓名
		order.siteId = this.siteId; //员工id
		order.siteName = this.siteName; //员工姓名
		order.createTime = this.createTime; //派工单时间
		order.location = this.location; //派工地点
		order.reason = this.reason; //部件损坏部位
		order.phenomenon = this.phenomenon;
		
		order.reportIds = new ArrayList<ObjectId>(this.reportIds); //维修报告
		
		return order;
	}
	

	
	@Override
	public String toString() {
		return "Order [id=" + id + ", oid=" + oid + ", pumpId=" + pumpId
				+ ", pumpName=" + pumpName + ", machineId=" + machineId
				+ ", machineName=" + machineName + ", siteId=" + siteId
				+ ", siteName=" + siteName + ", employeeCertId="
				+ employeeCertId + ", employeeName=" + employeeName
				+ ", updateTime=" + updateTime + ", receiveTime=" + receiveTime
				+ ", refuseTime=" + refuseTime + ", finishTime=" + finishTime
				+ ", createTime=" + createTime + ", faultId=" + faultId
				+ ", faultTime=" + faultTime + ", faultType=" + faultType
				+ ", faultReason=" + faultReason + ", location=" + location
				+ ", reason=" + reason + ", phenomenon=" + phenomenon
				+ ", remark=" + remark + ", received=" + received
				+ ", finished=" + finished + ", refused=" + refused
				+ ", refuseReason=" + refuseReason + ", reportIds=" + reportIds
				+ ", reportContent=" + reportContent + ", reportUpdateTime="
				+ reportUpdateTime + "]";
	}


}
