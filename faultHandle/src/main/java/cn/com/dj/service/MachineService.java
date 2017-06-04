/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.com.dj.dao.MachineDao;
import cn.com.inhand.common.model.Machine;
import cn.com.inhand.common.service.MongoService;

/**
 *
 * @author Jiang Du
 */
@Service
public class MachineService extends MongoService implements MachineDao{
    private String collectionName = "machine";

	@Override
	public List<Machine> getOnlineMachines(ObjectId oId,
			List<ObjectId> gatewayIds) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		return template.find(Query.query(Criteria.where("gatewayId").in(gatewayIds)), Machine.class, this.collectionName);
		 
	
	} 
}
