package org.june.solr.config.dih.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.june.solr.config.dih.core.StructureConfigurable;
import org.june.solr.config.dih.core.StructureConfigurableImp;
import org.june.solr.config.dih.data.DataSource;
import org.june.solr.config.dih.data.*;

/**
 * 配置管理业务逻辑层接口实现
 * 
 * @author lwp
 *
 */

public class Test {
	private static Logger LOGGER = Logger.getLogger(Test.class);
	private StructureConfigurable configurable;//DIH配置文件操作类
	/**
	 * 
	 * @param zkServer
	 * @throws IOException
	 */
	public Test(String zkServer) throws IOException {
		configurable = new StructureConfigurableImp(zkServer);
	}

	public String saveOrUpdateEntity(Entity entity) {
		LOGGER.info("【接口调用】saveOrUpdateEntity");
		LOGGER.info("【参数】" + entity.toString());
		String result = "success";
		try {
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
		Test cs = new Test("localhost:8686");
		DataSource ds = new DataSource();
		ds.setName("case");
		Entity table = new Entity();
		table.setDataSource(ds);
		table.setTablename("jcy_case_info");
		//table.setTablename("case");
		table.setTablecname("案件信息");
		table.setGroupname("business");
		table.setPkField("CASE_ID");
		table.setUpdateField("update_time");
		table.setTimeField("crt_time");
		Column colum2 = new Column();
		colum2.setName("CASE_NAME");
		colum2.setCname("案件名称");
		List<Column> list = new ArrayList<Column>();
		list.add(colum2);
		table.setColumns(list);
		
		//children
		Entity table1 = new Entity();
		table1.setDataSource(ds);
		table1.setTablename("jcy_case_undertaker_info");
		table1.setTablecname("子信息");
		table1.setPkField("UNDERTAKER_INFO_ID");
		table1.setParent(table);
		table1.setForeignKey("CASE_ID");
		Column colum = new Column();
		colum.setName("auth_no");
		colum.setCname("权限编号");
		List<Column> list1 = new ArrayList<Column>();
		list1.add(colum);
		Column colum1 = new Column();
		colum1.setName("CASE_NAME");
		colum1.setCname("案件名称");
		list1.add(colum1);
		table1.setColumns(list1);
		
		table.getChildren().add(table1);
		//table.getChildren().add(table1);
		//table.init();
		cs.saveOrUpdateEntity(table);	
		//cs.deleteEntity(table);
	}

}
