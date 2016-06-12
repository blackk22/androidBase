package com.tyl.framework;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tyl.framework.json.Gson;
import com.tyl.framework.json.JsonParseException;
import com.tyl.framework.json.JsonParser;
import com.tyl.framework.json.reflect.TypeToken;
import com.tyl.framework.log.Logger;

/**
 * 
 * @author LaoYing
 *
 */
public class TTJson {

	private final static String TAG=TTJson.class.getSimpleName();
	
	private static Gson gson=new Gson();
	/**
	 * 构建gson对象
	 */
	private static void buildGson(){
		if(gson==null){
			gson=new Gson();
		}
	}
	
	/**
	 * 判断json是不是有效的
	 * @param json
	 * @return
	 */
	public static boolean checkJsonValid(String json){
		if(json==null){
			return false;
		}
		boolean flag=true;
		try {  
            new JsonParser().parse(json);   
        } catch (JsonParseException e) {
        	Logger.e(TAG, e);
        	flag= false;  
        }
		return flag;
	}
	
	/**
	 * 判断json是不是一个对象
	 * @param json
	 * @return
	 */
	public static boolean checkJsonObject(String json){
		if(!checkJsonValid(json)){
			return false;
		}
		 return new JsonParser().parse(json).isJsonObject();
	}
	
	/**
	 * 判断json是不是一个数组
	 * @param json
	 * @return
	 */
	public static boolean  checkJsonArray(String json){
		if(!checkJsonValid(json)){
			return false;
		}
		 return new JsonParser().parse(json).isJsonArray();
	}
	/**
	 * 把单个java对象转换为json对象格式的字符串
	 * @param obj
	 * @return
	 */
	public static String objectToJson(Object obj){
		buildGson();
		if(obj==null){
			return null;
		}
		return gson.toJson(obj);
	}
	
	/**
	 * 把json对象转换为单个java对象，如果某个字段不进行转换操作  java 类设置为java 关键字transient 
	 * 如果用transient声明一个实例变量，当对象存储时，它的值不需要维持,Gson 也就不会序列化他
	 * @param json
	 * @param c class类型
	 * @return
	 */
	public static <T> T jsonToObject(String json,Class<T> c){
		 buildGson();
		 if(!checkJsonValid(json)){
			 return null;
		 }
		 return gson.fromJson(json,c);
	}
	
	/**
	 * 将json中指定name对应部分转换Map转对象 如：
	 * {"title":"view","width":800,"height":600,"thumb":{"url":"http://baidu.com/test.gif","width":80,"height":60}}
	 * 获得thumb的数据
	 *
	 * @param json
	 * @param name
	 * @param c
	 * @return
	 */
	public static <T> T jsonToObject(String json,String name,Class<T> c){
		buildGson();
		 
		 if(!checkJsonObject(json)||json.indexOf(name)==-1){
			 return null;
		 }
		String sjson=null;
		try {
			sjson = new JSONObject(json).getString(name);
		} catch (JSONException e) {
			Logger.e(TAG, e);
		}
		if(sjson==null){
			return null;
		}
		return gson.fromJson(sjson,c);
	}
	
	/**
	 * 把java List集合转换为json数组的字符串
	 * @param list
	 * @return
	 */
	public static<T> String listToJson(List<T> list){
		buildGson();
		if(list==null){
			return null;
		}
		Type listType = new TypeToken<List<T>>() {}.getType();
		return gson.toJson(list, listType);
	}
	
	/**
	 * 把一个json指定name对应json数组转换为一个java List集合对象，如：
	 * {"title":"view","width":800,"height":600,"position":[
	 * {"precision":"zip","Latitude":37.7668,"Longitude":-122.3959},
	 * {"precision":"zip","Latitude":37.7668,"Longitude":-122.3959},
	 * {"precision":"zip","Latitude":37.7668,"Longitude":-122.3959} 
	 * ]}
	 * 获得position的集合对象
	 * @param json
	 * @param listType  Type listType = new TypeToken<List<String>>() {}.getType();
	 * @return
	 */
	public static<T> T jsonToList(String json,String name,Type type){
		buildGson();
		 if(!checkJsonObject(json)||json.indexOf(name)==-1){
			 return null;
		 }
		String sjson=null;
		try {
			sjson = new JSONObject(json).getString(name);
		} catch (JSONException e) {
			Logger.e(TAG, e);
		}
		if(sjson==null){
			return null;
		}
		return gson.fromJson(sjson, type);
	}
	
	/**
	 * 把一个json数组转换为一个java List集合对象
	 * @param json
	 * @param listType  Type listType = new TypeToken<List<JavaBean>>() {}.getType();
	 * @return
	 */
	public static<T> List<T> jsonToList(String json,Type listType){
		buildGson();
		 if(!checkJsonValid(json)){
			 return null;
		 }
		return gson.fromJson(json, listType);
	}
	

	/**
	 *  将HashMap字符串转换为 JSON
	 * @param map
	 * @return
	 */
	public static<T> String mapToJson(Map<String, T> map){
		buildGson();
		if(map==null){
			return null;
		}
		return gson.toJson(map);
	}
	
	/**
	 * 将json转换Map转对象
	 */
	public static<V> Map<String, V> jsonToMap(String json,Type mapType){
		 buildGson();
		 if(!checkJsonValid(json)){
			 return null;
		 }
		return gson.fromJson(json, mapType);
	}
	
	/**
	 * 将json中指定name对应部分转换Map转对象 如：
	 * {"title":"view","width":800,"height":600,"thumb":{"url":"http://baidu.com/test.gif","width":80,"height":60}}
	 * mapType  Type mapType = new TypeToken<Map<String, String>>() {}.getType();
	 * 获得thumb的数据
	 */
	public static<V> Map<String, V> jsonToMap(String json,String name,Type mapType){
		 buildGson();
		 
		 if(!checkJsonObject(json)||json.indexOf(name)==-1){
			 return null;
		 }
		String sjson=null;
		try {
			sjson = new JSONObject(json).getString(name);
		} catch (JSONException e) {
			Logger.e(TAG, e);
		}
		if(sjson==null){
			return null;
		}
		
		return gson.fromJson(sjson, mapType);
	}
	
	
	/**
	 * 获得json对象中某个字符串对象
	 * @param json  json对象格式的字符串
	 * @param name  属性名称
	 * @return
	 */
	public  static String  jsonToObjectFile(String json,String name){
		 buildGson();
		 if(!checkJsonObject(json)||json.indexOf(name)==-1){
			 return null;
		 }
		String sjson=null;
		try {
			sjson = new JSONObject(json).getString(name);
		} catch (JSONException e) {
			Logger.e(TAG, e);
		}
		
		return sjson;
	}
	
	/**
	 * 获得json对象中某个整数字段
	 * @param json json对象格式的字符串
	 * @param name 属性名称
	 * @return
	 */
	public  static int  jsonToObjectIntFile(String json,String name){
		 int sjson=-1;
		 buildGson();
		 if(!checkJsonObject(json)||json.indexOf(name)==-1){
			 return sjson;
		 }
		
		try {
			sjson = new JSONObject(json).getInt(name);
		} catch (JSONException e) {
			Logger.e(TAG, e);
		}
		
		return sjson;
	}
	
	/**
	 * 获得json对象中某个布尔值字段
	 * @param json json对象格式的字符串
	 * @param name  属性名称
	 * @return
	 */
	public  static boolean  jsonToObjectBooleanFile(String json,String name){
		 boolean sjson=false;
		 buildGson();
		 if(!checkJsonObject(json)||json.indexOf(name)==-1){
			 return sjson;
		 }
		
		try {
			sjson = new JSONObject(json).getBoolean(name);
		} catch (JSONException e) {
			Logger.e(TAG, e);
		}
		
		return sjson;
	}
	
	
}
