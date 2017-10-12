package client;


import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.MemcachedFailureClient;
import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * Memcached Failure 模式客户端测试类
 * @author 
 * donald 
 * 2017年10月10日 
 * 下午12:49:45
 */
public class MemcachedFailureClient2Test {
	private static final Logger log = LoggerFactory.getLogger(MemcachedFailureClient2Test.class);
	public static void main(String[] args) {
		MemcachedFailureClient memcachedClient = MemcachedFailureClient.getInstance();
		memcachedClient.flushAll();
		memcachedClient.set("name", 0, "donald");
		String value = (String) memcachedClient.get("name");
		log.info("set name={}", value);
		memcachedClient.set("age", 0, "27");
		log.info("age={}", memcachedClient.get("age"));
		memcachedClient.incr("age", 1);
		log.info("incr age={}", memcachedClient.get("age"));//debug breakpoint
	    try {
			Thread.sleep(180000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		memcachedClient.set("sex", "man");
		Counter counter = memcachedClient.getCounter("counter", 0);
		try {
			log.info("incrementAndGet counter,{}", counter.incrementAndGet());
			log.info("decrementAndGet counter,{}", counter.decrementAndGet());
			log.info("addAndGet counter,{}", counter.addAndGet(3));
		} catch (MemcachedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		memcachedClient.shutdown();
	}
}
