package com.dblp.compare;

import java.util.Comparator;

import org.apache.lucene.document.Document;

public class UserRelComparator implements Comparator<Document> {

	public int compare(Document arg0, Document arg1) {
		// TODO Auto-generated method stub
			float f1= Float.parseFloat(arg0.getField("importindex").stringValue());//重要程度
		  float f2= Float.parseFloat(arg1.getField("importindex").stringValue());
		  
		  if(f2>f1)
			  return 1;
		  else if(f2==f1)
			  return 0;
		  else
			  return -1;
		  
		 /* if(u2>u1)
			  return 1;
		  else if(u2==u1)
		  {
			
		  }
		  else
			  return -1;*/
	}

	
	
}
