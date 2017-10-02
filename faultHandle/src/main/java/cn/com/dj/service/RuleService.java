/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.service;

import cn.com.dj.dao.RuleDao;
import cn.com.dj.dto.Rule;
import cn.com.inhand.common.service.MongoService;
import cn.com.inhand.common.util.UpdateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Jiang Du
 */
@Service
public class RuleService extends MongoService implements RuleDao{
    private String collectionName = "faultRule";
    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);
    //get the rule list of a Model
	@Override
	public List<Rule> getRuleListByModelId(ObjectId modelId,ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		query.addCriteria(Criteria.where("modelId").is(modelId));
		List<Rule> faultRules =mt.find(query, Rule.class,this.collectionName);
		return faultRules;
	}

	//create a new rule
	@Override
	public void createRule(Rule rule,ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		mt.save(rule,this.collectionName);
		logger.info("create a FaultRule info to mongo id = " + rule.getId());
	}
	//Delete the rule based on the rule id
	@Override
	public void deleteRule(ObjectId ruleId,ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = BasicQuery.query(Criteria.where("_id").is(ruleId));
		mt.remove(query, Rule.class, this.collectionName);
		logger.info("Delete FaultRule Info from mongo where id = " + ruleId);
	}
	
	// update the rule info
	@Override
	public void modifyRule(Rule rule,ObjectId oid) {
		Assert.notNull(rule.getId());
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = BasicQuery.query(Criteria.where("_id").is(rule.getId()));
		mt.updateFirst(query, UpdateUtils.convertBeanToUpdate(rule, new String[] { "_id" }), this.collectionName);
		logger.info("update FaultRule Info from mongo where id = " + rule.getId());
	}
	
	@Override
	//Get the rule by the rule id
	public Rule getRuleById(ObjectId ruleId, ObjectId oid) {
		MongoTemplate mt = this.factory.getMongoTemplateByOId(oid);
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(ruleId));
		return mt.findOne(query, Rule.class, this.collectionName);
	}

	@Override
	public boolean isRulePhenomenonExists(ObjectId oId, String name) {
		Query query = Query.query(Criteria.where("faultPhenomenon").is(name));
	    return exist(oId, query, "model");
	}

	@Override
	public List<Rule> getRulesByRuleIds(ObjectId modelId, ObjectId oId) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oId);
		return template.find(Query.query(Criteria.where("modelId").is(modelId)), Rule.class, this.collectionName);
	}

	@Override
	public List<Rule> getRulesByPumpId(ObjectId pumpId,ObjectId oid) {
		MongoTemplate template = this.factory.getMongoTemplateByOId(oid);
		return template.find(Query.query(Criteria.where("pumpId").is(pumpId)), Rule.class, this.collectionName);
	}

	public Map<ObjectId, List<Rule>> getRulesByPumpIds(List<ObjectId> pumpIds,ObjectId oid) {
		Map<ObjectId, List<Rule>> res = Maps.newHashMap();
		MongoTemplate template = this.factory.getMongoTemplateByOId(oid);
		List<Rule> rules = template.find(Query.query(Criteria.where("pumpId").in(pumpIds)), Rule.class, this.collectionName);
		for (Rule rule: rules) {
			addVal(res, rule.getPumpId(), rule);
		}
		return res;
	}

	private void addVal(Map<ObjectId, List<Rule>> maps, ObjectId key, Rule rule) {
		if (!maps.containsKey(key)) {
			maps.put(key, Lists.<Rule>newArrayList());
		}
		List<Rule> rules = maps.get(key);
		rules.add(rule);
	}
    
}
