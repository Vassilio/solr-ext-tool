package org.june.solr.config.data;

import java.util.HashMap;
import java.util.Map;

public class Entity extends Table {
	
	private static final String queryTemplate = "select t.*,'%s' tablename,'%s' groupname from %s t";
	private static final String deltaQueryTemplate = "select %s from %s where %s>'${dataimporter.last_index_time}'";
	private static final String deltaImportQueryTemplate = "select t.*,'%s' tablename,'%s' groupname from %s t where t.%s='${dataimporter.delta.%s}'";
	private static final String timeAlias = "creattime_inc";//时间字段别名
	private static final String timeCname = "创建时间";//时间字段中文名
	private static final String pkAlias = "id";//主键字段别名
	private static final String pkCname = "主键";//主键中文名
	private static final String aliasSuffix = "_t";
	
	private String name;
	private String deltaImportQuery;
	private String deltaQuery;
	private String query;

	public void init() throws Exception{
		//验证数据
		if(this.getPkField()==null){
			throw new Exception("pkField is null");
		}
		if(this.getTablecname()==null){
			throw new Exception("Tablecname is null");
		}
		if(this.getDataSource()==null){
			throw new Exception("DataSource is null");
		}
		if(this.getColumns()==null || this.getColumns().isEmpty()){
			throw new Exception("Columns is null or empty");
		}
		this.setName(this.getTablename());		
		String query = String.format(queryTemplate, this.getTablename(), this.getGroupname(), this.getTablename());
		this.setQuery(query);		
		String deltaQuery = String.format(deltaQueryTemplate, this.getPkField(), this.getTablename(),
				this.getUpdateField());
		this.setDeltaQuery(deltaQuery);		
		String deltaImportQuery = String.format(deltaImportQueryTemplate, this.getTablename(), this.getGroupname(),this.getTablename(),
				this.getPkField(), this.getPkField());
		this.setDeltaImportQuery(deltaImportQuery);	
		Map<String,Column> columnMap = new HashMap<String,Column>();
		for(Column column:this.getColumns()){
			if(column.getAlias()==null || column.getAlias().isEmpty()){
				column.setAlias(column.getName()+aliasSuffix);
			}
			columnMap.put(column.getName(), column);
		}
		//getColumns中主键不存在则缺省增加主键
		Column pkColumn = columnMap.get(this.getPkField());
		if(pkColumn == null){
			pkColumn = new Column();			
			pkColumn.setCname(pkCname);
			pkColumn.setName(this.getPkField());
			this.getColumns().add(pkColumn);
		}
		pkColumn.setAlias(pkAlias);
		//getColumns如有timeField字段，将其别名修改为creattime_inc
		if(this.getTimeField()==null) return;
		Column timeColumn = columnMap.get(this.getTimeField());
		if(timeColumn == null){
			timeColumn = new Column();			
			timeColumn.setCname(timeCname);
			timeColumn.setName(this.getTimeField());
			this.getColumns().add(timeColumn);
		}
		timeColumn.setAlias(timeAlias);
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeltaImportQuery() {
		return deltaImportQuery;
	}

	public void setDeltaImportQuery(String deltaImportQuery) {
		this.deltaImportQuery = deltaImportQuery;
	}

	public String getDeltaQuery() {
		return deltaQuery;
	}

	public void setDeltaQuery(String deltaQuery) {
		this.deltaQuery = deltaQuery;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
