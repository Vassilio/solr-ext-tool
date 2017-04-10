package org.june.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ConfigurableImpl implements Configurable {
	private static Logger LOGGER = Logger.getLogger(ConfigurableImpl.class);
	private static final int SESSION_TIMEOUT = 30000;
	private ZooKeeper zooKeeper;
	// zookeeper服务地址
	private String zkServer;
	// zookeeper节点树配置文件路径
	private String basePath = "/configs/myconf";
	public ConfigurableImpl(){}
	public ConfigurableImpl(String zkServer) throws IOException {
		this(zkServer, "/configs/myconf");
	}

	public ConfigurableImpl(String zkServer, String basePath) throws IOException {
		this.setBasePath(basePath);
		this.setZkServer(zkServer);
		this.initZk();
	}
	/**
	 * 初始化
	 * @param zkServer
	 * @param basePath
	 * @throws IOException
	 */
	public void initZk() throws IOException{
		Watcher watcher = new Watcher() {
			public void process(WatchedEvent event) {
			}
		};		
		zooKeeper = new ZooKeeper(zkServer, SESSION_TIMEOUT, watcher);
	}
	@Override
	public void addWatcher(final String pathPart,final Watcher watcher) throws KeeperException, InterruptedException{
		final String path = basePath + "/" + pathPart;
		zooKeeper.exists(path, new AfterWatcher(){
			@Override
			public void afterProcess() {
				try {
					addWatcher(pathPart,watcher);	
					LOGGER.info("【再监听成功】"+path);	
				} catch (KeeperException e) {
					LOGGER.error("【再监听失败】"+path+"\n"+e.getMessage());					
				} catch (InterruptedException e) {
					LOGGER.error("【再监听失败】"+path+"\n"+e.getMessage());
				}				
			}

			@Override
			public void inProcess(WatchedEvent arg0) {				
				watcher.process(arg0);
				LOGGER.info("【监听执行成功】"+arg0);	
			}			
		});
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

	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}

	public void setZooKeeper(ZooKeeper zooKeeper) {
		this.zooKeeper = zooKeeper;
	}

	public String getZkServer() {
		return zkServer;
	}

	public void setZkServer(String zkServer) {
		this.zkServer = zkServer;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}
