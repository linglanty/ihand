/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import cn.com.dj.dao.FaultDumpDao;
import cn.com.dj.dto.Fault;
import cn.com.dj.dto.FaultDump;
import cn.com.dj.util.QueryGenrator;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.util.UpdateUtils;
import cn.com.inhand.dn4.utils.DateUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 
 * @author Jiang Du
 */
@Service
public class FaultDumpService extends MongoService implements FaultDumpDao {
	private String collectionName = "faultDump";

	@Autowired
	private QueryGenrator queryGenrator;

	private static final Logger logger = LoggerFactory
			.getLogger(FaultDumpService.class);

	@Override
	public List<Fault> getAllunHandledFaultList(ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		//未派工 和已派工未接受的
		this.queryGenrator.withSortDESC(query, "createTime");
		List<Fault> faultInfos = mt.find(query, Fault.class,
				this.collectionName);
		return faultInfos;
	}

	@Override
	public FaultDump getFaultDumpById(ObjectId ruleId,Integer reasonId, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		query.addCriteria(Criteria.where("ruleId").is(ruleId));
		query.addCriteria(Criteria.where("reasonId").is(reasonId));
		return mt.findOne(query, FaultDump.class, this.collectionName);
	}

	@Override
	public void createFaultDump(FaultDump faultdump, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		faultdump.setCreateTime(Long.valueOf(DateUtils.getUTC()));
		mt.save(faultdump, this.collectionName);
		logger.info("create a FaultDumpInfo info to mongo id = " + faultdump.getId());
	}

	@Override
	public void updateFaultDump(FaultDump faultdump, ObjectId oid) {
		Assert.notNull(faultdump.getId());
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		faultdump.setCreateTime(Long.valueOf(DateUtils.getUTC()));
		Query query = BasicQuery.query(Criteria.where("_id").is(faultdump.getId()));
		mt.updateFirst(query, UpdateUtils.convertBeanToUpdate(faultdump, new String[] { "_id" }), this.collectionName);
		
	}


}
