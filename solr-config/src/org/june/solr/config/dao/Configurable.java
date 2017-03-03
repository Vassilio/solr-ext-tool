package org.june.solr.config.dao;

import java.util.List;

/**
 * 配置文件修改
 * @author lwp
 *
 */
public interface Configurable {
	/**
	 * 获取配置信息
	 * @param path 配置文件路径
	 * @return 配置文件内容
	 */
	public String getConfig(String path);
	/**
	 * 修改配置信息
	 * @param path 配置文件路径
	 * @param content 修改后的文件内容
	 */
	public void setConfig(String path,String content);
	/**
	 * 获取制定路径下所有节点
	 * @return
	 */
	public List<String> getConfigNodes();
}
