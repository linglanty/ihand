package cn.com.zlz.dao;

import cn.com.inhand.common.model.Machine;
import org.bson.types.ObjectId;

public abstract interface MachineDAO
{
  public abstract Machine getMachineById(ObjectId paramObjectId1, ObjectId paramObjectId2);

}
