/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.com.dj.dao.DataDao;
import cn.com.dj.dao.RealTimeDataDao;
import cn.com.dj.dto.Data;
import cn.com.dj.dto.Fault;
import cn.com.dj.dto.RealTimeData;
import cn.com.dj.dto.RealTimeVariable;
import cn.com.dj.dto.Rule;
import cn.com.dj.util.QueryGenrator;
import cn.com.inhand.common.exception.ErrorCode;
import cn.com.inhand.common.exception.ErrorCodeException;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.util.UpdateUtils;

/**
 * 
 * @author Jiang Du
 */
@Service
public class DataService extends MongoService implements
		DataDao {
	private String collectionName = "data";

	@Autowired
	private QueryGenrator queryGenrator;
	
	@Override
	public Data getlastestData(ObjectId deviceId, String varId,ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		query.addCriteria(Criteria.where("deviceId").is(deviceId));
		query.addCriteria(Criteria.where("id").is(varId));
		
		this.queryGenrator.withSortDESC(query, "endTime");
		
		return mt.findOne(query, Data.class, this.collectionName);
	}


}
