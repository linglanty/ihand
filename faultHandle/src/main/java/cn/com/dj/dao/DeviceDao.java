package cn.com.dj.dao;

import cn.com.inhand.common.model.Device;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public interface DeviceDao
{
  List<ObjectId> getOnlineDevices(ObjectId oId);

  Map<ObjectId, Device> getDevicesByIds(ObjectId oId, List<ObjectId> objectIds);

  Device getDeviceById(ObjectId oId, ObjectId deviceId);
  
}

