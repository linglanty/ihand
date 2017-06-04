/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.com.dj.dao.DeviceDao;
import cn.com.inhand.common.model.Device;
import cn.com.inhand.common.service.MongoService;

/**
 *
 * @author Jiang Du
 */
@Service
public class DeviceService extends MongoService implements DeviceDao{
    private String collectionName = "device";

	@Override
	public List<Device> getOnlineDevices(ObjectId oId) {
		// TODO Auto-generated method stub
		 MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		 Query query=BasicQuery.query(Criteria.where("online").is(Integer.valueOf(1)));
		 return template.find(query, Device.class,this.collectionName);
	}
        
    
}
