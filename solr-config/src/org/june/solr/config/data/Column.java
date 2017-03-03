package org.june.solr.config.data;
/**
 * 列信息
 * @author lwp
 *
 */
public class Column {
	//列名
	private String name;
	//中文名
	private String cname;
	//别名
	private String alias;
	//数据类型
	private String dataType;
	//是否索引
	private boolean index = true;
	//是否存储
	private boolean store = true;
	//是否超链接连接
	private boolean href = false;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public boolean isIndex() {
		return index;
	}
	public void setIndex(boolean index) {
		this.index = index;
	}
	public boolean isStore() {
		return store;
	}
	public void setStore(boolean store) {
		this.store = store;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public boolean isHref() {
		return href;
	}
	public void setHref(boolean href) {
		this.href = href;
	}
}
