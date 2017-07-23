/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import cn.com.dj.dao.DeviceDao;
import cn.com.inhand.common.model.Device;
import cn.com.inhand.common.service.MongoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Jiang Du
 */
@Service
public class DeviceService extends MongoService implements DeviceDao{
    private String collectionName = "device";

	@Override
	public List<ObjectId> getOnlineDevices(ObjectId oId) {
		 MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		 Query query=BasicQuery.query(Criteria.where("online").is(Integer.valueOf(1)));
		 List<Device> devices = template.find(query, Device.class,this.collectionName);
		 List<ObjectId> objectIds = Lists.newArrayList();
		 if(CollectionUtils.isEmpty(devices)) {
			return objectIds;
		 }
		for (Device device : devices) {
			objectIds.add(device.getId());
		}
		return objectIds;
	}

	@Override
	public Map<ObjectId, Device> getDevicesByIds(ObjectId oId, List<ObjectId> objectIds) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		Query query=new Query();
		query.addCriteria(Criteria.where("_id").in(objectIds));
		List<Device> devices = template.find(query, Device.class,this.collectionName);
		if (CollectionUtils.isEmpty(devices)) {
			return Maps.newHashMap();
		}

		Map<ObjectId, Device> deviceMap = Maps.newHashMap();
		for (Device device : devices) {
			deviceMap.put(device.getId(), device);
		}
		return deviceMap;
	}

	public Device getDeviceById(ObjectId oId, ObjectId deviceId) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		Query query=new Query();
		query.addCriteria(Criteria.where("_id").is(deviceId));
		Device device = template.findOne(query, Device.class,this.collectionName);
		return device;
	}

}
