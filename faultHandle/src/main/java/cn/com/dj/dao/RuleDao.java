package cn.com.dj.dao;

import cn.com.dj.dto.Rule;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public interface RuleDao {

	/**
	 * get the rule list of a Model
	 * @param modelId
	 * @param oid
	 * @return
	 */
	public List<Rule> getRuleListByModelId(ObjectId modelId,ObjectId oid) ;
	
	/**
	 * get the rule list of a Model
	 * @param modelId
	 * @param oid
	 * @return
	 */
	public List<Rule> getRulesByRuleIds(ObjectId modelId,ObjectId oid) ;

	/**
	 * get the rule list of a Model
	 * @param pumpId
	 * @param oid
	 * @return
	 */
	public List<Rule> getRulesByPumpId(ObjectId pumpId,ObjectId oid) ;


	public Map<ObjectId, List<Rule>> getRulesByPumpIds(List<ObjectId> pumpIds,ObjectId oid);

	/**
	 * is the rule phenomenon exist?
	 * @param oId
	 * @param name
	 * @return
	 */
	public boolean isRulePhenomenonExists(ObjectId oId, String name);
	/**
	 * Get the rule by the rule id
	 * @param ruleId
	 * @param oid
	 * @return
	 */
	public Rule getRuleById(ObjectId ruleId,ObjectId oid);
	
	/**
	 * create a new rule
	 * @param rule
	 * @param oid
	 */
	public void createRule(Rule rule,ObjectId oid);
	
	/**
	 * Delete the rule based on the rule id
	 * @param ruleId
	 * @param oid
	 */
	public void deleteRule(ObjectId ruleId,ObjectId oid);
	
	/**
	 *  update the rule info
	 * @param rule
	 * @param oid
	 */
	public void modifyRule(Rule rule,ObjectId oid);

}
