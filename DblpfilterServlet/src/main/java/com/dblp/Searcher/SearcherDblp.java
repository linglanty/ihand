package com.dblp.Searcher;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.dblp.Search.Analyzer.SynonymAnalyzer;
import com.dblp.Search.Analyzer.WordNetSynonymEngine;
import com.dblp.compare.ImportantComparator;
import com.dblp.compare.TimeComparator;
import com.dblp.compare.UserRelComparator;
import com.dblp.dao.RelevanceDao;
import com.dblp.model.QueryResults;
import com.dblp.util.Tools;

public class SearcherDblp {
	
	//private static String indexpath="G:\\DBLP\\DblpIndex";
	private static String indexpath="G:\\DblpIndex";
	private static 	Analyzer analyzer=new SynonymAnalyzer(new WordNetSynonymEngine(new File("G:\\wordnetindex")));
	
	private static int resultsize=100;
	
	private static Directory directory;
	private static SearcherManager searchermanager;
	static{
		try {
			directory=FSDirectory.open(new File(indexpath));
			searchermanager=new SearcherManager(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//相关性排序
	public static QueryResults search(boolean Iscopy,String queryString,int firstResult,int maxResult) throws Exception {
		
		List<Document> documents=new ArrayList<Document>();
		
		//IndexReader reader=IndexReader.open(directory);
		IndexSearcher indexsearcher=searchermanager.get();
		String[] fields={"title","author"};
		QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_30,fields,analyzer);
		String queryReplace=Tools.StringFilter(queryString);
		Query query=parser.parse(queryReplace);
		///高亮显示
		//System.out.println(query.toString());
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(300));
		Filter filter=null;

		TopDocs topDocs=null;
		int end=0;
		int num=0;
		if(Tools.KeyCount(queryReplace)<6)
		{
			topDocs= indexsearcher.search(query,filter,resultsize);
			num=resultsize;
		}
		else
		{
			topDocs= indexsearcher.search(query,filter,10);
			end=1;
			num=1;
		}
		if(topDocs.totalHits<=0)
			return null;
		if(end==1)
		{
			int docSn=topDocs.scoreDocs[0].doc;
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
//			int[] countss={0};
			return new QueryResults(1,topDocs.totalHits,documents);
		}
		int maxnum= Math.min(topDocs.totalHits, num);
		int count=0;
		String fieldName="";
		int titleCount=1;
		boolean Isend=false;
		int name=0;//标记是name
		int title=0;//标记是title
		int[] yearcount=new int[16];
//		yearcount={0};
		if(end!=1)
		{
			List<String> QueryToken=null;
			QueryToken=Tools.analyseZhQuery(queryReplace);
			int number=QueryToken.size();
			for(int i=0;i<maxnum&&!Isend;i++)
			{
				ScoreDoc scoredoc=topDocs.scoreDocs[i];
				int docSn=scoredoc.doc;
				Document docum=indexsearcher.doc(docSn);
				if(number>1)//如果搜索关键字数个数大于1
				{
					if(i<3)//前三项做适应性计算看是匹配作者还是标题
					{
						count=Tools.analyseCount(QueryToken, docum.get("title"));//获取当前匹配单词个数
						if(count>1)
						{
							if(count==number)
							{
								title++;
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
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							
						}
						else
						{
							count=Tools.analyseCount(QueryToken, docum.get("author"));//获取当前匹配单词个数
							if(count>1)
							{
								if(count==number)
								{
									name++;
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
									if(!Iscopy)
									{
										int year = Integer.parseInt(docum.get("year"));
										if(year>1997)
										{
											if(year-1998<16)
											{
												yearcount[year-1998]++;
											}
										}
									}
									documents.add(docum);
								}
							}
						}
						if(i==2)
						{
							if(name>title)
								fieldName="author";
							else
								fieldName="title";
						}
					}
					if(i>=3)
					{
						titleCount=Tools.KeyCount(docum.get(fieldName));//文献标题词数
						if(titleCount>=number)//论文标题词项数大于等于搜索词数时
						{
							count=Tools.analyseCount(QueryToken, docum.get(fieldName));//获取当前匹配单词个数
							if(count==number)
							{
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
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							else
							{
								if(count>number)//考虑到有的标题长度很短
								{
									Isend=true;//当匹配个数小于搜索词个数的时候结束
									break;
								}
							}
						}
					}
				}
				else	//当搜索长度为1时直接筛选出
				{
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
					if(!Iscopy)
					{
						int year = Integer.parseInt(docum.get("year"));
						if(year>1997)
						{
							if(year-1998<16)
							{
								yearcount[year-1998]++;
							}
						}
					}
					documents.add(docum);
				} 
			}
		}
		searchermanager.release(indexsearcher);
		List<Document> docList=new ArrayList<Document>();
		end = Math.min(firstResult+maxResult, documents.size());
		for(int i=firstResult;i<end;i++)
		{
			docList.add(documents.get(i));
		} 
		return new QueryResults(documents.size(),documents.size(),yearcount,docList);
	}
	
	//用户相关性排序
    public static QueryResults search(Connection conn,int userid,boolean Iscopy, String queryString,int firstResult,int maxResult,String field) throws Exception {

		List<Document> documents=new ArrayList<Document>();
		IndexSearcher indexsearcher=searchermanager.get();
		String[] fields={"title","author"};
		QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_30,fields,analyzer);
		String queryReplace=Tools.StringFilter(queryString);
		Query query=parser.parse(queryReplace);
		///高亮显示
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(300));
		Filter filter=null;
		TopDocs topDocs=null;
		int end=0;
		int num=0;
		if(Tools.KeyCount(queryReplace)<5)
		{
			topDocs= indexsearcher.search(query,filter,resultsize);
			num=resultsize;
		}
		else
		{
			topDocs= indexsearcher.search(query,filter,10);
			end=1;
			num=1;
		}
		if(topDocs.totalHits<=0)
			return null;
		if(end==1)
		{
			int docSn=topDocs.scoreDocs[0].doc;
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
			return new QueryResults(1,topDocs.totalHits,documents);
		
		}
		int maxnum= Math.min(topDocs.totalHits, num);
		int count=0;
		String fieldName="";
		int titleCount=1;
		boolean Isend=false;
		int name=0;//标记是name
		int title=0;//标记是title
		int[] yearcount=new int[16];
//		yearcount={0};
		if(end!=1)
		{
			List<String> QueryToken=null;
			QueryToken=Tools.analyseZhQuery(queryReplace);
			int number=QueryToken.size();
			for(int i=0;i<maxnum&&!Isend;i++)
			{
				ScoreDoc scoredoc=topDocs.scoreDocs[i];
				int docSn=scoredoc.doc;
				Document docum=indexsearcher.doc(docSn);
				if(number>1)//如果搜索关键字数个数大于1
				{
					if(i<3)//前三项做适应性计算看是匹配作者还是标题
					{
						count=Tools.analyseCount(QueryToken, docum.get("title"));//获取当前匹配单词个数
						if(count>1)
						{
							if(count==number)
							{
								title++;
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
								if(RelevanceDao.IsExit(conn, userid, Integer.parseInt(docum.get("id"))))
								{
									docum.getField("importindex").setValue(Float.toString(3.0f));//用来标示是否已经被点击
								}
								else
								{
									int intfield= Integer.parseInt(docum.getField("field").stringValue());
									if(intfield!=0&&Tools.Isinfield(intfield,field))
									{
										docum.getField("importindex").setValue(Float.toString(2.0f));//用来标示是否是笔者想关注的领域！！
									}
								}
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							
						}
						else
						{
							count=Tools.analyseCount(QueryToken, docum.get("author"));//获取当前匹配单词个数
							if(count>1)
							{
								if(count==number)
								{
									name++;
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
									if(!Iscopy)
									{
										int year = Integer.parseInt(docum.get("year"));
										if(year>1997)
										{
											if(year-1998<16)
											{
												yearcount[year-1998]++;
											}
										}
									}
									if(RelevanceDao.IsExit(conn, userid, Integer.parseInt(docum.get("id"))))
									{
										docum.getField("importindex").setValue(Float.toString(3.0f));//用来标示是否已经被点击
									}
									else
									{
										int intfield= Integer.parseInt(docum.getField("field").stringValue());
										if(intfield!=0&&Tools.Isinfield(intfield,field))
										{
											docum.getField("importindex").setValue(Float.toString(2.0f));//用来标示是否是笔者想关注的领域！！
										}
									}
									
									documents.add(docum);
								}
							}
						}
						if(i==2)
						{
							if(name>title)
								fieldName="author";
							else
								fieldName="title";
						}
					}
					if(i>=3)
					{
						titleCount=Tools.KeyCount(docum.get(fieldName));//文献标题词数
						if(titleCount>=number)//论文标题词项数大于等于搜索词数时
						{
							count=Tools.analyseCount(QueryToken, docum.get(fieldName));//获取当前匹配单词个数
							if(count==number)
							{
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
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								if(RelevanceDao.IsExit(conn, userid, Integer.parseInt(docum.get("id"))))
								{
									docum.getField("importindex").setValue(Float.toString(3.0f));//用来标示是否已经被点击
								}
								else
								{
									int intfield= Integer.parseInt(docum.getField("field").stringValue());
									if(intfield!=0&&Tools.Isinfield(intfield,field))
									{
										docum.getField("importindex").setValue(Float.toString(2.0f));//用来标示是否是笔者想关注的领域！！
									}
								}
								documents.add(docum);
							}
							else
							{
								if(count>number)//考虑到有的标题长度很短
								{
									Isend=true;//当匹配个数小于搜索词个数的时候结束
									break;
								}
							}
						}
					}
				}
				else	//当搜索长度为1时直接筛选出
				{
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
					if(!Iscopy)
					{
						int year = Integer.parseInt(docum.get("year"));
						if(year>1997)
						{
							if(year-1998<16)
							{
								yearcount[year-1998]++;
							}
						}
					}
					if(RelevanceDao.IsExit(conn, userid, Integer.parseInt(docum.get("id"))))
					{
						docum.getField("importindex").setValue(Float.toString(3.0f));//用来标示是否已经被点击
					}
					else
					{
						int intfield= Integer.parseInt(docum.getField("field").stringValue());
						if(intfield!=0&&Tools.Isinfield(intfield,field))
						{
							docum.getField("importindex").setValue(Float.toString(2.0f));//用来标示是否是笔者想关注的领域！！
						}
					}
					documents.add(docum);
				} 
			}
		}
		searchermanager.release(indexsearcher);
		UserRelComparator userRelComparator=new UserRelComparator();
		Collections.sort(documents,userRelComparator);
		List<Document> docList=new ArrayList<Document>();
		end = Math.min(firstResult+maxResult, documents.size());
		for(int i=firstResult;i<end;i++)
		{
			docList.add(documents.get(i));
		} 
		return new QueryResults(documents.size(),documents.size(),yearcount,docList);
    }
	
    //时间排序
	public static QueryResults searchByTime(boolean Iscopy,Connection conn, String queryString,int firstResult,int maxResult) throws Exception {
		
		List<Document> documents=new ArrayList<Document>();
		IndexSearcher indexsearcher=searchermanager.get();
		String[] fields={"title","author"};
		QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_30,fields,analyzer);
		String queryReplace=Tools.StringFilter(queryString);
		Query query=parser.parse(queryReplace);
		///高亮显示
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(300));
		Filter filter=null;
		//Sort sort=new Sort(new SortField("importindex",SortField.FLOAT));
		//sort.setSort(new SortField("importindex",FieldCache.DEFAULT_FLOAT_PARSER,true));
		TopDocs topDocs=null;
		int end=0;
		int num=0;
		if(Tools.KeyCount(queryReplace)<5)
		{
			topDocs= indexsearcher.search(query,filter,resultsize);
			num=resultsize;
		}
		else
		{
			topDocs= indexsearcher.search(query,filter,10);
			end=1;
			num=1;
		}
		if(topDocs.totalHits<=0)
			return null;
		if(end==1)
		{
			int docSn=topDocs.scoreDocs[0].doc;
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
			return new QueryResults(1,topDocs.totalHits,documents);
		}
		int maxnum= Math.min(topDocs.totalHits, num);
		int count=0;
		String fieldName="";
		int titleCount=1;
		int title=0;
		int name=0;
		int[] yearcount=new int[16];
		boolean Isend=false;
		if(end!=1)
		{
			List<String> QueryToken=null;
			QueryToken=Tools.analyseZhQuery(queryReplace);
			int number=QueryToken.size();
			for(int i=0;i<maxnum&&!Isend;i++)
			{
				ScoreDoc scoredoc=topDocs.scoreDocs[i];
				int docSn=scoredoc.doc;
				Document docum=indexsearcher.doc(docSn);
				if(number>1)
				{
					if(i<3)
					{
						count=Tools.analyseCount(QueryToken, docum.get("title"));//获取当前匹配单词个数
						if(count>1)
						{
							if(count==number)
							{
								title++;
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
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							
						}
						else
						{
							count=Tools.analyseCount(QueryToken, docum.get("author"));//获取当前匹配单词个数
							if(count>1)
							{
								if(count==number)
								{
									name++;
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
									if(!Iscopy)
									{
										int year = Integer.parseInt(docum.get("year"));
										if(year>1997)
										{
											if(year-1998<16)
											{
												yearcount[year-1998]++;
											}
										}
									}
									documents.add(docum);
								}
							}
						}
						if(i==2)
						{
							if(name>title)
								fieldName="author";
							else
								fieldName="title";
						}
					}
					if(i>=3)
					{
						titleCount=Tools.KeyCount(docum.get(fieldName));//文献标题词数
						if(titleCount>=number)//论文标题词项数大于等于搜索词数时
						{
							count=Tools.analyseCount(QueryToken, docum.get(fieldName));//获取当前匹配单词个数
							if(count==number)
							{
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
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							else
							{
								if(count>number)//考虑到有的标题长度很短
								{
									Isend=true;//当匹配个数小于搜索词个数的时候结束
									break;
								}
							}
						}
					}
				}
				else	//当搜索长度为1时直接筛选出
				{
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
					if(!Iscopy)
					{
						int year = Integer.parseInt(docum.get("year"));
						if(year>1997)
						{
							if(year-1998<16)
							{
								yearcount[year-1998]++;
							}
						}
					}
					documents.add(docum);
				}
			}
		}
		searchermanager.release(indexsearcher);
		TimeComparator timeComparator=new TimeComparator();
		Collections.sort(documents,timeComparator);
		end = Math.min(firstResult+maxResult, documents.size());
		List<Document> docList=new ArrayList<Document>();
		for(int i=firstResult;i<end;i++)
		{
			docList.add(documents.get(i));
		}
		return new QueryResults(documents.size(),documents.size(),yearcount,docList);
	}
    
	//重要性排序
	public static QueryResults searchByImporIndex(boolean Iscopy,Connection conn, String queryString,int firstResult,int maxResult) throws Exception {

		List<Document> documents=new ArrayList<Document>();
		IndexSearcher indexsearcher=searchermanager.get();	
		String[] fields={"title","author"};//
		QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_30,fields,analyzer);
		String queryReplace=Tools.StringFilter(queryString);
		Query query=parser.parse(queryReplace);
		///高亮显示
		//System.out.println(query);
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(300));
		Filter filter=null;
		//Sort sort=new Sort(new SortField("importindex",SortField.FLOAT));
		//sort.setSort(new SortField("importindex",FieldCache.DEFAULT_FLOAT_PARSER,true));
		TopDocs topDocs=null;
		int end=0;
		int num=0;
		if(Tools.KeyCount(queryReplace)<6)
		{
			topDocs= indexsearcher.search(query,filter,resultsize);
			//end =Math.min(firstResult+maxResult, topDocs.totalHits);
			num=resultsize;
		}
		else
		{
			topDocs= indexsearcher.search(query,filter,10);
			end=1;
			num=1;
		}
		if(topDocs.totalHits<=0)
			return null;
		if(end==1)
		{
			int docSn=topDocs.scoreDocs[0].doc;
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
			return new QueryResults(1,topDocs.totalHits,documents);
		}
		int maxnum= Math.min(topDocs.totalHits, num);
		int count=0;
		String fieldName="";
		int titleCount=1;
		boolean Isend=false;
		int name=0;//标记是name
		int title=0;//标记是title
		int[] yearcount=new int[16];
		if(end!=1)
		{
			List<String> QueryToken=null;
			QueryToken=Tools.analyseZhQuery(queryReplace);
			int number=QueryToken.size();
			for(int i=0;i<maxnum&&!Isend;i++)
			{
				ScoreDoc scoredoc=topDocs.scoreDocs[i];
				int docSn=scoredoc.doc;
				Document docum=indexsearcher.doc(docSn);
				if(number>1)
				{
					if(i<3)
					{
						count=Tools.analyseCount(QueryToken, docum.get("title"));//获取当前匹配单词个数
						if(count>1)
						{
							if(count==number)
							{
								title++;
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
								String time=docum.getField("year").stringValue();
								String id=docum.getField("id").stringValue();
								docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							
						}
						else
						{
							count=Tools.analyseCount(QueryToken, docum.get("author"));//获取当前匹配单词个数
							if(count>1)
							{
								if(count==number)
								{
									name++;
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
									String time=docum.getField("year").stringValue();
									String id=docum.getField("id").stringValue();
									docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
									if(!Iscopy)
									{
										int year = Integer.parseInt(docum.get("year"));
										if(year>1997)
										{
											if(year-1998<16)
											{
												yearcount[year-1998]++;
											}
										}
									}
									documents.add(docum);
								}
							}
						}
						if(i==2)
						{
							if(name>title)
								fieldName="author";
							else
								fieldName="title";
						}
					}
					if(i>=3)
					{
						titleCount=Tools.KeyCount(docum.get(fieldName));//文献标题词数
						if(titleCount>=number)//论文标题词项数大于等于搜索词数时
						{
							count=Tools.analyseCount(QueryToken, docum.get(fieldName));//获取当前匹配单词个数
							if(count==number)
							{
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
								String time=docum.getField("year").stringValue();
								String id=docum.getField("id").stringValue();
								docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							else
							{
								if(count>number)//考虑到有的标题长度很短
								{
									Isend=true;//当匹配个数小于搜索词个数的时候结束
									break;
								}
							}
						}
					}
				}
				else	//当搜索长度为1时直接筛选出
				{
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
					String time=docum.getField("year").stringValue();
					String id=docum.getField("id").stringValue();
					docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
					if(!Iscopy)
					{
						int year = Integer.parseInt(docum.get("year"));
						if(year>1997)
						{
							if(year-1998<16)
							{
								yearcount[year-1998]++;
							}
						}
					}
					documents.add(docum);
				}
			}
		}
		searchermanager.release(indexsearcher);
		ImportantComparator importComparator=new ImportantComparator();
		Collections.sort(documents,importComparator);//修改比较类
		List<Document> docList=new ArrayList<Document>();
		end = Math.min(firstResult+maxResult, documents.size());
		for(int i=firstResult;i<end;i++)
		{
			docList.add(documents.get(i));
		}
		return new QueryResults(documents.size(),documents.size(),yearcount,docList);
	}
	
	public static QueryResults searchByImporIndex1(boolean Iscopy,Connection conn, String queryString,int firstResult,int maxResult) throws Exception {

		List<Document> documents=new ArrayList<Document>();
		IndexSearcher indexsearcher=searchermanager.get();
		String[] fields={"title","author"};
		QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_30,fields,analyzer);
		String queryReplace=Tools.StringFilter(queryString);
		Query query=parser.parse(queryReplace);
		///高亮显示
		//System.out.println(query);
	/*	Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(300));*/
		Filter filter=null;
		//Sort sort=new Sort(new SortField("importindex",SortField.FLOAT));
		//sort.setSort(new SortField("importindex",FieldCache.DEFAULT_FLOAT_PARSER,true));
		TopDocs topDocs=null;
		int end=0;
		int num=0;
		if(Tools.KeyCount(queryReplace)<6)
		{
			topDocs= indexsearcher.search(query,filter,resultsize);
			//end =Math.min(firstResult+maxResult, topDocs.totalHits);
			num=resultsize;
		}
		else
		{
			topDocs= indexsearcher.search(query,filter,10);
			end=1;
			num=1;
		}
		if(topDocs.totalHits<=0)
			return null;
		if(end==1)
		{
			int docSn=topDocs.scoreDocs[0].doc;
			Document docum=indexsearcher.doc(docSn);
		/*	String hcString= highlighter.getBestFragment(analyzer, "title",docum.get("title"));
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
			}*/
			documents.add(docum);
			return new QueryResults(1,topDocs.totalHits,documents);
		}
		int maxnum= Math.min(topDocs.totalHits, num);
		int count=0;
		String fieldName="";
		int titleCount=1;
		boolean Isend=false;
		int name=0;//标记是name
		int title=0;//标记是title
		int[] yearcount=new int[16];
		if(end!=1)
		{
			List<String> QueryToken=null;
			QueryToken=Tools.analyseZhQuery(queryReplace);
			int number=QueryToken.size();
			for(int i=0;i<maxnum&&!Isend;i++)
			{
				ScoreDoc scoredoc=topDocs.scoreDocs[i];
				int docSn=scoredoc.doc;
				Document docum=indexsearcher.doc(docSn);
				if(number>1)
				{
					if(i<3)
					{
						count=Tools.analyseCount(QueryToken, docum.get("title"));//获取当前匹配单词个数
						if(count>1)
						{
							if(count==number)
							{
								title++;
							/*	String hcString= highlighter.getBestFragment(analyzer, "title",docum.get("title"));
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
								}*/
								String time=docum.getField("year").stringValue();
								String id=docum.getField("id").stringValue();
								docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							
						}
						else
						{
							count=Tools.analyseCount(QueryToken, docum.get("author"));//获取当前匹配单词个数
							if(count>1)
							{
								if(count==number)
								{
									name++;
								/*	String hcString= highlighter.getBestFragment(analyzer, "title",docum.get("title"));
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
									}*/
									String time=docum.getField("year").stringValue();
									String id=docum.getField("id").stringValue();
									docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
									if(!Iscopy)
									{
										int year = Integer.parseInt(docum.get("year"));
										if(year>1997)
										{
											if(year-1998<16)
											{
												yearcount[year-1998]++;
											}
										}
									}
									documents.add(docum);
								}
							}
						}
						if(i==2)
						{
							if(name>title)
								fieldName="author";
							else
								fieldName="title";
						}
					}
					if(i>=3)
					{
						titleCount=Tools.KeyCount(docum.get(fieldName));//文献标题词数
						if(titleCount>=number)//论文标题词项数大于等于搜索词数时
						{
							count=Tools.analyseCount(QueryToken, docum.get(fieldName));//获取当前匹配单词个数
							if(count==number)
							{
							/*	String hcString= highlighter.getBestFragment(analyzer, "title",docum.get("title"));
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
								}*/
								String time=docum.getField("year").stringValue();
								String id=docum.getField("id").stringValue();
								docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
								if(!Iscopy)
								{
									int year = Integer.parseInt(docum.get("year"));
									if(year>1997)
									{
										if(year-1998<16)
										{
											yearcount[year-1998]++;
										}
									}
								}
								documents.add(docum);
							}
							else
							{
								if(count>number)//考虑到有的标题长度很短
								{
									Isend=true;//当匹配个数小于搜索词个数的时候结束
									break;
								}
							}
						}
					}
				}
				else	//当搜索长度为1时直接筛选出
				{
					/*String hcString= highlighter.getBestFragment(analyzer, "title",docum.get("title"));
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
					}*/
					String time=docum.getField("year").stringValue();
					String id=docum.getField("id").stringValue();
					docum.getField("importindex").setValue(Float.toString(Tools.calculate(conn,id,time)));
					if(!Iscopy)
					{
						int year = Integer.parseInt(docum.get("year"));
						if(year>1997)
						{
							if(year-1998<16)
							{
								yearcount[year-1998]++;
							}
						}
					}
					documents.add(docum);
				}
			}
		}
		searchermanager.release(indexsearcher);
		ImportantComparator importComparator=new ImportantComparator();
		Collections.sort(documents,importComparator);//修改比较类
		List<Document> docList=new ArrayList<Document>();
		end = Math.min(firstResult+maxResult, documents.size());
		for(int i=firstResult;i<end;i++)
		{
			docList.add(documents.get(i));
		}
		return new QueryResults(documents.size(),documents.size(),yearcount,docList);
	}
	
	//时间范围
	public static QueryResults searchByRange(String queryString,String start,String endtime,int firstResult,int maxResult) throws Exception {
		
		IndexSearcher indexsearcher=searchermanager.get();
		List<Document> documents=new ArrayList<Document>();
		String[] fields={"title","author"};
		QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_30,fields,analyzer);
		String queryReplace=Tools.StringFilter(queryString);
		Query query=parser.parse(queryReplace);
		///高亮显示
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer scorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, scorer);
		highlighter.setTextFragmenter(new SimpleFragmenter(300));
		Filter filter = new TermRangeFilter("year",start,endtime, true, true);
		TopDocs topDocs= indexsearcher.search(query,filter,resultsize);
		if(topDocs.totalHits<=0)
			return null;
		List<String> QueryToken=null;
		int maxnum= Math.min(topDocs.totalHits, resultsize);
		int count=0;
		String fieldName="";
		int titleCount=1;
		boolean Isend=false;
		int name=0;//标记是name
		int title=0;//标记是title
		int[] yearcount=new int[16];
		QueryToken=Tools.analyseZhQuery(queryReplace);
		int number=QueryToken.size();
		for(int i=0;i<maxnum&&!Isend;i++)
		{
			ScoreDoc scoredoc=topDocs.scoreDocs[i];
			int docSn=scoredoc.doc;
			Document docum=indexsearcher.doc(docSn);
			if(number>1)
			{
				if(i<3)
				{
					count=Tools.analyseCount(QueryToken, docum.get("title"));//获取当前匹配单词个数
					if(count>1)
					{
						if(count==number)
						{
							title++;
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
					}
					else
					{
						count=Tools.analyseCount(QueryToken, docum.get("author"));//获取当前匹配单词个数
						if(count>1)
						{
							if(count==number)
							{
								name++;
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
								int year = Integer.parseInt(docum.get("year"));
								if(year>1997)
								{
									if(year-1998<16)
									{
										yearcount[year-1998]++;
									}
								}
								documents.add(docum);
							}
						}
					}
					if(i==2)
					{
						if(name>title)
							fieldName="author";
						else
							fieldName="title";
					}
				}
				if(i>=3)
				{
					titleCount=Tools.KeyCount(docum.get(fieldName));//文献标题词数
					if(titleCount>=number)//论文标题词项数大于等于搜索词数时
					{
						count=Tools.analyseCount(QueryToken, docum.get(fieldName));//获取当前匹配单词个数
						if(count==number)
						{
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
							int year = Integer.parseInt(docum.get("year"));
							if(year>1997)
							{
								if(year-1998<16)
								{
									yearcount[year-1998]++;
								}
							}
							documents.add(docum);
						}
						else
						{
							if(count>number)//考虑到有的标题长度很短
							{
								Isend=true;//当匹配个数小于搜索词个数的时候结束
								break;
							}
						}
					}
				}
			}
			else	//当搜索长度为1时直接筛选出
			{
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
				int year = Integer.parseInt(docum.get("year"));
				if(year>1997)
				{
					if(year-1998<16)
					{
						yearcount[year-1998]++;
					}
				}
				documents.add(docum);
			}
		}
		searchermanager.release(indexsearcher);
		List<Document> docList=new ArrayList<Document>();
		int end = Math.min(firstResult+maxResult, documents.size());
		for(int i=firstResult;i<end;i++)
		{
			docList.add(documents.get(i));
		}
		return new QueryResults(documents.size(),topDocs.totalHits,documents);
	}
	
}
		
	