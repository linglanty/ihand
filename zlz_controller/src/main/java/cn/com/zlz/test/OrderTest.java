package cn.com.zlz.test;

import org.bson.types.ObjectId;

import com.mongodb.Mongo;

//import com.mongodb.BasicDBObject;
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.DBObject;
//import com.mongodb.Mongo;

public class OrderTest {
	
	
	
	public static void main(String[] args) {
		
		try {
			
		String oIds = "560ccfd3fef53f717a93f153";
		 ObjectId oId=new ObjectId(oIds);
		 
		 Mongo mongo = new Mongo();
		
//		 Mongo mongo=new Mongo("localhost",27017);
//
//		  DB dba=mongo.getDB("ABCDE_db");
//		  DBCollection coll=dba.getCollection("order");
//		  DBObject doc=new BasicDBObject();

		 
		  
//		  doc.put("_id", objid);
		  
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
