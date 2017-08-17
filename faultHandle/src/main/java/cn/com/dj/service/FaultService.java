/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import cn.com.dj.dao.FaultDao;
import cn.com.dj.dto.Fault;
import cn.com.dj.dto.FaultQueryBean;
import cn.com.dj.util.QueryGenrator;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.dn4.utils.DateUtils;
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

import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * @author Jiang Du
 */
@Service("faultService")
public class FaultService extends MongoService implements FaultDao {
	private String collectionName = "faultInfo";

	@Autowired
	private QueryGenrator queryGenrator;

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
		//未派工 和已派工未接受的
		query.addCriteria(Criteria.where("status").ne(2));
		this.queryGenrator.withSortDESC(query, "createTime");
		List<Fault> faultInfos = mt.find(query, Fault.class,
				this.collectionName);
		return faultInfos;
	}

	@Override
	public Fault getFaultInfoById(ObjectId faultId, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
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
		mt.findAndModify(query, Update.update("status", Integer.valueOf(1)),
				Fault.class);
	}

	@Override
	public List<Fault> getFualtsBypage(ObjectId oId, FaultQueryBean fqb,
			int skip, int limit) {
		Query query = new Query();
		
		if(fqb.getStatus().size()>0){
			query.addCriteria(Criteria.where("status").in(fqb.getStatus()));
		}
		
		if(fqb.getLevels().size()>0){
			query.addCriteria(Criteria.where("level").in(fqb.getLevels()));
		}
		
		if (fqb.getSiteName() != null && !fqb.getSiteName().equals("")) {
			Pattern pattern = Pattern.compile("^.*"+ fqb.getSiteName()+ ".*$");  
			query.addCriteria(Criteria.where("siteName").regex(pattern));
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
		return template.find(query, Fault.class, collectionName);
	}

}
