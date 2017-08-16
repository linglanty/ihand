package cn.com.dj.dao;

import cn.com.dj.dto.Fault;
import cn.com.dj.dto.FaultQueryBean;
import org.bson.types.ObjectId;

import java.util.List;

public interface FaultDao {

	//get all fault info
	List<Fault> getAllFaultsList(ObjectId oid);
	
	/**
	 * get faults per page
	 * @param oId
	 * @param fqb: the query bean
	 * @param skip
	 * @param limit
	 * @return
	 */
	List<Fault> getFualtsBypage(ObjectId oId, FaultQueryBean fqb, int skip, int limit);
	
	//get all unhandled fault info
	List<Fault> getAllunHandledFaultList(ObjectId oid) ;
	
	//Get the fault info by the fault id
	Fault getFaultInfoById(ObjectId faultId, ObjectId oid);
	
	//create a new rule
	void createFault(Fault fault, ObjectId oid);
	
	// modify the fault status
	void modifyFaultStatus(Fault fault, ObjectId oid);
}
