package com.dblp.compare;

import java.util.Comparator;

import org.apache.lucene.document.Document;

public class TimeComparator  implements Comparator<Document> {

	public int compare(Document d1, Document d2){
		  // TODO Auto-generated method stub
		 int year1= Integer.parseInt(d1.get("year"));
		 int year2= Integer.parseInt(d2.get("year"));
		 if(year2>year1)
			 return 1;
		 else if(year2==year1)
			 return 0;
		 else
			 return -1;
		
		  
	}

}
/*if(u2>u1)
return 1;
else if(u2==u1)
{

}
else
return -1;
*/
/* if(important2>important1)
{
return 1;
}
else if(important2==important1)
{
if(year1<year2)
		return 1;
if(year1==year2)
	return 0;
return -1;
}
return -1;*/
/* double total1=f1*0.35+u1*0.65;
double total2=f2*0.35+u2*0.65;
double total1=Math.sqrt(u1)+f1*f1;
double total2=Math.sqrt(u2)+f1*f1;
if(total1<total2)
 return 1;
if(total1==total2)
	  return 0;
if(total1>total2)
 return -1;
return 0;*/

/* if (d2.getField("mdate").stringValue().compareTo(d1.getField("mdate").stringValue())>0) 
{
 return 1;
}
else if (d1.getField("mdate").stringValue().equals(d2.getField("mdate").stringValue())) 
{
return 0;
} 
else 
{
return -1;
}*/
/*	  float u1=Float.parseFloat(d1.getField("userRel").stringValue());//用来充当文本相关性
float u2=Float.parseFloat(d2.getField("userRel").stringValue());*/

/*	  double total1=important1*0.42+u1*0.62;
double total2=important2*0.42+u2*0.62;*/

/*if(u2>u1&&year2>year1&&important2>important1)
 return 1;*/