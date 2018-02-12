package com.redis.utils;




public interface RedisService{
	
	Object deleteData(String key);
	
	Object queryObjectData(String key) ;
	
	Object saveObjectData(String key,Object t);
	 
	Object saveObjectData(String key,Object t,long time);
}
