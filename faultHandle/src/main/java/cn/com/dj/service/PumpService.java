package cn.com.dj.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cn.com.dj.dao.PumpDao;
import cn.com.dj.dto.Pump;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.util.UpdateUtils;

@Service
public class PumpService extends MongoService implements PumpDao {

	private String collectionName = "pump";

	private static final Logger logger = LoggerFactory
			.getLogger(PumpService.class);
	
	
	@Override
	public List<Pump> getPumpListByModelId(ObjectId modelId, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		query.addCriteria(Criteria.where("modelId").is(modelId));
		List<Pump> faultRules = mt.find(query, Pump.class,this.collectionName);
		return faultRules;
		
	}

	@Override
	public boolean isPumpExists(ObjectId oId, String name) {
		Query query = Query.query(Criteria.where("pumpName").is(name));
	    return exist(oId, query, collectionName);
	}

	@Override
	public Pump getPumpById(ObjectId pumpId, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(pumpId));
		return mt.findOne(query, Pump.class, this.collectionName);
	}

	@Override
	public void createPump(Pump pump, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		mt.save(pump,this.collectionName);
		logger.info("create a pump info to mongo id = " + pump.getId());
	}

	@Override
	public void deletePump(ObjectId pumpId, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = BasicQuery.query(Criteria.where("_id").is(pumpId));
		mt.remove(query, Pump.class, this.collectionName);
		logger.info("Delete pump Info from mongo where id = " + pumpId);
	}

	@Override
	public void modifyPump(Pump pump, ObjectId oid) {
		Assert.notNull(pump.getId());
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = BasicQuery.query(Criteria.where("_id").is(pump.getId()));
		mt.updateFirst(query, UpdateUtils.convertBeanToUpdate(pump, new String[] { "_id" }), this.collectionName);
		logger.info("update Pump Info from mongo where id = " + pump.getId());
		
	}

}
