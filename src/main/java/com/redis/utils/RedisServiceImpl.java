package com.redis.utils;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

	Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
	@Autowired
	protected RedisTemplate<Serializable, Serializable> redisTemplate;

	public Object saveObjectData(final String key, final Object value) {
		Object obj = null;
		try {
			obj = redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					
					byte[]bytes = ((RedisSerializer<Object>)(redisTemplate.getDefaultSerializer())).serialize(value);
					connection.set(
							redisTemplate.getStringSerializer().serialize(
									key),bytes);
					return null;
				}

			});
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("saveObjectData::缓存出现异常，请联系技术人员。" + e);
		}
		return obj;
	}

	public Object saveObjectData(final String key, final Object value, final long time) {
		
		Object obj = null;
		try {
			obj = redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					byte[]bytes = ((RedisSerializer<Object>)(redisTemplate.getDefaultSerializer())).serialize(value);
					connection.set(
							redisTemplate.getStringSerializer().serialize(
									key),bytes);
					connection.expire(redisTemplate.getStringSerializer()
							.serialize(key), time);
					return null;
				}

			});
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("saveObjectData::缓存出现异常，请联系技术人员。" + e);
		}
		return obj;
	}

	// 取数据
	public Object queryObjectData(final String keyId) {
		Object obj = null;
		try {
			obj = redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					byte[] key = redisTemplate.getStringSerializer().serialize(
							keyId);
					if (connection.exists(key)) {
						byte[] value = connection.get(key);
						Object redisValue = redisTemplate.getDefaultSerializer().deserialize(value);
						return redisValue;
					}
					return null;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("queryObjectData::缓存出现异常，请联系技术人员。" + e);
		}
		 return obj;
	}

	public Object deleteData(final String key) {
		Object obj = null;
		try {
			obj = redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.del(redisTemplate.getStringSerializer().serialize(
							key));
					return null;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("deleteData::缓存出现异常，请联系技术人员。"+ e);
		}
		return obj;
	}

}
