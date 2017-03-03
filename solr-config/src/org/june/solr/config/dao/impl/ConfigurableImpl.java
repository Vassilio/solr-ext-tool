package org.june.solr.config.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.june.solr.config.dao.Configurable;

public class ConfigurableImpl implements Configurable {
	private static Logger LOGGER = Logger.getLogger(ConfigurableImpl.class);
	private static final int SESSION_TIMEOUT = 30000;
	private ZooKeeper zooKeeper;
	// zookeeper服务地址
	private String zkServer;
	// zookeeper节点树配置文件路径
	private String basePath;

	public ConfigurableImpl(String zkServer) throws IOException {
		this(zkServer, "/configs/myconf");
	}

	public ConfigurableImpl(String zkServer, String basePath) throws IOException {
		this.zkServer = zkServer;
		Watcher watcher = new Watcher() {
			public void process(WatchedEvent event) {
			}
		};
		this.basePath = basePath;
		zooKeeper = new ZooKeeper(zkServer, SESSION_TIMEOUT, watcher);
	}

	/**
	 * 销毁方法
	 * 
	 * @throws InterruptedException
	 */
	@PreDestroy
	public void preDestory() throws InterruptedException {
		zooKeeper.close();
	}

	/**
	 * 重建zk客户端
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private synchronized void reBuild() {
		try {
			ZooKeeper zooKeeper = new ZooKeeper(zkServer, SESSION_TIMEOUT, new Watcher() {
				public void process(WatchedEvent event) {
				}
			});
			this.zooKeeper.close();
			this.zooKeeper = zooKeeper;
			LOGGER.info("【重建zk客户端】 : 成功");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("【重建zk客户端】:" + e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOGGER.error("【重建zk客户端】:" + e.getMessage());
		}
	}

	@Override
	public String getConfig(String path) {
		String result = null;
		try {
			byte[] bytes = zooKeeper.getData(basePath + "/" + path, null, null);
			result = new String(bytes, "utf-8");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			reBuild();
		}
		LOGGER.info("【获取配置文件】 : " + path);
		return result;
	}

	@Override
	public void setConfig(String path, String content) {
		try {
			zooKeeper.setData(basePath + "/" + path, content.getBytes("utf-8"), -1);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			reBuild();
		}
	}

	@PreDestroy
	public void release() throws InterruptedException {
		zooKeeper.close();
	}
	@Override
	public List<String> getConfigNodes() {
		try {
			return zooKeeper.getChildren(basePath, false);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		reBuild();
		return new ArrayList<String>();
	}
}
