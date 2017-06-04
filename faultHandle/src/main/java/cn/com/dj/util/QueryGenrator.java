package cn.com.dj.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import cn.com.dj.dto.ModelQueryBean;

@Component
public class QueryGenrator {

	public void withSortDESC(Query query, String field) {
		query.with(new Sort(Sort.Direction.DESC, new String[] { field }));
	}

	public Query getQuery(Query query, ModelQueryBean mqb) {
		/*addResourceIds(query, mqb.getResourceIds());
		addName(query, mqb.getName());
		addExist(query);
		addTime(query, mqb.getStartTime(), mqb.getEndTime());
		addGatway(query, mqb.getGateway());*/
		return query;
	}
}