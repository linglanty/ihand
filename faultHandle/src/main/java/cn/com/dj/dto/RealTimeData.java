package cn.com.dj.dto;

import java.util.List;

import org.bson.types.ObjectId;

public class RealTimeData {
    private ObjectId _id;
    private ObjectId deviceId;
    private Integer sensorId;
    private List<RealTimeVariable> vars;

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

    public List<RealTimeVariable> getVars() {
        return vars;
    }

    public void setVars(List<RealTimeVariable> vars) {
        this.vars = vars;
    }
	
	@Override
	public String toString(){
		StringBuilder builder=new StringBuilder();
		builder.append("[");
		 
        int iMax = vars.size() - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('{');
        for (int i = 0; ; i++) {
            b.append(vars.get(i).getId()+":"+(Float)vars.get(i).getValue());
            if (i == iMax)
                return b.append('}').toString();
            b.append(", ");
        }
		/*for(RealTimeVariable variable:vars)
		{
			builder.append(variable.)
		}*/
		
	}

}
