package bootstrap.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import util.PropertiesUtil;

/**
 * Memcached Failure 模式客户端
 * @author donald
 * 2017年10月10日
 * 下午12:49:45
 */
public class MemcachedFailureClient {
	private static final Logger log = LoggerFactory.getLogger(MemcachedFailureClient.class);
	private static final String MEMCACHED_SERVER_LIST = "failureServerList";
	private static PropertiesUtil  propertiesUtil = PropertiesUtil.getInstance();
	private static volatile MemcachedFailureClient instance;
	private static MemcachedClientBuilder builder;
	private static MemcachedClient memcachedClient;
	static{
		String failureServerList = propertiesUtil.getProperty(MEMCACHED_SERVER_LIST);
		Map<InetSocketAddress, InetSocketAddress> serverAddressesMap = AddrUtil.getAddressMap(failureServerList);
		builder = new XMemcachedClientBuilder(serverAddressesMap);
		builder.setFailureMode(true);
		try {
			memcachedClient = builder.build();
		} catch (IOException e) {
			log.error("连接异常");
			e.printStackTrace();
		}
	}
	public static synchronized MemcachedFailureClient getInstance() {
		if (instance == null) {
			instance = new MemcachedFailureClient();
		}
		return instance;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key,Object value){
		return set(key, 0, value);
	}
	/**
	 * 
	 * @param key
	 * @param expire 过期时间秒
	 * @param value
	 * @return
	 */
	public boolean set(String key,int expire,Object value){
		boolean finish = false;
		try {
			finish = memcachedClient.set(key, expire, value);
		} catch (TimeoutException e) {
			log.error("set超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("set中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("set错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key,Object value){
		return add(key, 0, value);
	}
	/**
	 * 
	 * @param key
	 * @param expire 过期时间秒
	 * @param value
	 * @return
	 */
	public boolean add(String key,int expire,Object value){
		boolean finish = false;
		try {
			finish = memcachedClient.add(key, expire, value);
		} catch (TimeoutException e) {
			log.error("add超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("add中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("add错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key){
		Object value = null;
		try {
			value = memcachedClient.get(key);
		} catch (TimeoutException e) {
			log.error("get超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("get中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("get操作错误");
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 
	 * @param key
	 * @param appendValue
	 * @return
	 */
	public boolean append(String key,Object appendValue){
		boolean finish = false;
		try {
			finish = memcachedClient.append(key, appendValue);
		} catch (TimeoutException e) {
			log.error("append超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("append中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("append操作错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * 
	 * @param key
	 * @param prependValue
	 * @return
	 */
	public boolean prepend(String key,Object prependValue){
		boolean finish = false;
		try {
			finish = memcachedClient.prepend(key, prependValue);
		} catch (TimeoutException e) {
			log.error("prepend超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("prepend中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("prepend操作错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean replace(String key,Object value){
		return replace(key, 0, value);
	}
	/**
	 * 
	 * @param key
	 * @param expire 过期时间秒
	 * @param value
	 * @return
	 */
	public boolean replace(String key,int expire,Object value){
		boolean finish = false;
		try {
			finish = memcachedClient.replace(key, expire, value);
		} catch (TimeoutException e) {
			log.error("replace超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("replace中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("replace错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * 
	 * @param key
	 * @param expire
	 * @return
	 */
	public boolean touch(String key,int expire){
		boolean finish = false;
		try {
			finish = memcachedClient.touch(key, expire);
		} catch (TimeoutException e) {
			log.error("touch超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("touch中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("touch操作错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * @param key
	 * @param step
	 * @param defalut
	 * @return
	 */
	public long incr(String key,long step,long defalut){
		long value = 0;
		try {
			value = memcachedClient.incr(key, step, defalut);
		} catch (TimeoutException e) {
			log.error("incr超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("incr中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("incr操作错误");
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 
	 * @param key
	 * @param step
	 * @return
	 */
	public long incr(String key,long step){
		long value = 0;
		try {
			value = memcachedClient.incr(key, step);
		} catch (TimeoutException e) {
			log.error("incr超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("incr中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("incr操作错误");
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 
	 * @param key
	 * @param step
	 * @return
	 */
	public long decr(String key,long step){
		long value = 0;
		try {
			value = memcachedClient.decr(key, step);
		} catch (TimeoutException e) {
			log.error("decr超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("decr中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("decr操作错误");
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public long gets(String key){
		long sid = 0;
		try {
			GetsResponse<Integer> result = memcachedClient.gets(key);
			sid = result.getCas(); 
		} catch (TimeoutException e) {
			log.error("gets超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("gets中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("gets操作错误");
			e.printStackTrace();
		}
		return sid;
	}
	/**
	 * 
	 * @param key
	 * @param obj
	 * @return
	 */
	public boolean cas(String key,Object obj){
		boolean finish = false;
		long sid = gets(key);
		finish = cas(key, 0, obj, sid);
		return finish;
	}
	/**
	 * 
	 * @param key
	 * @param expire
	 * @param obj
	 * @param sid key当前版本id
	 * @return
	 */
	public boolean cas(String key, int expire, Object obj, long sid){
		boolean finish = false;
		try {
			finish = memcachedClient.cas(key, expire, obj, sid);
		} catch (TimeoutException e) {
			log.error("cas超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("cas中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("cas操作错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * @param key
	 * @return
	 */
	public boolean delete(String key){
		boolean finish = false;
		try {
			finish = memcachedClient.delete(key);
		} catch (TimeoutException e) {
			log.error("delete超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("delete中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("delete操作错误");
			e.printStackTrace();
		}
		return finish;
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public void deleteWithNoReply(String key){
		try {
			memcachedClient.deleteWithNoReply(key);
		} catch (InterruptedException e) {
			log.error("deleteWithNoReply中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("deleteWithNoReply操作错误");
			e.printStackTrace();
		}
	}
	/**
	 * @param key
	 * @return
	 */
	public Counter getCounter(String key) {
		return getCounter(key,0);
	}
	/**
	 * @param key
	 * @param init
	 * @return
	 */
	public Counter getCounter(String key,int init) {
		Counter counter = memcachedClient.getCounter(key,init);
		return counter;
	}
	/**
	 * 
	 */
	public void flushAll(){
		try {
			memcachedClient.flushAll();
		} catch (TimeoutException e) {
			log.error("flushAll超时");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("flushAll中断异常");
			e.printStackTrace();
		} catch (MemcachedException e) {
			log.error("flushAll操作错误");
			e.printStackTrace();
		}
	}
	public void shutdown(){
		try {
			memcachedClient.shutdown();
		} catch (IOException e) {
			log.error("客户端关闭异常");
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() {
		try {
			
			memcachedClient.shutdown();
		} catch (IOException e) {
			log.error("memcached 关闭客户端连接失败！");
			e.printStackTrace();
		}
	}

	
}
