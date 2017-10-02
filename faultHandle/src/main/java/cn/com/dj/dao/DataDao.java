package cn.com.dj.dao;

import org.bson.types.ObjectId;

import cn.com.dj.dto.Data;


public interface DataDao
{
	
	Data getlastestData(ObjectId deviceId, String varId, ObjectId oid);
	
}

