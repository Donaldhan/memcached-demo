package util;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * json工具类
 * @author donald
 * 2017年7月20日
 * 下午9:57:34
 */
public class JacksonUtil {
	private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);
    private static  JacksonUtil instance;
    private static  JsonFactory jsonFactory;
    private static ObjectMapper objectMapper;
    private static String[] stringFields={"userPwd" , "oldUserPwd" , "newUserPwd" , "reNewUserPwd" };//过滤字段
    public static final String STRING_FILTER="string_filter";//过滤器名字
    static{
    	 jsonFactory = new JsonFactory();
    	 objectMapper = new ObjectMapper();
         //包含密码字段过滤器
         FilterProvider filterProvider =
                 new SimpleFilterProvider().addFilter(STRING_FILTER,SimpleBeanPropertyFilter
                         .serializeAllExcept(stringFields));
         objectMapper.setFilters(filterProvider);
    }
    /**
     * 
     * @return
     */
    public static synchronized JacksonUtil getInstance() {
        if (instance == null) {
                instance = new JacksonUtil();
         }
        return instance;
    }
    /**
     *将对象转化为 json字符串
     * @param obj
     * @return
     */
    public String toJson(Object obj) {
    	String result = null;
        JsonGenerator jsonGenerator = null;
        try {
            StringWriter out = new StringWriter();
            jsonGenerator = jsonFactory.createGenerator(out);
            objectMapper.writeValue(jsonGenerator, obj);
            result =  out.toString();

        } catch (Exception e) {
        	log.error("=====对象转化为 json字符串异常："+getExceptionStackInfo(e));
            e.printStackTrace();
        } finally {
            try {
                if (jsonGenerator != null)
                    jsonGenerator.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。 (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List:readValue(json,List .class).但是如果我们想把json转换为特定类型的List，比如List<Student>，就不能直接进行转换了。
     * 因为readValue(json ,List.class)返回的其实是List<Map>类型，你不能指定readValue()的第二个参数是List<
     * Student>.class，所以不能直接转换。 我们可以把readValue()的第二个参数传递为Student[].class.然后使用Arrays
     * .asList();方法把得到的数组转换为特定类型的List。 (3)转换为Map：readValue(json,Map.class)
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我们使用泛型，得到的也是泛型
     * 
     * @param content 要转换的JavaBean类型
     * @param valueType 原始json字符串数据
     * @return JavaBean对象
     */
    /**
     * 将json字符串转为bean对象
     * @param json
     * @param clazz
     * @return
     */
    public <T> T toBean(String json, Class<T> clazz) {
    	T result = null;
        try {
        	result =  objectMapper.readValue(json, clazz);
        } catch (Exception e) {
        	log.error("=====json字符串转为bean对象异常："+getExceptionStackInfo(e));
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 转换字符串为List
     * @param json
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T> List<T> toList(String json, Class<T> clazz) {
    	List<T> result = null;
        try {
        	JavaType javaType = getCollectionJavaType(List.class,clazz);
        	result =  (List<T>) objectMapper.readValue(json, javaType);
        } catch (Exception e) {
        	log.error("=====json字符串转为bean对象异常："+getExceptionStackInfo(e));
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 转换json字符串为Map对象
     * @param json
     * @param kClazz
     * @param vClazz
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <K,V> Map<K,V> toMap(String json, Class<K> kClazz,Class<V> vClazz) {
    	Map result = null;
        try {
        	JavaType javaType = getCollectionJavaType(Map.class,kClazz,vClazz);
        	result =  objectMapper.readValue(json, javaType);
        } catch (Exception e) {
        	log.error("=====json字符串转为Map对象异常："+getExceptionStackInfo(e));
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 获取泛型的Collection的JavaType
     * @param collectionClass 集合类型
     * @param elementClasses 集合元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public JavaType getCollectionJavaType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass,
                elementClasses);
    }

    /**
     * 获取json字符串属性attr的值
     * @param json
     * @param attr
     * @return
     */
    public String getJsonAttrValue(String json, String attr) {
    	String result = null;
        try {
            JsonNode node = objectMapper.readTree(json);
            result = node.get(attr).toString();
        } catch (IOException e) {
        	log.error("=====获取json字符串属性值异常："+getExceptionStackInfo(e));
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 获取异常堆栈信息
     * @param e
     * @return
     */
    private String getExceptionStackInfo(Exception e){
    	final StringWriter sWriter = new StringWriter();
    	final PrintWriter pWriter = new PrintWriter(sWriter);
    	e.printStackTrace(pWriter);
    	return sWriter.toString();
    }
}
