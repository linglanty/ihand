package cn.com.zlz.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.dj.model.*;
import cn.com.zlz.dto.FaultQueryBean;



public interface FaultDAO {

	//get all fault info
	public List<Fault> getAllFaultsList(ObjectId oid);
	
	/**
	 * get faults per page
	 * @param oId
	 * @param fqb: the query bean
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<Fault> getFualtsBypage(ObjectId oId,FaultQueryBean fqb, int skip, int limit);
	
	//get all unhandled fault info
	public List<Fault> getAllunHandledFaultList(ObjectId oid) ;
	
	//Get the fault info by the fault id
	public Fault getFaultInfoById(ObjectId faultId,ObjectId oid);
	
	//create a new rule
	public void createFault(Fault fault,ObjectId oid);
	
	// modify the fault status
	public void modifyFaultStatus(Fault fault,ObjectId oid);
	
}
