package cn.com.dj.util;

import java.util.List;
import java.util.Map;

import cn.com.dj.dto.FaultParameter;
import cn.com.dj.dto.FaultReason;
import cn.com.dj.dto.FaultVar;
import cn.com.dj.dto.Limit;
import cn.com.dj.dto.RealTimeVariable;

public class LogicJudg {
	
	//find the reason of the fault
	public static FaultReason findReason(List<FaultReason> faultReasons,Map<String,RealTimeVariable> realtimeVariables)
	{
		for(FaultReason reason:faultReasons)
		{
			List<FaultParameter> vars=reason.getVars();
			if(vars == null||vars.size() < 1) continue;
			if (isFault(vars, realtimeVariables)) {
				return reason;
			}
		}
		return null;
	}
	
	//and all the parameters satisfied
	public static boolean isFault( List<FaultParameter> parameters,Map<String,RealTimeVariable> realtimeVariables)
	{
		for(FaultParameter parameter:parameters)
		{
			//对于每个参数都满足的时候可以确定是这种错误
			if (!hasProblemByGroup(parameter,realtimeVariables)) {
				return false;
			}
		}
		return true;
	}
	// or :one of the parameters satisfied
	private static boolean hasProblemByGroup(FaultParameter parameter,Map<String,RealTimeVariable> realtimeVariables)
	{
		List<FaultVar> vars=parameter.getVars();
		for(FaultVar var:vars)
		{
			if (realtimeVariables.containsKey(var.get_id())) {
				RealTimeVariable realTimeVariable=realtimeVariables.get(var.get_id());
				if(hasProblem(var.getLimit(), realTimeVariable))
					return true;
			}
		}
		return false;
	}
	/*
	 * 故障规则设置：
		1，故障规则可设置 上下界边界是否包含异常情况
		3, 当是一个固定值时，上下界相等时 如果相等则返回是，否则返回否
		2，当实时数据在故障设置的范围内是发出故障提醒
	 * */
	private static boolean hasProblem(Limit limit,RealTimeVariable variable)
	{
		float value=(Float)variable.getValue();
		if(Float.compare(limit.getMinValue(), limit.getMaxValue())==0)//if the range is a value then we should compare the value with that value not the range
			return Float.compare(limit.getMinValue(), value)==0;
		if (limit.getMinAlert()&&Float.compare(value,limit.getMinValue())<0) {//value<limit.getMinValue()
			return false;
		}
		if (limit.getMaxAlert()&&Float.compare(value,limit.getMaxValue())>0) {
			return false;
		}
		
		
		return true;
	}
}
