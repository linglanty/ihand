package cn.com.dj.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.dj.dto.RealTimeData;
import cn.com.dj.dto.RealTimeVariable;


public class RealTimeData2Map {

	public static Map<String,RealTimeVariable> convertObj2Map(RealTimeData realTimeData)
	{
		List<RealTimeVariable> variables=realTimeData.getVars();
		Map<String,RealTimeVariable> map=new HashMap<String, RealTimeVariable>();
		for(RealTimeVariable variable:variables)
		{
			map.put(variable.getId(), variable);
		}
		return map;
	}
}
