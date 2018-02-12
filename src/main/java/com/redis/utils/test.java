package com.redis.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.Memcache.MemcachedUtils;
import redis.clients.jedis.Jedis;
import com.redis.vo.User;
import com.whalin.MemCached.MemCachedClient;

public class test {

	public static void main(String[] args) {
//		ApplicationContext ac = new ClassPathXmlApplicationContext(
//				"classpath:/META-INF/spring/redis-home.xml");
//		 MemCachedClient memCachedClient = (MemCachedClient)ac.getBean("memCachedClient");
//		BaseRedisService data = (BaseRedisService) ac.getBean("baseRedisService");
//		BaseRedisService.jedis.set("1", "2");
//		System.out.println(BaseRedisService.jedis.get("guo"));
//		 memCachedClient.add("name", "han");
//		boolean tt = memCachedClient.set("name", "han"); 
//		System.out.println(tt);
//	     System.out.println(memCachedClient.get("name"));  
//		 MemcachedUtils.set("aa", "bb", new Date(1000 * 60));  
//		    Object obj = MemcachedUtils.get("aa");  
//		    System.out.println("***************************");  
//		    System.out.println(obj.toString());  
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/spring/redis-home.xml");
		BaseRedisService service = (BaseRedisService) ac.getBean("baseRedisService");
		Jedis jedis = service.getJedis();
		jedis.sadd("names", "s1","s2","s3","s4");
		jedis.smembers("names");
		jedis.srem("names", "s1","s2","s3","s4");
	}

	public void testObject() {
		List<User> lists = buildTestData();

		User userA = lists.get(0);

//		ObjectsTranscoder<User> objTranscoder = new ObjectsTranscoder<User>();
//
//		byte[] result1 = objTranscoder.serialize(userA);
//
//		User userA_userA = objTranscoder.deserialize(result1);
//
//		System.out.println(userA_userA.getName() + "\t" + userA_userA.getAge());
	}

	public void testList() {
		List<User> lists = buildTestData();

//		ListTranscoder<User> listTranscoder = new ListTranscoder<User>();
//
//		byte[] result1 = listTranscoder.serialize(lists);
//
//		List<User> results = listTranscoder.deserialize(result1);
//
//		for (User user : results) {
//			System.out.println(user.getName() + "\t" + user.getAge());
//		}

	}

	private static List<User> buildTestData() {
		test tst = new test();
		User userA = new User();
		userA.setName("lily");
		userA.setAge(25);

		User userB = new User();

		userB.setName("Josh Wang");
		userB.setAge(28);

		List<User> list = new ArrayList<User>();
		list.add(userA);
		list.add(userB);

		return list;
	}
}
