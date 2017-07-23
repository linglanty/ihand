package com.dblp.util;

import java.io.File;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;

import com.dblp.Search.Analyzer.SynonymAnalyzer;
import com.dblp.Search.Analyzer.WordNetSynonymEngine;
import com.dblp.dao.DocumentDao;
import com.dblp.model.Documents;
import com.dblp.model.QueryResults;

public class Tools {
	
	private static int CurrentYear;
	static{
		Calendar cal = Calendar.getInstance();
		CurrentYear=cal.get(Calendar.YEAR);
	}
	//计算重要性指标
	private static 	Analyzer analyzer=new SynonymAnalyzer(new WordNetSynonymEngine(new File("G:\\wordnetindex")));
//	private static DocumentDao daos=new DocumentDao();
	public static float calculate(Connection conn,String id,String Year) {
		
		Documents docu= DocumentDao.GetOneDocument(conn, Integer.parseInt(id));
		if(docu==null)
			return 0;
	//	System.out.println(id+"Year:"+Year+"grade:"+docu.getGrade()+"Hits:"+docu.getHits()+"Citenum:"+docu.getCitenum());
		float ss=(float)((docu.getCitenum()*0.01/(CurrentYear+10- Integer.parseInt(Year)))+docu.getHits()*1.0/100+docu.getGrade());
		
		//float ss=(float)((docu.getCitenum()*0.01/(CurrentYear+10-Integer.parseInt(Year)))+docu.getHits()*1.0/100+docu.getGrade());
		//System.out.println(ss);
		return ss;
		
	//	return grade+(float)(citenum*1.0/(2013-Integer.parseInt(Year)+1));//learning to rank
	}
	//是否在领域里
	public static boolean Isinfield(int f1,String field2) {
		
		if(field2.indexOf(String.valueOf(f1))>=0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	//字符串相似程度
    public static float Similarity(String s1,String s2) {
		
    	s1=s1.toLowerCase();
		s2=s2.toLowerCase();
		if(s1.lastIndexOf('.')==s1.length()-1)
		{
			s1=s1.substring(0, s1.length()-2);
		}
		
		String[] quers=s1.split(" ");
		int i=0;
		for(int j=0;j<quers.length;j++)
		{
			if(s2.indexOf(quers[j])>=0)
			{
				i++;
			}
		}
		return (float)(i*1.0/quers.length);
	}
	
	//获取文献的类型
	public static String GetType(int type) {
		switch(type)
		{
			case 1:return "article";
			case 2:return "inproceedings";
			case 3:return "book";
			case 4:return "incollection";
			case 5:return "phdthesis";
			case 6:return "masterthesis";
			default: return null;
		}
//		1, 'article', '期刊或杂志上的文章'
//		2, 'inproceedings', '会议论文集中的一篇文章'
//		3, 'book', '有出版社的书籍'
//		4, 'incollection', '一本书中有自己题目的一部分'
//		5, 'phdthesis', '博士论文'
//		6, 'masterthesis', '硕士论文'
	}
	
	public static String GetArea(int type) {
		switch (type)
		{
			case 1:return "Computer systems & high-performance computing";
			case 2:return "Network & Information Security";
			case 3:return "Software engineering, system software and programming languages";
			case 4:return "Databases, data mining and content retrieval";
			case 5:return "Computer Graphics & Multimedia";
			case 6:return "Artificial Intelligence & Pattern Recognition";
			case 7:return "Human-computer interaction & ubiquitous computing";
			case 8:return "Frontier crossing & comprehensive";
			case 9:return "Theoretical Computer Science";
			default:return "Computer networks";
		}
	}
	
	public   static String StringFilter(String str)  {
        // 只允许字母和数字       
        // String   regEx  =  "[^a-zA-Z0-9]";                     
           // 清除掉所有特殊字符
	  str=str.toLowerCase();
	  String regEx="[\\-,*()\\+=:?^!~\"{}\\[\\]$]";// String regEx="[*()\\-+{}\\[\\]]";
	  Pattern p   =   Pattern.compile(regEx);
	  Matcher m   =   p.matcher(str);
	  return   m.replaceAll(" ").replace(" or ", " ").replace(" not "," ").replace(" and ", " ").replace("'", "");     
	}
	
	public static void Setpearconf(Connection conn, Documents docu,int type,int pjid)
	{
		
//		Connection conn= DataLink.conn;
		String sql="SELECT Abbr,Fullname,grade FROM t_peraconf where id=?";
		try {
			PreparedStatement pre=conn.prepareStatement(sql);
			pre.setInt(1, pjid);
			ResultSet set=pre.executeQuery();
			while(set.next())
			{
				if(type==1)
				{
					docu.setAbbr(set.getString("Abbr"));
					docu.setJourac((set.getString("Fullname")));
					docu.setGrade(set.getInt("grade"));
				}
				else
				{
					docu.setAbbr(set.getString("Abbr"));
					docu.setConf(set.getString("Fullname"));
					docu.setGrade(set.getInt("grade"));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static QueryResults SeachResult(Analyzer analyzer, IndexSearcher indexsearcher,Highlighter highlighter,int firstResult,int maxResult,TopDocs topDocs)  throws Exception
	{
		List<Document> documents=new ArrayList<Document>();
		int end = Math.min(firstResult+maxResult, topDocs.totalHits);
		for(int i=firstResult;i<end;i++)
		{
			ScoreDoc scoredoc=topDocs.scoreDocs[i];
			int docSn=scoredoc.doc;
			Document docum=indexsearcher.doc(docSn);
			
			String hcString= highlighter.getBestFragment(analyzer, "title",docum.get("title"));
			if(hcString!=null)
			{
				docum.getField("title").setValue(hcString);
			}
			else
			{
				String hcString2=highlighter.getBestFragment(analyzer, "author",docum.get("author"));
				if(hcString2!=null)
				{
					docum.getField("author").setValue(hcString2);
				}
			}
			documents.add(docum);
		}
		return new QueryResults(100,topDocs.totalHits,documents);
	}
	
	public static int KeyCount(String queryString) {
		String[] quers=queryString.split(" ");
		return quers.length;
	}

	public static List<String> analyseZhQuery(String str) throws Exception
	{
		List<String> strResult=new ArrayList<String>();
		String[] strall=str.split(" ");
		TokenStream tokenstream=null;
		TermAttribute termAtt =null;
		String temp="";//拼接数组的作用
		for(int i=0;i<strall.length;i++)
		{
			temp="";
			tokenstream=analyzer.tokenStream("content", new StringReader(strall[i]));
			termAtt= (TermAttribute) tokenstream.getAttribute(TermAttribute.class); 
			while(tokenstream.incrementToken())
			{
				if(temp=="")
				{
					temp=termAtt.term();
				}
				else
				{
					temp+="###"+termAtt.term();
				}
			}
			if(temp.trim()!="")
				strResult.add(temp);
		}
			return strResult;
	}
	
	public static List<String> analyseZhTest(String str) throws Exception
	{
		
		TokenStream tokenstream =analyzer.tokenStream("content", new StringReader(str));
		 TermAttribute termAtt = (TermAttribute) tokenstream.getAttribute(TermAttribute.class); 
//		 String[] strResult;
		 List<String> strResult=new ArrayList<String>();
		while(tokenstream.incrementToken())
		{
			strResult.add(termAtt.term()); 
		}
		return strResult;
	}
	
	public static int analyseCount(List<String> str,String title) throws Exception
	{//用来计算匹配数目
		TokenStream tokenstream =analyzer.tokenStream("content", new StringReader(title));
		int count=0;
		int i=0;
		 TermAttribute termAtt = (TermAttribute) tokenstream.getAttribute(TermAttribute.class); 
		 String result="";
		while(tokenstream.incrementToken())
		{
			result=result.concat(" "+termAtt.term());
		}
		
		result+=" ";
//		System.out.print(result);
		String[] temp2=null;//截取str
		while(i<str.size())
		{
			if(str.get(i).indexOf("###")==-1)//不含有###,即同义词
			{
				if(result.contains(" "+str.get(i)+" "))
					count++;
			}
			else
			{
				temp2=str.get(i).split("###");//含有计算匹配个数
				boolean Ismicro=false;
				boolean Isblog=false;
				for(int k=0;k<temp2.length;k++)
				{
					if(result.contains(" "+temp2[k]+" "))//当对于microblog的筛选
					{
						
						if(temp2[k].equals("micro")||temp2[k].equals("blog"))
						{
							if(temp2[k].equals("micro"))
							{
								Ismicro=true;
							}
							else 
							{
								Isblog=true;
							}
							if(Isblog&&Ismicro)
								count++;
						}//s(400510317,1,'operate',n,1,,0).
						else if(temp2[k].equals("operat")||temp2[k].equals("system"))
						{

							if(temp2[k].equals("operat"))
							{
								Ismicro=true;
							}
							else 
							{
								Isblog=true;
							}
							if(Isblog&&Ismicro)
								count++;
						}//filemaker
						else if(temp2[k].equals("filemaker")||temp2[k].equals("pro"))
						{
							if(temp2[k].equals("filemaker"))
							{
								Ismicro=true;
							}
							else 
							{
								Isblog=true;
							}
							if(Isblog&&Ismicro)
								count++;
						}
						else
						{
							count++;
							break;
						}
					}
				}
			}
			i++;
		}
		return count;
//		return strResult;
	}
	
	public static void ShowDocument(QueryResults results)
	{
		if(results.getRecordCount()==0)
		{
			System.out.println("未找到您想要的结果!!");
			return;
		}
		System.out.println("搜索结果中共有"+results.getRecordCount()+"条记录!!");
		List<Document> documents=results.getRecordList();
		int i=0;
		while(i<documents.size())
		{
			Document document=documents.get(i);
			System.out.println(document.get("title"));
		/*	Document document=documents.get(i);
			System.out.println("id:  "+document.get("id"));
			System.out.println("mdate:  "+document.get("mdate"));
			System.out.println("title:  "+document.get("title"));
			System.out.println("author:  "+document.get("author"));
			System.out.println("importIndex:  "+document.get("importindex"));
			System.out.println("year:  "+document.get("year"));
			System.out.println("userRel:  "+document.get("userRel"));*/
			i++;
		}

	}

	//将docum获取的信息转换成所需要的Documents
	public static List<Documents> Transdoc(Connection conn,List<Document> docu) {
		
		List<Documents> documentlist=new ArrayList<Documents>();
		for(int i=0;i<docu.size();i++)
		{
			Documents documents=DocumentDao.GetDocument(conn, Integer.parseInt(docu.get(i).get("id")));
			if(documents==null)
				documents=new Documents();
			documents.setId(Integer.parseInt(docu.get(i).get("id")));
			documents.setTitle(docu.get(i).get("title"));
			if(docu.get(i).get("author")!=null)
				documents.setAuthor(docu.get(i).get("author"));
			documentlist.add(documents);
		}
		return documentlist;
	}
	//时间转换器
	public static String convertTime(String year,int Type) {
		
		 Pattern pattern = Pattern.compile("[0-9]*");
		    if(!pattern.matcher(year).matches())
		    	return "0000";
		int result=0;
		
		/*	Calendar cal = Calendar.getInstance();
			return String.valueOf(cal.get(Calendar.YEAR));
		*/
		
		int intyear= Integer.parseInt(year);
		if(Type==1)
		{
			if(intyear<1000)
				result=intyear+1000;
		}
		else
		{
			if(intyear<1000)
				result=intyear+2000;
		}
		return String.valueOf(result);
		
	}
/*	
	public static boolean IsClick(Connection conn,int Uid,int Docuid,String query)
	{
		
		
		return true;
	}
	*/
	
}
