package com.guangde.home.utils.storemanage;

import javax.servlet.http.HttpSession;

import com.redis.utils.RedisService;


public abstract class StoreManage {
	
	public static final String STROE_TYPE_SESSION = "session";
	public static final String STROE_TYPE_CACHE = "cache";
	public static final String STROE_TYPE_REDIS = "redis";
    
	/****************公共接口********************************************************/
    public abstract Object put(String key,Object value);
	public abstract Object get(String key);
	public abstract Object delete(String key);
	
	public abstract Object put(String key,Object value,long time);
    /********************************************************************************/
	
	/*工厂方法*/
	public static StoreManage create(String type,Object obj){
		if(STROE_TYPE_SESSION.equals(type)){
			return new SessionStoreManageImpl((HttpSession)obj);
		}else if(STROE_TYPE_CACHE.equals(type)){
			
		}else if(STROE_TYPE_REDIS.equals(type)){
			return new RedisStoreManageImpl((RedisService)obj);
		}
		return null;
	}
}
