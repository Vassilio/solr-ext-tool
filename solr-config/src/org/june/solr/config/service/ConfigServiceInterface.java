package org.june.solr.config.service;

import org.june.solr.config.data.Entity;
/**
 * 配置管理业务逻辑层接口
 * @author lwp
 *
 */
public interface ConfigServiceInterface {
	/**
	 * 增加信息源
	 * @param entity 信息源信息
	 * @return success-成功 其他则失败
	 */
	public String saveOrUpdateEntity(Entity table);
}
