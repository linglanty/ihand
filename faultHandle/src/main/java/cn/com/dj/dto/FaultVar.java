package cn.com.dj.dto;


/**
 * 
 * @author Jiang Du id,name,Limit
 *
 */
public class FaultVar {

	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Limit getLimit() {
		return limit;
	}
	public void setLimit(Limit limit) {
		this.limit = limit;
	}
	private String _id;
	private String name;
	private Limit limit;
	
}
