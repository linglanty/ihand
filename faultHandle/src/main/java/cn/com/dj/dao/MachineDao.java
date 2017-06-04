package cn.com.dj.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.inhand.common.model.Machine;

public abstract interface MachineDao
{
	public abstract List<Machine> getOnlineMachines(ObjectId oId,List<ObjectId> gatewayIds);
	
}

