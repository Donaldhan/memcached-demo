package bootstrap;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * Memcached Standby模式 客户端测试类
 * @author 
 * donald 
 * 2017年10月10日 
 * 下午12:49:45
 */
public class MemcachedStandbyClientTest {
	private static final Logger log = LoggerFactory.getLogger(MemcachedStandbyClientTest.class);
	public static void main(String[] args) {
		MemcachedStandbyClient memcachedClient = MemcachedStandbyClient.getInstance();
		memcachedClient.set("name", 0, "donald");
		String value = (String) memcachedClient.get("name");
		log.info("set name={}", value);
		memcachedClient.delete("name");
		value = (String) memcachedClient.get("name");
		log.info("delete name={}", value);
		if (!memcachedClient.set("name", 0, "jamel")) {
			log.error("set error");
		}
		value = (String) memcachedClient.get("name");
		log.info("set name={}", value);
		if (memcachedClient.add("name", 0, "donald")) {
			log.error("Add error,key is existed");
		}
		value = (String) memcachedClient.get("name");
		log.info("name={}", value);
		if (!memcachedClient.replace("name", 0, "rain")) {
			log.error("replace error");
		}
		value = (String) memcachedClient.get("name");
		log.info("repalce name={}", value);
		memcachedClient.append("name", "-han");
		value = (String) memcachedClient.get("name");
		log.info("append name={}", value);
		memcachedClient.prepend("name", "0-");
		value = (String) memcachedClient.get("name");
		log.info("prepend name={}", value);
		memcachedClient.touch("name", 3);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		value = (String) memcachedClient.get("name");
		log.info("after touch name={}", value);
		memcachedClient.deleteWithNoReply("name");
		memcachedClient.set("age", 0, "27");
		log.info("age={}", memcachedClient.get("age"));
		memcachedClient.incr("age", 2, 1);// age 增加2，age不存在，则为1
		memcachedClient.incr("age", 1);
		log.info("incr age={}", memcachedClient.get("age"));
		memcachedClient.decr("age", 2);
		log.info("decr age={}", memcachedClient.get("age"));
		if (!memcachedClient.cas("age", 27)) {
			log.error("cas error");
		}
		log.info("cas age={}", memcachedClient.get("age"));
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
		memcachedClient.flushAll();
		memcachedClient.shutdown();
	}
}
