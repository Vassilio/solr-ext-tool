package org.june.solr.config.dih.data;

import java.util.ArrayList;
import java.util.List;
/**
 * 表信息
 * @author lwp
 *
 */
public class Table {
	//数据源
	private DataSource dataSource;
	//表名
	private String tablename;
	//表中文名
	private String tablecname;
	//主键
	private String pkField;
	//增量字段
	private String updateField;
	//分组名称
	private String groupname;
	//时间字段
	private String timeField;
	//外键
	private String foreignKey;
	//列
	List<Column> columns = new ArrayList<Column>();
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getTablecname() {
		return tablecname;
	}
	public void setTablecname(String tablecname) {
		this.tablecname = tablecname;
	}
	public String getPkField() {
		return pkField;
	}
	public void setPkField(String pkField) {
		this.pkField = pkField;
	}
	public String getUpdateField() {
		return updateField;
	}
	public void setUpdateField(String updateField) {
		this.updateField = updateField;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public String getTimeField() {
		return timeField;
	}
	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}
	public String getForeignKey() {
		return foreignKey;
	}
	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
}
