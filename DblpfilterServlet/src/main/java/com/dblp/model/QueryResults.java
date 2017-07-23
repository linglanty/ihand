package com.dblp.model;

import java.util.List;

import org.apache.lucene.document.Document;

public class QueryResults {
	
	private int recordCount;
	private List<Document> recordList;
	
	private int maxindex;
	
	private int[] count;

	public QueryResults(int maxindex,int recordCount,int[] count, List<Document> recordList) {
		super();
		this.recordCount = recordCount;
		this.recordList = recordList;
		this.maxindex=maxindex;
		this.count=count;
	}

	public QueryResults(int maxindex,int recordCount, List<Document> recordList) {
		super();
		this.recordCount = recordCount;
		this.recordList = recordList;
		this.maxindex=maxindex;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public List<Document> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<Document> recordList) {
		this.recordList = recordList;
	}

	public int getMaxindex() {
		return maxindex;
	}

	public void setMaxindex(int maxindex) {
		this.maxindex = maxindex;
	}

	public int[] getCount() {
		return count;
	}

	public void setCount(int[] count) {
		this.count = count;
	}
	
}
