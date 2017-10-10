package bootstrap;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
import util.PropertiesUtil;

/**
 * 
 * @author donald
 * 2017年10月10日
 * 下午12:49:45
 */
public class MemcachedClientTest {
	private static final Logger log = LoggerFactory.getLogger(MemcachedClientTest.class);
	private static final String MEMCACHED_SERVER_LIST = "serverList";
	private static PropertiesUtil  propertiesUtil = PropertiesUtil.getInstance();
	public static void main(String[] args) {
		String serverList = propertiesUtil.getProperty(MEMCACHED_SERVER_LIST);
		List<InetSocketAddress> serverAddresses = AddrUtil.getAddresses(serverList);
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(serverAddresses);
		MemcachedClient memcachedClient = null;
		try {
			memcachedClient = builder.build();
			memcachedClient.set("name", 0, "donald");
			String value = memcachedClient.get("name");
			System.out.println("hello=" + value);
			memcachedClient.delete("hello");
			value = memcachedClient.get("hello");
			System.out.println("hello=" + value);
		} catch (MemcachedException e) {
			System.err.println("MemcachedClient operation fail");
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("MemcachedClient operation timeout");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			memcachedClient.shutdown();
		} catch (IOException e) {
			System.err.println("Shutdown MemcachedClient fail");
			e.printStackTrace();
		}
	}
}
