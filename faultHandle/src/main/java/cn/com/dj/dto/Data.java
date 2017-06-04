package cn.com.dj.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

	/*{
    "_id" : ObjectId("54d04f262cdccf6e2be69cd0"),
    "deviceId" : ObjectId("54bdc0a32cdc43b7d33f72a0"),
    "sensorId" : 0,
    "id" : "300009",
    "timestamp" : NumberLong(1394674376),
    "value" : "44491708",
    "createTime" : NumberLong(1422937894),
    "endTime" : NumberLong(1422937841),
    "timestampUs" : NumberLong(1394674376000000)
}*/
	@Id
    @JsonProperty("_id")
    private ObjectId _id;
	private ObjectId deviceId;
	private Integer sensorId;
	private String id;
	private Long timestamp;
	private String value;
	private Long createTime;
	private Long endTime;
	private Long timestampUs;
	public ObjectId get_id() {
		return _id;
	}
	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	public ObjectId getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(ObjectId deviceId) {
		this.deviceId = deviceId;
	}
	public Integer getSensorId() {
		return sensorId;
	}
	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Long getTimestampUs() {
		return timestampUs;
	}
	public void setTimestampUs(Long timestampUs) {
		this.timestampUs = timestampUs;
	}
	
}
