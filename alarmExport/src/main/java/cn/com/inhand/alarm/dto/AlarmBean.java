package cn.com.inhand.alarm.dto;
public class AlarmBean {
	private String _id;
	private int timestamp;
	private String desc;
	private String sourceName;
	private int sourceType;
	private String sourceId;
	private int level;
	private String siteName;
	private String siteId;
	private int type;
	private int state;
	private int createTime;
	private int repeat;
	public String get_id() {
		return _id;
	}
	@Override
	public String toString() {
		return "AlarmBean [_id=" + _id + ", timestamp=" + timestamp + ", desc="
				+ desc + ", sourceName=" + sourceName + ", sourceType="
				+ sourceType + ", sourceId=" + sourceId + ", level=" + level
				+ ", siteName=" + siteName + ", siteId=" + siteId + ", type="
				+ type + ", state=" + state + ", createTime=" + createTime
				+ ", repeat=" + repeat + "]";
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
}
