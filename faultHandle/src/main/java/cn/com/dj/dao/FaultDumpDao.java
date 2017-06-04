package cn.com.dj.dao;

import java.util.List;

import org.bson.types.ObjectId;

import cn.com.dj.dto.Fault;
import cn.com.dj.dto.FaultDump;

public interface FaultDumpDao {

	
	
	//get all unhandled fault info
	public List<Fault> getAllunHandledFaultList(ObjectId oid) ;
	
	//Get the fault info by the fault id
	public FaultDump getFaultDumpById(ObjectId ruleId,Integer reasonId,ObjectId oid);
	
	//create a new faultDump
	public void createFaultDump(FaultDump faultDump,ObjectId oid);
	
	//delete a faultDump
	public void updateFaultDump(FaultDump faultDump,ObjectId oid);
	
	
}
