package com.guangde.home.utils.storemanage;

import com.redis.utils.RedisService;

public class RedisStoreManageImpl extends StoreManage{

	private RedisService redisService;
	
	public RedisStoreManageImpl(RedisService redisService) {
		this.redisService = redisService;
	}
	

	@Override
	public Object put(String key, Object value) {
		//todo 修改要返回结果
		 redisService.saveObjectData(key, value);
		 return null;
	}

	@Override
	public Object get(String key) {
		return redisService.queryObjectData(key);
	}

	@Override
	public Object delete(String key) {
		 redisService.deleteData(key);
		 return null;
	}


	@Override
	public Object put(String key, Object value, long time) {
		redisService.saveObjectData(key, value, time);
		return null;
	}

}
