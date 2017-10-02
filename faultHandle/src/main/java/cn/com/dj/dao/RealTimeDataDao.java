package cn.com.dj.dao;

import cn.com.dj.dto.RealTimeData;
import cn.com.dj.dto.RealTimeVariable;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;


public abstract interface RealTimeDataDao
{
	public abstract List<RealTimeData> getAllRealTimeData(ObjectId oId,List<ObjectId> machineId);
  
	public abstract Map<String, RealTimeVariable> getAllRealTimeMap(ObjectId oId, ObjectId machineId);
	
	public abstract void createRtData(ObjectId oId,RealTimeData rtData);
	
	public abstract RealTimeData getRtData(ObjectId oId,ObjectId rtId);
	
	public void modifyRt_Data(RealTimeData rtData,ObjectId oid);
}

