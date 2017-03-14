package org.june.solr.config.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity extends Table {
	
	private static final String queryTemplate = "select t.*,'%s' tablename,'%s' groupname from %s t";
	private static final String queryTemplateOfChild = "select * from %s where %s='${%s.%s}'"; //子entity查询语句
	private static final String deltaQueryTemplate = "select %s from %s where %s>'${dataimporter.last_index_time}'";
	private static final String deltaImportQueryTemplate = "select t.*,'%s' tablename,'%s' groupname from %s t where t.%s='${dataimporter.delta.%s}'";
	private static final String timeAlias = "creattime_inc";//时间字段别名
	private static final String timeCname = "创建时间";//时间字段中文名
	private static final String pkAlias = "id";//主键字段别名
	private static final String pkCname = "主键";//主键中文名
	private static final String aliasSuffix = "_t";//单值拷贝中文
	private static final String aliasSuffixMulti = "_txt";//多值拷贝中文
	
	private String name;
	private String deltaImportQuery;
	private String deltaQuery;
	private String query;
	private List<Entity> children = new ArrayList<Entity>();//子实体
	private Entity parent = null;//父实体
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
		Map<String,Column> columnMap = new HashMap<String,Column>();
		for(Column column:this.getColumns()){
			if(column.getAlias()==null || column.getAlias().isEmpty()){
				//子实体field均为多值
				if(this.parent!=null){
					column.setAlias(column.getName()+aliasSuffixMulti);
				}else{
					column.setAlias(column.getName()+aliasSuffix);
				}				
			}
			columnMap.put(column.getName(), column);
		}
		//子实体查询仅包含全量查询语句
		if(this.parent!=null){
			String query = String.format(queryTemplateOfChild, this.getTablename(), this.getForeignKey(), this.parent.getName(),this.parent.getPkField());
			this.setQuery(query);
		}else{
			String query = String.format(queryTemplate, this.getTablename(), this.getGroupname(), this.getTablename());
			this.setQuery(query);		
			String deltaQuery = String.format(deltaQueryTemplate, this.getPkField(), this.getTablename(),
					this.getUpdateField());
			this.setDeltaQuery(deltaQuery);		
			String deltaImportQuery = String.format(deltaImportQueryTemplate, this.getTablename(), this.getGroupname(),this.getTablename(),
					this.getPkField(), this.getPkField());
			this.setDeltaImportQuery(deltaImportQuery);	
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
		//递归初始化
		for(Entity child:children){
			child.init();
		}
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
	public List<Entity> getChildren() {
		return children;
	}
	public void setChildren(List<Entity> children) {
		this.children = children;
	}
	public Entity getParent() {
		return parent;
	}
	public void setParent(Entity parent) {
		this.parent = parent;
	}
}
