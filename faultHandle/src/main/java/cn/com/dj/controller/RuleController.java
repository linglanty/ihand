/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.dj.dao.RuleDao;
import cn.com.dj.dto.Rule;
import cn.com.dj.dto.RuleCreateBean;
import cn.com.dj.log.BusinessLogger;
import cn.com.dj.log.LogCode;
import cn.com.inhand.common.dto.BasicResultDTO;
import cn.com.inhand.common.dto.OnlyResultDTO;
import cn.com.inhand.common.exception.ErrorCode;
import cn.com.inhand.common.exception.ErrorCodeException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Jiang Du
 */
@Controller
@RequestMapping({"api/rule"})
public class RuleController {
    
	@Autowired
    private RuleDao ruleService;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	BusinessLogger businessLogger;
    
	//list all of the rule of a model
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody 
    public Object getAllRuleOverviewInfo(@RequestParam("access_token") String accessToken,
    		@RequestParam(value="modelId", required=true) ObjectId modelId,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId) { 
    	System.out.println("RULE List API call");
        List<Rule> list = ruleService.getRuleListByModelId(modelId, oId);
        return new BasicResultDTO((long)list.size(), 0, list.size(), list);
    }
    
    //get the rule info by the rule id
    @RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Object getFaultRuleById(@PathVariable ObjectId id, @RequestParam("access_token") String accessToken,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId)
    {
    	Rule rule = this.ruleService.getRuleById(id, oId);
    	OnlyResultDTO result = new OnlyResultDTO();
    	result.setResult(rule);
    	return result;
    }
    
   @RequestMapping(value={""}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
   @ResponseBody
   public Object add(@RequestParam("access_token") String accessToken,
    		@RequestParam(required=false, defaultValue="0") int verbose, @RequestHeader(value="X-API-OID", required=false) ObjectId xOId, 
    		@RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp, @RequestHeader(value="X-API-UID", required=false) ObjectId xUId, 
    		@RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType,@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
    		@Valid @RequestBody RuleCreateBean ruleCb) {
	    	if (this.ruleService.isRulePhenomenonExists(oId, ruleCb.getFaultDescription())) {
	    		throw new ErrorCodeException(ErrorCode.RESOURCE_NAME_ALREADY_EXISTS, new Object[] { ruleCb.getFaultDescription() });
	    	}
    	  	Rule rule = (Rule)this.mapper.convertValue(ruleCb, Rule.class);
	      	this.ruleService.createRule(rule, oId);
	      	this.businessLogger.info(oId, LogCode.CREATE_RULE_OK, xUId, xUsername, xIp, new String[] { rule.getId().toString() });
	      	return new OnlyResultDTO(rule);
      }
    
    //update the rule info
    @RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.PUT})
    @ResponseBody
    public Object update(@PathVariable ObjectId id,@RequestParam("access_token") String accessToken, @RequestBody Rule rule,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,@RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp, @RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
    		@RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType){
    	rule.setId(id);
    	Rule oldRule = this.ruleService.getRuleById(id,oId);
    	if(oldRule==null){
    		throw new ErrorCodeException(ErrorCode.RESOURCE_DOES_NOT_EXIST,id);
    	}
    	if (rule.getFaultDescription()!=null&&!rule.getFaultDescription().equals(oldRule.getFaultDescription())&&this.ruleService.isRulePhenomenonExists(oId, rule.getFaultDescription())) {
    		throw new ErrorCodeException(ErrorCode.RESOURCE_NAME_ALREADY_EXISTS, new Object[] { rule.getFaultDescription() });
		}	
    	this.ruleService.modifyRule(rule, oId);
    	this.businessLogger.info(oId, LogCode.UPDATE_RULE_OK, xUId, xUsername, xIp, new String[] { rule.getId().toString() });
    	return new OnlyResultDTO(rule);
    }
    
    //delete a rule by the id
    @RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.DELETE})
    @ResponseBody
    public Object deleteById(@PathVariable ObjectId id,@RequestParam("access_token") String accessToken,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,@RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp, @RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
    		@RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType){
    	Rule rule = this.ruleService.getRuleById(id,oId);
    	if(rule==null){
    		throw new ErrorCodeException(ErrorCode.RESOURCE_DOES_NOT_EXIST, new Object[] { id });
    	}
    	this.ruleService.deleteRule(id, oId);
    	this.businessLogger.info(oId, LogCode.DELETE_RULE_OK, xUId, xUsername, xIp, new String[] { rule.getId().toString() });
    	OnlyResultDTO result = new OnlyResultDTO();
    	Map<String,ObjectId> resultMap = new HashMap<String,ObjectId>();
    	resultMap.put("id", id);
    	result.setResult(resultMap);
    	return result;
    }
    
    
    
}
