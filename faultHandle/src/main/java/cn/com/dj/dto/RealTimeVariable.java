package cn.com.dj.dto;


import java.util.Date;
import java.util.Map;

public class RealTimeVariable {
    /**
     * 变量名
     */
    private String id;
    private Float value;
    private Date timestamp;
    private Date endTime;
    private Long timestampUs;
    
    public RealTimeVariable(){}
    
    public RealTimeVariable(String id,float value){
    	this.id=id;
    	this.value=value;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    

	public Long getTimestampUs() {
		return timestampUs;
	}

	public void setTimestampUs(Long timestampUs) {
		this.timestampUs = timestampUs;
	}
	
	public static RealTimeVariable getInstance(Map<String, String> map){
		String val=map.get("value");
		String idString=map.get("id");
		if(val!=null)
			return new RealTimeVariable(idString, Float.parseFloat(val));
		return null;
	}
	
}
