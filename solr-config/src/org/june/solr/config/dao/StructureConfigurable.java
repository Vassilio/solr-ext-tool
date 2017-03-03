package org.june.solr.config.dao;

import java.io.IOException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.june.solr.config.data.DataSource;
import org.june.solr.config.data.Entity;

/**
 * 结构化配置
 * @author lwp
 *
 */
public interface StructureConfigurable {
	/**
	 * 创建实体,如存在则更新
	 * @param entity
	 * @return 
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public Document saveOrUpdateEntity(Entity entity) throws DocumentException, IOException;
	/**
	 * 获取所有实体
	 * @return
	 * @throws DocumentException 
	 */
	public List<Entity> getEntitys() throws DocumentException;
	/**
	 * 根据信息源名称获取获取实体
	 * @return
	 * @throws DocumentException 
	 */
	public List<Entity> getEntitys(String name) throws DocumentException;
	/**
	 * 删除实体
	 * @param entity
	 * @return 
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public Document deleteEntity(Entity entity) throws DocumentException, IOException;
	/**
	 * 获取所有数据源
	 * @return
	 * @throws DocumentException 
	 */
	public List<DataSource> getDataSources() throws DocumentException;
	/**
	 * 获取指定数据源
	 * @return
	 * @throws DocumentException 
	 */
	public List<DataSource> getDataSources(String name) throws DocumentException;
	/**
	 * 增加DataSource,如存在则更新
	 * @param dataSource
	 * @return 
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public Document saveOrUpdateDataSource(DataSource dataSource) throws DocumentException, IOException;
	/**
	 * 删除数据源
	 * @param dataSource
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public Document deleteDataSource(DataSource dataSource) throws DocumentException, IOException;
}
