package cn.com.dj.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.dj.dto.ModelQueryBean;
import cn.com.inhand.common.model.Model;

public abstract interface ModelDao
{
  public abstract Model getModelById(ObjectId paramObjectId1, ObjectId paramObjectId2);

  public abstract Model getModelById(ObjectId paramObjectId1, ObjectId paramObjectId2, int paramInt);

  public abstract Model getModel(ObjectId paramObjectId, String paramString, Object paramObject);

  public abstract void deleteModelById(ObjectId paramObjectId1, ObjectId paramObjectId2);

  public abstract void updateModel(Model paramModel);

  public abstract void updateModel(ObjectId paramObjectId, Model paramModel);

  public abstract void createModel(ObjectId paramObjectId, Model paramModel);

  public abstract List<Model> getModels(ObjectId paramObjectId, List<ObjectId> paramList);

  public abstract List<Model> getModels(ObjectId paramObjectId, ModelQueryBean paramModelQueryBean, int paramInt1, int paramInt2, int paramInt3);

  public abstract List<ObjectId> getModels(ObjectId paramObjectId, String paramString);

  public abstract long getCount(ObjectId paramObjectId, ModelQueryBean paramModelQueryBean);

  public abstract boolean isModelNameExists(ObjectId paramObjectId, String paramString);

  public abstract boolean isFieldExists(ObjectId paramObjectId, String paramString, Object paramObject);

  public abstract int getNextPlcTypeId(ObjectId paramObjectId);
}

/* Location:           D:\apache-tomcat-7.0.63-windows-x86\apache-tomcat-7.0.63\webapps\site\WEB-INF\classes\
 * Qualified Name:     cn.com.inhand.site.dao.ModelDAO
 * JD-Core Version:    0.6.1
 */