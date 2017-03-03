package org.june.solr.config.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.june.solr.config.dao.StructureConfigurable;
import org.june.solr.config.dao.impl.StructureConfigurableImp;
import org.june.solr.config.data.*;
import org.june.solr.config.service.ConfigServiceInterface;

/**
 * 配置管理业务逻辑层接口实现
 * 
 * @author lwp
 *
 */

public class ConfigServiceImpl implements ConfigServiceInterface {
	private static Logger LOGGER = Logger.getLogger(ConfigServiceImpl.class);
	private StructureConfigurable configurable;//DIH配置文件操作类
	/**
	 * 
	 * @param zkServer
	 * @throws IOException
	 */
	public ConfigServiceImpl(String zkServer) throws IOException {
		configurable = new StructureConfigurableImp(zkServer);
	}

	@Override
	public String saveOrUpdateEntity(Entity entity) {
		LOGGER.info("【接口调用】saveOrUpdateEntity");
		LOGGER.info("【参数】" + entity.toString());
		String result = "success";
		try {
			// 初始化entity
			entity.init();
			// 修改配置文件
			configurable.saveOrUpdateEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		LOGGER.info("【返回值】" + result);
		LOGGER.info("【接口调用】saveOrUpdateEntity结束");
		return result;
	}
	public void deleteEntity(Entity entity) throws DocumentException, IOException{
		configurable.deleteEntity(entity);
	}
	public static void main(String[] args) throws Exception {
		ConfigServiceImpl cs = new ConfigServiceImpl("localhost:8686");
		DataSource ds = new DataSource();
		ds.setDriver("oracle.jdbc.driver.OracleDriver");
		ds.setName("outer");
		ds.setPassword("dms");
		ds.setUrl("jdbc:oracle:thin:@10.110.1.12:1521:jcdb");
		ds.setUser("dms");
		Entity table = new Entity();
		table.setDataSource(ds);
		table.setTablename("test_entity123");
		table.setTablecname("案件信息");
		table.setGroupname("business");
		table.setPkField("CASE_ID");
		table.setUpdateField("update_time");
		table.setTimeField("crt_time");
		Column colum2 = new Column();
		colum2.setName("CASE_NAME");
		colum2.setCname("案件名称");
		colum2.setDataType("string");
		colum2.setAlias("CASE_NAME_t");
		List<Column> list = new ArrayList<Column>();
		list.add(colum2);
		table.setColumns(list);
		table.init();
		cs.saveOrUpdateEntity(table);	
		//cs.deleteEntity(table);
	}

}
