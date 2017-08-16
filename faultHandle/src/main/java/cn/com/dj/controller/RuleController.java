/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.com.dj.controller;

import cn.com.dj.dao.RuleDao;
import cn.com.dj.dto.Rule;
import cn.com.dj.dto.RuleCreateBean;
import cn.com.dj.log.LogCode;
import cn.com.inhand.common.dto.BasicResultDTO;
import cn.com.inhand.common.dto.OnlyResultDTO;
import cn.com.inhand.common.exception.ErrorCode;
import cn.com.inhand.common.exception.ErrorCodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jiang Du
 *  配置故障规则信息
 */
@Controller
@RequestMapping({"api/rule"})
public class RuleController {

	private static Logger logger = LoggerFactory.getLogger(RuleController.class);

	@Autowired
    private RuleDao ruleService;
	
	@Autowired
	ObjectMapper mapper;

    /**
     * 展示一个型号的所有故障规则信息
     * @param accessToken
     * @param modelId
     * @param oId
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody 
    public Object getAllRuleOverviewInfo(
            @RequestParam("access_token") String accessToken,
    		@RequestParam(value="modelId", required=true) ObjectId modelId,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId) { 
    	System.out.println("RULE List API call");
        List<Rule> list = ruleService.getRuleListByModelId(modelId, oId);
        return new BasicResultDTO((long)list.size(), 0, list.size(), list);
    }

    /**
     * 根据泵的主键来获取泵的详细信息
     * @param id
     * @param accessToken
     * @param oId
     * @return
     */
    @RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Object getFaultRuleById(
            @PathVariable ObjectId id,
            @RequestParam("access_token") String accessToken,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId)
    {
    	Rule rule = this.ruleService.getRuleById(id, oId);
    	OnlyResultDTO result = new OnlyResultDTO();
    	result.setResult(rule);
    	return result;
    }

    /**
     * 添加一个故障规则信息
     * @param accessToken
     * @param verbose
     * @param xOId
     * @param xUsername
     * @param xIp
     * @param xUId
     * @param roleType
     * @param oId
     * @param ruleCb
     * @return
     */
   @RequestMapping(value={""}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
   @ResponseBody
   public Object add(
            @RequestParam("access_token") String accessToken,
    		@RequestParam(required=false, defaultValue="0") int verbose,
            @RequestHeader(value="X-API-OID", required=false) ObjectId xOId,
    		@RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp,
            @RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
    		@RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType,
            @RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
    		@Valid @RequestBody RuleCreateBean ruleCb) {
	    	if (this.ruleService.isRulePhenomenonExists(oId, ruleCb.getFaultDescription())) {
	    		throw new ErrorCodeException(ErrorCode.RESOURCE_NAME_ALREADY_EXISTS, new Object[] { ruleCb.getFaultDescription() });
	    	}
    	  	Rule rule = this.mapper.convertValue(ruleCb, Rule.class);
	      	this.ruleService.createRule(rule, oId);
	      	logger.info("id:{}, code:{}, uId:{}, userName:{}, ip:{}, ruleId:{} ", oId, LogCode.CREATE_RULE_OK, xUId, xUsername, xIp, rule.getId().toString() );
	      	return new OnlyResultDTO(rule);
      }

    /**
     * 更新规则信息
     * @param id
     * @param accessToken
     * @param rule
     * @param oId
     * @param xUsername
     * @param xIp
     * @param xUId
     * @param roleType
     * @return
     */
    @RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.PUT})
    @ResponseBody
    public Object update(
            @PathVariable ObjectId id,
            @RequestParam("access_token") String accessToken,
            @RequestBody Rule rule,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
            @RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp,
            @RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
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
    	logger.info("id:{}, code:{}, uId:{}, ruleId:{} ",oId, LogCode.UPDATE_RULE_OK, xUId, xUsername, xIp, rule.getId().toString());
    	return new OnlyResultDTO(rule);
    }

    /**
     * 删除规则信息
     * @param id
     * @param accessToken
     * @param oId
     * @param xUsername
     * @param xIp
     * @param xUId
     * @param roleType
     * @return
     */
    @RequestMapping(value={"/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.DELETE})
    @ResponseBody
    public Object deleteById(
            @PathVariable ObjectId id,
            @RequestParam("access_token") String accessToken,
    		@RequestParam(value="oid", defaultValue="54BCA345DA08A0075C000001") ObjectId oId,
            @RequestHeader(value="X-API-USERNAME", required=false) String xUsername,
    		@RequestHeader(value="X-API-IP", required=false) String xIp,
            @RequestHeader(value="X-API-UID", required=false) ObjectId xUId,
    		@RequestHeader(value="X-API-ROLE-TYPE", required=false) Integer roleType){
    	Rule rule = this.ruleService.getRuleById(id,oId);
    	if(rule==null){
    		throw new ErrorCodeException(ErrorCode.RESOURCE_DOES_NOT_EXIST, new Object[] { id });
    	}
    	this.ruleService.deleteRule(id, oId);
    	logger.info("id:{}, code:{}, uId:{}, userName:{}, ruleId:{} ", oId, LogCode.DELETE_RULE_OK, xUId, xUsername, xIp, rule.getId().toString());
    	OnlyResultDTO result = new OnlyResultDTO();
    	Map<String,ObjectId> resultMap = new HashMap<String,ObjectId>();
    	resultMap.put("id", id);
    	result.setResult(resultMap);
    	return result;
    }
}
