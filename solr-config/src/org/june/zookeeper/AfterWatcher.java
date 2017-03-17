package org.june.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
/**
 * 增加后处理
 * @author lwp
 *
 */
public abstract class AfterWatcher implements Watcher {

	@Override
	public final void process(WatchedEvent arg0) {
		inProcess(arg0);
		afterProcess();
	}
	/**
	 * process后处理方法
	 */
	public abstract void afterProcess();
	/**
	 * process执行方法
	 * @param arg0 事件对象
	 */
	public abstract void inProcess(WatchedEvent arg0);

}
