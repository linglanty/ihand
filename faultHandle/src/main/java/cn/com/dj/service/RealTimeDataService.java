/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import cn.com.dj.dao.RealTimeDataDao;
import cn.com.dj.dto.RealTimeData;
import cn.com.dj.dto.RealTimeVariable;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.util.UpdateUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Jiang Du
 */
@Service
public class RealTimeDataService extends MongoService implements
		RealTimeDataDao {
	private String collectionName = "rt_data";

	@Override
	public List<RealTimeData> getAllRealTimeData(ObjectId oId,
			List<ObjectId> machineIds) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		return template.find(
				Query.query(Criteria.where("deviceId").in(machineIds)),
				RealTimeData.class, this.collectionName);
	}

	@Override
	public Map<String, RealTimeVariable> getAllRealTimeMap(
			ObjectId oId, ObjectId machineId) {
		Map<String, RealTimeVariable> realVar = getRealTimeDataFromRTData(
				oId, machineId);
		return realVar;
	}

	private Map<String, RealTimeVariable> getRealTimeDataFromRTData(
			ObjectId oId, ObjectId deviceId) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		Map<String, RealTimeVariable> result = new HashMap<String, RealTimeVariable>();
		// Map<String, RealTimeVariable> trDataMap = (Map<String,
		// RealTimeVariable>) template.findOne(query, Map.class,collectionName);
		Query query = Query.query(Criteria.where("deviceId").is(deviceId));
		query.fields().exclude("_id").exclude("sensorId").exclude("deviceId");
		Map<String, Map<String, String>> trDataMap = (Map<String, Map<String, String>>) template
				.findOne(query, Map.class, this.collectionName);
		
		if ((trDataMap != null) && (trDataMap.size() > 0)) {
			for (String key : trDataMap.keySet()) {
				result.put(key,
						RealTimeVariable.getInstance(trDataMap.get(key)));// 通过map将map中的内容转换成realtimevariables
			}
		}
		return result;
		
	}

	/**
	 * 创建 RtData 数据
	 */
	@Override
	public void createRtData(ObjectId oId, RealTimeData rtData) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		template.save(rtData, this.collectionName);

	}

	// 获取RtData数据
	@Override
	public RealTimeData getRtData(ObjectId oId, ObjectId rtId) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oId);
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(rtId));
		return mt.findOne(query, RealTimeData.class, this.collectionName);
	}

	// 修改
	@Override
	public void modifyRt_Data(RealTimeData rtData, ObjectId oid) {
		Assert.notNull(rtData.get_id());
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = BasicQuery.query(Criteria.where("_id")
				.is(rtData.get_id()));
		mt.updateFirst(
				query,
				UpdateUtils.convertBeanToUpdate(rtData, new String[] { "_id" }),
				this.collectionName);

	}

}
