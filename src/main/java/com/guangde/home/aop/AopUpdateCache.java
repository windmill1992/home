package com.guangde.home.aop;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.guangde.entry.BaseBean;
import com.guangde.home.utils.BGConfig;
import com.redis.utils.BaseRedisService;


@Component
@Aspect
public class AopUpdateCache {

	private static final Logger logger = LoggerFactory.getLogger(AopUpdateCache.class);
	@Autowired
	private BaseRedisService baseRedisService;
	private final  ExecutorService pool = Executors.newCachedThreadPool();
	private static int DEFAULT_UPDATE_TIME = 1000;//两次更新之间的间隔1秒
	
	/**
	 * 默认缓存更新aop
	 * 使用缓存更新的方法：api包下，以update,launch,save,delete开头的方法
	 *  使用缓存需符合的参数格式BaseBean baseBean,有baseBean.getRange_key()指定要更新的缓存范围
	 * 
	 */
	@Around("execution(* com.guangde.api..*.save*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object[]obj = pjp.getArgs();
		String userCache = BGConfig.get("userCache");
		boolean isCache = userCache!=null?new Boolean(userCache):false;
		if(isCache&&obj.length>0){
			//第一个参数是BaseBean或其子类才用缓存
			if(obj[0] instanceof BaseBean){
				BaseBean bean = (BaseBean)obj[0];
				if(bean.isCache()&&(bean.getRange_key()!=null&&bean.getRange_key().size()>0)){
					dealCache(bean,true,0,1);
				}
			}
		}
		return pjp.proceed();
	}
	private void dealCache(BaseBean bean,boolean isAsyn,long millis,int time){
		if(bean==null||bean.getRange_key()==null||bean.getRange_key().size()==0)
			return;
		if(isAsyn){
			delCache(bean,time);
		}else{
			final BaseBean b = bean;
			final long m = millis;
			final int t = time;
			pool.execute(new Runnable() {
				@Override
				public void run() {
					if(m>0)
						try {
							Thread.sleep(m);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					delCache(b,t);
				}
			});
		}
	}
	private void delCache(BaseBean bean,int time){
		Jedis jedis = baseRedisService.getJedis();
		Set<String> delKeys = null;
		for(String range_key:bean.getRange_key()){
			delKeys = jedis.smembers(range_key);
			if(delKeys!=null&&delKeys.size()>0){
				String[]keys = delKeys.toArray(new String[delKeys.size()]);
				jedis.del(keys);
				jedis.srem(range_key,keys);
			}
		}
		if(time>0){
			//再调用一次是为了，在按缓存范围删除缓存时，有可能缓存范围的内容已经改变
			//由于未加锁，不保证同步
			//todo 后期看使用效果，如果不理想要修改
			
			dealCache(bean,false,DEFAULT_UPDATE_TIME,0);
		}
	}
}
