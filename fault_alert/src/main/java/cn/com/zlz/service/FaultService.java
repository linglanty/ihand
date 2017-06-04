/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.zlz.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import cn.com.dj.model.*;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.dn4.utils.DateUtils;
import cn.com.zlz.dao.FaultDAO;
import cn.com.zlz.dto.FaultQueryBean;
import cn.com.zlz.util.QueryGenerator;

/**
 * 
 * @author Jiang Du
 */
@Service
public class FaultService extends MongoService implements FaultDAO {
	private String collectionName = "faultInfo";

	@Autowired
	private QueryGenerator queryGenrator;

	private static final Logger logger = LoggerFactory
			.getLogger(FaultService.class);

	@Override
	public List<Fault> getAllFaultsList(ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		List<Fault> faultInfos = mt.find(query, Fault.class,
				this.collectionName);
		return faultInfos;
	}

	@Override
	public List<Fault> getAllunHandledFaultList(ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		query.addCriteria(Criteria.where("status").is(0));
		this.queryGenrator.withSortDESC(query, "createTime");
		List<Fault> faultInfos = mt.find(query, Fault.class,
				this.collectionName);
		return faultInfos;
	}

	@Override
	public Fault getFaultInfoById(ObjectId faultId, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		System.out.println("--> " + faultId + " " + oid);
		query.addCriteria(Criteria.where("_id").is(faultId));
		return mt.findOne(query, Fault.class, this.collectionName);
	}

	@Override
	public void createFault(Fault fault, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		fault.setCreateTime(Long.valueOf(DateUtils.getUTC()));
		fault.setStatus(0);
		mt.save(fault, this.collectionName);
		logger.info("create a FaultInfo info to mongo id = " + fault.getId());
	}

	@Override
	public void modifyFaultStatus(Fault fault, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = BasicQuery.query(Criteria.where("_id").is(fault.getId()));
		
		System.out.println("faultId: " + fault.getId() + " " + oid);
		
		mt.findAndModify(query, Update.update("status", fault.getStatus()),
				Fault.class, this.collectionName);
	}

	@Override
	public List<Fault> getFualtsBypage(ObjectId oId, FaultQueryBean fqb,
			int skip, int limit) {
		Query query = new Query();
		
		if(fqb.getStatus().size()>0){
			query.addCriteria(Criteria.where("status").in(fqb.getStatus()));
		}
		if (fqb.getMachinename() != null && !fqb.getMachinename().equals("")) {
			query.addCriteria(Criteria.where("machineName").is(
					fqb.getMachinename()));
		}
		Criteria criteria = Criteria.where("createTime");
		if (fqb.getStartTime() != 0) {
			criteria.gte(Long.valueOf(fqb.getStartTime()));
		}
		if (fqb.getEndTime() != 0) {
			criteria.lte(fqb.getEndTime());
		}
		if (fqb.getStartTime() != 0 || fqb.getEndTime() != 0) {
			query.addCriteria(criteria);
		}
		this.queryGenrator.withSortDESC(query, "createTime");
		query.limit(limit);
		query.skip(skip);
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		return template.find(query, Fault.class, "faultInfo");
	}

}
