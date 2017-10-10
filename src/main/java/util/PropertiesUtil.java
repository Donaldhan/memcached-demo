package util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author donald
 * 2017年9月29日
 * 下午10:21:34
 */
public class PropertiesUtil {
	private static final Logger log  = LoggerFactory.getLogger(PropertiesUtil.class);
	private static final String MEMCACHED_CONFIG_FILE = "memcached.properties";
    private static volatile PropertiesUtil instance = null;
    private static Properties properties = null;
    static{
    	 if (properties == null) {
    		 properties = new Properties();
         }
         try {
        	 InputStream inputStream = Thread.currentThread().getContextClassLoader()
        	            .getResourceAsStream(MEMCACHED_CONFIG_FILE);
        	 properties.load(inputStream);
         } catch (IOException e1) {
             e1.printStackTrace();
         }
    }
    
    /**
     * 
     * @return
     */
    public static synchronized PropertiesUtil getInstance() {
        if (instance == null) {
        	instance = new PropertiesUtil();
        }
        return instance;
    }
    /**
     * 
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * 获取属性int值
     * @param key
     * @return
     */
    public Integer getIntegerProperty(String key) {
    	String value = properties.getProperty(key);
        return Integer.valueOf(value);
    }
    
    
    public static void main(String[] args) {
		log.info("serverList:"+PropertiesUtil.getInstance().getProperty("serverList"));
	}
}
