package com.redis.utils;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

/**
 * 封装redis 缓存服务器服务接口
 * 
 */
@Service("baseRedisService")
public class BaseRedisService {
	private static Logger logger = LoggerFactory.getLogger(BaseRedisService.class);
	private static int DEFAULT_LOCK_TIME = 60;//默认锁住时间1分钟
	// 操作redis客户端
	private   Jedis jedis;
	@Autowired
	@Qualifier("connectionFactory")
	private JedisConnectionFactory jedisConnectionFactory;
	
	@PostConstruct
	public void init(){
		//初始化redis java客户端
		if (jedis == null) {
			synchronized(this){
				if(jedis==null){
					jedis = jedisConnectionFactory.getShardInfo().createResource();
				}
			}
		}
	}
	
	/**
	 * 获取锁，并等待
	 * @param connection
	 * @param key
	 * @param value
	 * @return
	 * //todo 后期使用，监控程序状态，
	 */
	public  boolean tryLock(String key,String value){
		boolean isLock = false;
		try{
			do{
				if(jedis.setnx(key, value)==1){
					jedis.expire(key, DEFAULT_LOCK_TIME);
					isLock = true;
					break;
				}
				Thread.sleep(1000);
			}while(true);
		}catch(Exception e){
			logger.error("缓存获取锁失败");
		}
		return isLock;
	}
	public  boolean tryRelease(String key,String value){
		boolean isRelease = false;
		try{
			
			String v = jedis.get(key);
			if(value.equals(v)){
				//因为比较和删除，两个操作非原子操作，在锁缓存超过有效时间的情况下存在小概率的问题（在锁缓存有效时间还未执行完程序，释放锁，该程序有问题，需优化）
				jedis.del(key);
			}
		}catch(Exception e){
			logger.error("缓存释放锁失败");
		}
		return isRelease;
	}
	
	/**
	 * 获取一个jedis 客户端
	 * 
	 * @return
	 */
	public Jedis getJedis() {
		//防止对象外露
		Jedis temp = jedis;
		return temp;
	}
}
