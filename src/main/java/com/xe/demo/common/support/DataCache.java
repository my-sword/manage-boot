package com.xe.demo.common.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.xe.demo.common.Constant;
import com.xe.demo.common.MapperKey;
import com.xe.demo.common.dao.DaoSupport;
import com.xe.demo.model.AuthOperation;

/**
 * 获取权限列表缓存
 */
@Component
public class DataCache {

	@Autowired
	private DaoSupport dao;//对象操作
	private Map<String, Object> dataMap = new HashMap<>();

	/** 
	 * 初始化 
	 */
	@PostConstruct
	public void init() {
		@SuppressWarnings("unchecked")
		//查找"所有权限"的对象集合  MapperKey：根据包位置+字段id 查找Mapper集合
		List<AuthOperation> list = (List<AuthOperation>) dao.findForList(MapperKey.OPERATION_queryAllOpers, null);
		dataMap.put(Constant.SESSION_OPERATIONS, list);//添加缓存权限信息，权限列表
		//System.out.println(dataMap);//{SESSION_OPERATIONS=[com.xe.demo.model.AuthOperation@a7ad6e5, ....
	}

	public String getString(String key) {
		Object value = getValue(key);//dataMap.get(key)
		String ret = null;
		if (null != value) {
			ret = String.valueOf(value);
		}
		return ret;
	}
	public int getInt(String key) {
		String str = getString(key);
		int ret = 0;
		if (null != str) {
			ret = Integer.parseInt(str);
		}
		return ret;
	}
	public Integer getInteger(String key) {
		String str = getString(key);
		Integer ret = null;
		if (null != str) {
			ret = Integer.parseInt(str);
		}
		return ret;
	}
	public Long getLong(String key) {
		String value = getString(key);
		Long ret = null;
		if (null != value) {
			ret = Long.parseLong(value);
		}
		return ret;
	}

	/** 
	 * 查询结果(缓存与否)
	 * 如果数据没有缓存,那么从dataMap里面获取,如果缓存了, 
	 * 那么从CACHE_KEY里面获取 
	 * 并且将缓存的数据存入到 CACHE_KEY里面 
	 * 其中key 为 #key
	 */
	//@Cacheable表示该方法是支持缓存的,将其返回值缓存起来，以保证下次利用同样的参数来执行该方法时可以直接从缓存中获取结果，而不需要再次执行该方法
	@Cacheable(value = Constant.CACHE_KEY, key = "#key")
	public Object getValue(String key) {
		return dataMap.get(key);
	}

	/** 
	 * 插入 或者更新 
	 * 插入或更新数据到dataMap中 
	 * 并且缓存到 CACHE_KEY中 
	 * 如果存在了那么更新缓存中的值 
	 * 其中key 为 #key
	 */
	//@CachePut注解，该方法每次都会执行，会清除对应的key值得缓存(或者更新) 返回值null查一次数据库==@CacheEvict 返回值不为null 更新缓存
	@CachePut(value = Constant.CACHE_KEY, key = "#key")
	public Object setValue(String key, Object value) {
		dataMap.put(key, value);
		return value;
	}

	/** 
	 * 删除 
	 * 删除dataMap里面的数据 
	 * 并且删除缓存CACHE_KEY中的数据 
	 * 其中key 为 #key
	 */
	@CacheEvict(value = Constant.CACHE_KEY, key = "#key")
	public void remove(String key) {
		dataMap.remove(key);
	}

}