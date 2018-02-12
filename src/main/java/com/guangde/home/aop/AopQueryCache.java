package com.guangde.home.aop;

import com.guangde.entry.BaseBean;
import com.guangde.home.utils.BGConfig;
import com.guangde.home.utils.DateUtil;
import com.redis.utils.BaseRedisService;
import com.redis.utils.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author px
 * 缓存aop
 *
 */

@Component
@Aspect
public class AopQueryCache {
	private static final Logger logger = LoggerFactory.getLogger(AopQueryCache.class);
	@Autowired
	private RedisService redisService;
	@Autowired
	private BaseRedisService baseRedisService;
	private final  ExecutorService pool = Executors.newCachedThreadPool();
	/**
	 * 默认缓存查询aop
	 * 
	 * 使用缓存查询的方法：api包下，以query开头的方法
	 * 
	 * 使用缓存需符合的参数格式：例，args:（BaseBean baseBean, int pageNum, int pageSize）or args:（BaseBean baseBean）
	 * 
	 */
	@Around("execution(* com.guangde.api..*.query*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object[]obj = pjp.getArgs();
		String userCache = BGConfig.get("userCache");
		boolean isCache = userCache!=null?new Boolean(userCache):false;
		if(isCache&&obj.length>0){
			//第一个参数是BaseBean或其子类才用缓存
			if(obj[0] instanceof BaseBean){
				BaseBean bean = (BaseBean)obj[0];
				if(bean.isCache()&&bean.getKey().length()>0){
					if(obj.length>2){
						bean.getKey().append("_pageNum_");
						bean.getKey().append(obj[1].toString());
						bean.getKey().append("_pageSize_");
						bean.getKey().append(obj[2].toString());
					}
					String key = bean.getKey().toString();
					logger.info(key);
					Object result = redisService.queryObjectData(key);
					if(result==null){
						logger.info(key+"缓存未命中");
						result =  pjp.proceed();
						if(bean.getValidTime()==null){
							redisService.saveObjectData(key, result);
						}else{
							redisService.saveObjectData(key, result,bean.getValidTime());
						}
						//分配查询结果缓存范围
						allotCacheRange(bean,result,false);
					}else{
					    logger.info(key+"缓存命中");
					}
				    return result;
				}
			}
		}
		return pjp.proceed();
	}
	/**
	 * 对查询结果分配缓存范围，以便更新操作时，将对应缓存范围的缓存置为无效
	 *
	 * @param query    查询参数
	 * @param result   查询结果
	 * @param isAsyn   是否异步处理
	 */
	public void allotCacheRange(BaseBean query,Object result,boolean isAsyn){
		
		if(result==null||query==null)
			return ;
		
		if(isAsyn){
			allotCacheRange(query, result);
		}else{
			final BaseBean q = query;
			final Object r = result;
			//todo 后期优化，队列中有相同任务执行一次
			pool.execute(new Runnable() {
				@Override
				public void run() {
					allotCacheRange(q, r);
				}
			});
		}
	}
	private void allotCacheRange(BaseBean query,Object result){
		    List<String> range_keys = query.allotCacheRange(result);
		    if(range_keys!=null){
		    	String key = query.getKey().toString();
		    	//将查询缓存的键值记录到对应的缓存范围
		    	Jedis jedis = baseRedisService.getJedis();
		    	for(String range_key:range_keys){
		    		jedis.sadd(range_key, key);
		    		jedis.expire(range_key, (int)DateUtil.DURATION_WEEK_S);
		    	}
		    }
	}
}
