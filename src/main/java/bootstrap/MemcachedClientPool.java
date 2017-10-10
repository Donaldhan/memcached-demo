package bootstrap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.StringTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import util.PropertiesUtil;

/**
 * 
 * @author donald
 * 2017年10月10日
 * 下午12:49:45
 */
public class MemcachedClientPool {
	private static final Logger log = LoggerFactory.getLogger(MemcachedClientPool.class);
	private static final String MEMCACHED_SERVER_LIST = "serverList";
	private static final String MEMCACHED_POOL_SIZE = "poolSize";
	private static PropertiesUtil  propertiesUtil = PropertiesUtil.getInstance();
	private static MemcachedClientBuilder builder;
	private static MemcachedClient memcachedClient;
	static{
		String serverList = propertiesUtil.getProperty(MEMCACHED_SERVER_LIST);
		List<InetSocketAddress> serverAddresses = AddrUtil.getAddresses(serverList);
		int poolSize = propertiesUtil.getIntegerProperty(MEMCACHED_POOL_SIZE);
		builder = new XMemcachedClientBuilder(serverAddresses);
		builder.setConnectionPoolSize(poolSize);
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key,String value){
		return set(key, 0, value);
	}
	/**
	 * 
	 * @param key
	 * @param expire 过期时间秒
	 * @param value
	 * @return
	 */
	public boolean set(String key,int expire,String value){
		boolean finish = false;
		try {
			memcachedClient = builder.build();
			finish = memcachedClient.set(key, expire, value);
		} catch (IOException e) {
			log.error("连接异常");
			e.printStackTrace();
		} catch (TimeoutException e) {
			log.error("设值超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("设值错误");
			e.printStackTrace();
		}
		return finish;
	}
	public static void main(String[] args) {
		try {
			memcachedClient = builder.build();
			memcachedClient.set("name", 0, "donald");
			String value = memcachedClient.get("name");
			log.info("set name={}",value);
			memcachedClient.delete("name");
			value = memcachedClient.get("name");
			log.info("delete name={}",value);
			if (!memcachedClient.set("name", 0, "jamel")) {
				log.error("set error");
		    }
			value = memcachedClient.get("name");
			log.info("set name={}",value);
		    if (memcachedClient.add("name", 0, "donald")) {
		  	  log.error("Add error,key is existed");
		    }
		    value = memcachedClient.get("name");
			log.info("name={}",value);
		    if (!memcachedClient.replace("name", 0, "rain")) {
		  	  log.error("replace error");
		    }
		    value = memcachedClient.get("name");
			log.info("repalce name={}",value);
		    memcachedClient.append("name", "-han");
		    value = memcachedClient.get("name");
			log.info("append name={}",value);
		    memcachedClient.prepend("name", "0-");
		    value = memcachedClient.get("name");
			log.info("prepend name={}",value);
			memcachedClient.touch("name",3);
			Thread.sleep(3000);
		    value = memcachedClient.get("name", new StringTranscoder());
		    log.info("after touch name={}",value);
		    memcachedClient.deleteWithNoReply("name");
		    memcachedClient.set("age", 0, "27");
		    log.info("age={}",memcachedClient.get("age"));
		    memcachedClient.incr("age", 2, 1);//age 增加2，age不存在，则为1
		    memcachedClient.incr("age", 1);
		    log.info("incr age={}",memcachedClient.get("age"));
		    memcachedClient.decr("age", 2);
		    log.info("decr age={}",memcachedClient.get("age"));
		    GetsResponse<Integer> result = memcachedClient.gets("age");
		    long casId = result.getCas(); 
		    //尝试将a的值更新为2
		    if (!memcachedClient.cas("age", 0, 27, casId)) {
		    	log.error("cas error");
		    }
		    log.info("cas age={}",memcachedClient.get("age"));
		    Counter counter=memcachedClient.getCounter("counter",0);
		    log.info("incrementAndGet counter,{}",counter.incrementAndGet());
		    log.info("decrementAndGet counter,{}",counter.decrementAndGet());
		    log.info("addAndGet counter,{}",counter.addAndGet(3));
		} catch (MemcachedException e) {
			log.error("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			log.error("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				memcachedClient.flushAll();
				memcachedClient.shutdown();
			} catch (IOException e) {
				log.error("Shutdown MemcachedClient fail");
				e.printStackTrace();
			} catch (TimeoutException e) {
				log.error("MemcachedClient operation timeout");
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
		}
	}
}
