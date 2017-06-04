package cn.com.dj.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.inhand.common.model.Device;

public abstract interface DeviceDao
{

  public abstract List<Device> getOnlineDevices(ObjectId oId);
  
}

