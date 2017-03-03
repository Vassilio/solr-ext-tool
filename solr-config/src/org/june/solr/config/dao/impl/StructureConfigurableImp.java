package org.june.solr.config.dao.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.june.solr.config.dao.StructureConfigurable;
import org.june.solr.config.data.*;
/**
 * db-data-config.xml配置管理工具
 * @author lwp
 *
 */
public class StructureConfigurableImp extends ConfigurableImpl implements StructureConfigurable {
	
	private final static String DIH_CONFIG = "db-data-config.xml";//DIH配置文件名称	
	private final static String DIH_CONFIG_DOCUMENT = "document";//配置文件内部document标签
	private final static String DIH_CONFIG_ENTITY = "entity";//配置文件内部entity标签
	private final static String DIH_CONFIG_ENTITY_DATASOURCE = "dataSource";//数据源
	private final static String DIH_CONFIG_ENTITY_QUERY = "query";//实体全量查询
	private final static String DIH_CONFIG_ENTITY_DELTAIMPORTQUERY = "deltaImportQuery";//实体增量导入
	private final static String DIH_CONFIG_ENTITY_DELTAQUERY = "deltaQuery";//实体增量查询
	private final static String DIH_CONFIG_ENTITY_TABLECNAME = "tablecname";//信息源分类
	private final static String DIH_CONFIG_ENTITY_PK = "pk";//主键
	private final static String DIH_CONFIG_ENTITY_UPDATEFIELD = "updateField";//增量查询字段
	private final static String DIH_CONFIG_ENTITY_TIMEFIELD = "timeField";//时间字段
	private final static String DIH_CONFIG_ENTITY_GROUPNAME = "groupName";//分组名称
	private final static String DIH_CONFIG_ENTITY_FIELD_COLUMN = "column";//列
	private final static String DIH_CONFIG_NAME = "name";//名称
	private final static String DIH_CONFIG_ENTITY_FIELD_CNAME = "cname";//中文名
	private final static String DIH_CONFIG_ENTITY_FIELD = "field";//字段标签
	private final static String DIH_CONFIG_DATASOURCE_DRIVER = "driver";//数据库驱动
	private final static String DIH_CONFIG_DATASOURCE_PASSWORD = "password";//密码
	private final static String DIH_CONFIG_DATASOURCE_TYPE = "type";//驱动类型
	private final static String DIH_CONFIG_DATASOURCE_URL = "url";//数据库地址
	private final static String DIH_CONFIG_DATASOURCE_USER = "user";//数据库用户
	/**
	 * 构造方法(配置文件路径默认为/configs/myconf)
	 * @param zkServer zookeeper服务地址
	 * @throws IOException
	 */
	public StructureConfigurableImp(String zkServer) throws IOException {
		super(zkServer);
	}
	/**
	 * 构造方法
	 * @param zkServer zookeeper服务地址
	 * @param basePath 配置文件路径（以/开始，结尾不带/）
	 * @throws IOException
	 */
	public StructureConfigurableImp(String zkServer,String basePath) throws IOException {
		super(zkServer,basePath);
	}	
	@Override
	public Document saveOrUpdateEntity(Entity entity) throws DocumentException, IOException {
		String xmlStr = this.getConfig(DIH_CONFIG);
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		Element documentEle = root.element(DIH_CONFIG_DOCUMENT);
		Element entitynow = this.entityExist(document, entity);
		// 重名则先移除后增加
		if (entitynow != null) {
			documentEle.remove(entitynow);
		}
		// 不存在则增加ds
		if (this.dataSourceExist(document, entity.getDataSource()) == null) {
			document = this.saveOrUpdateDataSource(entity.getDataSource());
		}

		Element entityEle = documentEle.addElement(DIH_CONFIG_ENTITY);
		entityEle.addAttribute(DIH_CONFIG_ENTITY_DATASOURCE, entity.getDataSource().getName());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_QUERY, entity.getQuery());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_DELTAIMPORTQUERY, entity.getDeltaImportQuery());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_DELTAQUERY, entity.getDeltaQuery());
		entityEle.addAttribute(DIH_CONFIG_NAME, entity.getName());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_TABLECNAME, entity.getTablecname());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_PK, entity.getPkField());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_UPDATEFIELD, entity.getUpdateField());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_TIMEFIELD, entity.getTimeField());
		entityEle.addAttribute(DIH_CONFIG_ENTITY_GROUPNAME, entity.getGroupname());
		for (Column column : entity.getColumns()) {
			Element field = entityEle.addElement(DIH_CONFIG_ENTITY_FIELD);
			field.addAttribute(DIH_CONFIG_ENTITY_FIELD_COLUMN, column.getName());
			field.addAttribute(DIH_CONFIG_NAME, column.getAlias());
			field.addAttribute(DIH_CONFIG_ENTITY_FIELD_CNAME, column.getCname());
		}
		this.setConfig(document);
		return document;
	}

	@Override
	public List<Entity> getEntitys() throws DocumentException {
		return this.getEntitys(null);
	}

	@Override
	public Document deleteEntity(Entity entity) throws DocumentException, IOException {
		List<Entity> result = new ArrayList();
		String xmlStr = this.getConfig(DIH_CONFIG);
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		Element documentEle = root.element(DIH_CONFIG_DOCUMENT);
		List<Element> entitys = documentEle.elements(DIH_CONFIG_ENTITY);
		for (Element entityEle : entitys) {
			if (entityEle.attributeValue(DIH_CONFIG_NAME).equals(entity.getName())) {
				documentEle.remove(entityEle);
				break;
			}
		}
		this.setConfig(document);
		return document;
	}

	@Override
	public List<DataSource> getDataSources() throws DocumentException {
		return getDataSources(null);
	}

	public List<DataSource> getDataSources(String name) throws DocumentException {
		List<DataSource> result = new ArrayList();
		String xmlStr = this.getConfig(DIH_CONFIG);
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		List<Element> dsList = (List<Element>) root.elements(DIH_CONFIG_ENTITY_DATASOURCE);
		for (Element ds : dsList) {
			DataSource dataSource = element2DataSource(ds);
			if (name != null) {
				String attrName = dataSource.getName();
				if (attrName.equals(name)) {
					result.add(dataSource);
					break;
				}
			} else {
				result.add(dataSource);
			}
		}
		return result;
	}

	@Override
	public Document saveOrUpdateDataSource(DataSource dataSource) throws DocumentException, IOException {
		String xmlStr = this.getConfig(DIH_CONFIG);
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		Element ele = dataSourceExist(document, dataSource);
		// 如存在则先移除后增加
		if (ele != null) {
			root.remove(ele);
		}
		// 新增节点
		Element dataSourceEle = root.addElement(DIH_CONFIG_ENTITY_DATASOURCE);
		dataSourceEle.addAttribute(DIH_CONFIG_NAME, dataSource.getName());
		dataSourceEle.addAttribute(DIH_CONFIG_DATASOURCE_DRIVER, dataSource.getDriver());
		dataSourceEle.addAttribute(DIH_CONFIG_DATASOURCE_PASSWORD, dataSource.getPassword());
		dataSourceEle.addAttribute(DIH_CONFIG_DATASOURCE_TYPE, dataSource.getType());
		dataSourceEle.addAttribute(DIH_CONFIG_DATASOURCE_URL, dataSource.getUrl());
		dataSourceEle.addAttribute(DIH_CONFIG_DATASOURCE_USER, dataSource.getUser());
		this.setConfig(document);
		return document;
	}

	/**
	 * 检查datasoucre是否存在
	 * 
	 * @param dataSource
	 * @return
	 * @throws DocumentException
	 */
	private Element dataSourceExist(Document document, DataSource dataSource) {
		Element root = document.getRootElement();
		List<Element> dsList = (List<Element>) root.elements(DIH_CONFIG_ENTITY_DATASOURCE);
		for (Element dataSoucre : dsList) {
			if (dataSource.getName().equals(dataSoucre.attribute(DIH_CONFIG_NAME).getText())) {
				return dataSoucre;
			}
		}
		return null;
	}

	/**
	 * 格式化修改配置文件
	 * 
	 * @param document
	 * @throws IOException
	 */
	private void setConfig(Document document) throws IOException {
		String encoding = "UTF-8";
		StringWriter writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(encoding);

		XMLWriter xmlwriter = new XMLWriter(writer, format);
		xmlwriter.write(document);
		this.setConfig(DIH_CONFIG, writer.toString());
	}

	/**
	 * 检查entity是否存在
	 * 
	 * @param entity
	 * @return
	 */
	private Element entityExist(Document document, Entity entity) {
		Element root = document.getRootElement();
		Element documentEle = root.element(DIH_CONFIG_DOCUMENT);
		List<Element> entitys = documentEle.elements(DIH_CONFIG_ENTITY);
		for (Element entityEle : entitys) {
			if (entityEle.attributeValue(DIH_CONFIG_NAME).equals(entity.getName())) {
				return entityEle;
			}
		}
		return null;
	}

	/**
	 * xml元素转entity对象
	 * 
	 * @param entityEle
	 * @return
	 */
	private static Entity element2Entity(Document document, Element entityEle) {
		Entity entity = new Entity();
		String dataSource = entityEle.attributeValue(DIH_CONFIG_ENTITY_DATASOURCE);
		entity.setDataSource(getDataSource(document, dataSource));
		String deltaImportQuery = entityEle.attribute(DIH_CONFIG_ENTITY_DELTAIMPORTQUERY).getText();
		entity.setDeltaImportQuery(deltaImportQuery);
		String deltaQuery = entityEle.attribute(DIH_CONFIG_ENTITY_DELTAQUERY).getText();
		entity.setDeltaQuery(deltaQuery);
		String name = entityEle.attribute(DIH_CONFIG_NAME).getText();
		entity.setName(name);
		String query = entityEle.attribute(DIH_CONFIG_ENTITY_QUERY).getText();
		entity.setQuery(query);
		String pk = entityEle.attribute(DIH_CONFIG_ENTITY_PK).getText();
		entity.setPkField(pk);
		String tablecname = entityEle.attributeValue(DIH_CONFIG_ENTITY_TABLECNAME);
		entity.setTablecname(tablecname);
		String updateField = entityEle.attributeValue(DIH_CONFIG_ENTITY_UPDATEFIELD);
		entity.setUpdateField(updateField);
		String timeField = entityEle.attributeValue(DIH_CONFIG_ENTITY_TIMEFIELD);
		entity.setTimeField(timeField);
		String groupName = entityEle.attributeValue(DIH_CONFIG_ENTITY_GROUPNAME);
		entity.setGroupname(groupName);
		List<Element> fields = entityEle.elements(DIH_CONFIG_ENTITY_FIELD);
		for (Element field : fields) {
			Column column = new Column();
			String columnStr = field.attributeValue(DIH_CONFIG_ENTITY_FIELD_COLUMN);
			String nameStr = field.attributeValue(DIH_CONFIG_NAME);
			String cname = field.attributeValue(DIH_CONFIG_ENTITY_FIELD_CNAME);
			column.setName(columnStr);
			column.setAlias(nameStr);
			column.setCname(cname);
			entity.getColumns().add(column);
		}
		return entity;
	}

	/**
	 * xml元素转datasource对象
	 * 
	 * @param dsEle
	 * @return
	 */
	private static DataSource element2DataSource(Element dsEle) {
		String name = dsEle.attributeValue(DIH_CONFIG_NAME);
		String driver = dsEle.attributeValue(DIH_CONFIG_DATASOURCE_DRIVER);
		String password = dsEle.attributeValue(DIH_CONFIG_DATASOURCE_PASSWORD);
		String type = dsEle.attributeValue(DIH_CONFIG_DATASOURCE_TYPE);
		String url = dsEle.attributeValue(DIH_CONFIG_DATASOURCE_URL);
		String user = dsEle.attributeValue(DIH_CONFIG_DATASOURCE_USER);
		DataSource ds = new DataSource();
		ds.setName(name);
		ds.setDriver(driver);
		ds.setPassword(password);
		// ds.setType(type);
		ds.setUrl(url);
		ds.setUser(user);
		return ds;
	}

	/**
	 * 获取数据源
	 * 
	 * @param name
	 * @return
	 */
	private static DataSource getDataSource(Document document, String name) {
		Element root = document.getRootElement();
		List<Element> dsList = (List<Element>) root.elements(DIH_CONFIG_ENTITY_DATASOURCE);
		for (Element dataSoucre : dsList) {
			if (name.equals(dataSoucre.attribute(DIH_CONFIG_NAME).getText())) {
				return element2DataSource(dataSoucre);
			}
		}
		return null;
	}

	@Override
	public Document deleteDataSource(DataSource dataSource) throws DocumentException, IOException {
		List<DataSource> result = new ArrayList();
		String xmlStr = this.getConfig(DIH_CONFIG);
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		List<Element> dataSources = root.elements(DIH_CONFIG_ENTITY_DATASOURCE);
		for (Element dataSourceEle : dataSources) {
			if (dataSourceEle.attributeValue(DIH_CONFIG_NAME).equals(dataSource.getName())) {
				root.remove(dataSourceEle);
				this.deleteEntityByDsname(document, dataSource.getName());
				break;
			}
		}
		this.setConfig(document);
		return document;
	}

	@Override
	public List<Entity> getEntitys(String name) throws DocumentException {
		List<Entity> result = new ArrayList();
		String xmlStr = this.getConfig(DIH_CONFIG);
		Document document = DocumentHelper.parseText(xmlStr);
		Element root = document.getRootElement();
		Element documentEle = root.element(DIH_CONFIG_DOCUMENT);
		List<Element> entitys = documentEle.elements(DIH_CONFIG_ENTITY);
		for (Element entityEle : entitys) {
			if (name != null) {
				String attrName = entityEle.attributeValue(DIH_CONFIG_NAME);
				if (attrName.equals(name)) {
					result.add(element2Entity(document, entityEle));
					break;
				}
			} else {
				result.add(element2Entity(document, entityEle));
			}
		}
		return result;
	}

	/**
	 * 根据数据源名称删除实体
	 */
	private void deleteEntityByDsname(Document document, String dsname) {
		Element root = document.getRootElement();
		Element documentEle = root.element(DIH_CONFIG_DOCUMENT);
		List<Element> entitys = documentEle.elements(DIH_CONFIG_ENTITY);
		for (Element entityEle : entitys) {
			if (entityEle.attributeValue(DIH_CONFIG_ENTITY_DATASOURCE).equals(dsname)) {
				documentEle.remove(entityEle);
			}
		}
	}
}
