package cn.com.dj.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.dj.dto.Pump;

public abstract interface PumpDao {
	
	/**
	 * get the pump list of a Model
	 * @param modelId
	 * @param oid
	 * @return
	 */
	public List<Pump> getPumpListByModelId(ObjectId modelId,ObjectId oid) ;
	
	/*
	public List<Rule> getPumpListByRuleIds(List<ObjectId> modelIds,ObjectId oid) ;
	*/
	/**
	 * check whether the name of pump exsit ?
	 * @param oId
	 * @param name
	 * @return
	 */
	public boolean isPumpExists(ObjectId oId, String name);
	
	/**
	 * get the pump info by the pump id
	 * @param ruleId
	 * @param oid
	 * @return
	 */
	public Pump getPumpById(ObjectId pumpId,ObjectId oid);
	
	/**
	 * create a new pump
	 * @param pump
	 * @param oid
	 */
	public void createPump(Pump pump,ObjectId oid);
	
	/**
	 * delete a pump by the id of pump
	 * @param ruleId
	 * @param oid
	 */
	public void deletePump(ObjectId pumpId,ObjectId oid);
	
	/**
	 *  update the pump info
	 * @param pump
	 * @param oid
	 */
	public void modifyPump(Pump pump,ObjectId oid);
}
