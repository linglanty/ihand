package com.dblp.compare;

import java.util.Comparator;

import org.apache.lucene.document.Document;

public class ImportantComparator implements Comparator<Document> {

	public int compare(Document d1, Document d2) {
		// TODO Auto-generated method stub
		  float f1= Float.parseFloat(d1.getField("importindex").stringValue());//重要程度
		  float f2= Float.parseFloat(d2.getField("importindex").stringValue());
		  if(f2>f1)
			  return 1;
		  if(f2==f1)
			  return 0;
		  else
			  return -1;
	}
}
/*  double total1=f1*u1;
double total2=f2*u2;*/
/*		  if(u2>u1)
return 1;
else if(u2==u1)
{
if(f2>f1)
	  return 1;
else if(f2==f1)
	  return 0;
else
	  return -1;
}
else
return 0;*/
/*  if(u1<0.15) u1=0;
if(u2<0.15) u2=0;
double total1=f1*0.4+u1*0.6;
double total2=f2*0.4+u2*0.6;
double total1=Math.sqrt(u1)+f1*f1;
double total2=Math.sqrt(u2)+f1*f1;
if(total1<total2)
return 1;
if(total1==total2)
  return 0;
if(total1>total2)
return -1;
return 0;*/
/*  if (d2.getField("importindex").stringValue().compareTo(d1.getField("importindex").stringValue())>0) 
{
return 1;
}
else if (d1.getField("importindex").stringValue().equals(d2.getField("importindex").stringValue())) 
{
return 0;
} 
else 
{
return -1;
}*/