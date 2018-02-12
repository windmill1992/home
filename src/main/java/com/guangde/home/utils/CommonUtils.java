package com.guangde.home.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiPayConfig;
import com.guangde.entry.ApiRedpackets;
import com.guangde.entry.ApiRedpacketspool;
import com.guangde.entry.ApiThirdUser;
import com.guangde.entry.ApiUser_Redpackets;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

/**
 * 本类会调用接口的方法，变成静态的方法
 * 
 * @author guoqw
 * 
 */
public class CommonUtils {
	
	Logger logger = LoggerFactory.getLogger(CommonUtils.class);	
	
	public static ICommonFacade commonFacade = SpringContextUtil.getBean("commonFacade",ICommonFacade.class);
	public static IUserFacade userFacade = SpringContextUtil.getBean("userFacade",IUserFacade.class);
	public static RedisService redisService = SpringContextUtil.getBean("redisService",RedisService.class);
	/**获取微信的识别信息**/
	public static final String GetAccess_tokenRequest="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxa09ee42dbe779694&secret=c8c9005d568c7575770df85d9c92a87c&grant_type=authorization_code";
	/**获取微信的用户信息**/
	public static final String GetAccess_userinfoRequest = "https://api.weixin.qq.com/sns/userinfo?lang=zh_CN";
	
	
	/**
	 * 判断是否能够领取红包，能就自动领取
	 * @param projectId
	 * @param userId
	 */
	public static void IsQueryRedPacket(Integer userId, Integer projectId,String spare,IRedPacketsFacade redPacketsFacade) {

		ApiConfig apiConfig = new ApiConfig();
		apiConfig.setConfigKey("redPacketsProject");
		List<ApiConfig> list = commonFacade.queryList(apiConfig);
		String redProject = list.get(0).getConfigValue().trim();
		if (StringUtils.isNotEmpty(redProject)&&!"-1".equals(redProject)) {
			if (!"0".equals(redProject)) {//有限制
				String[] project = redProject.split(",");
				List<Integer> projectList = new ArrayList<Integer>();
				for (int i = 0; i < project.length; i++) {
					projectList.add(Integer.valueOf(project[i]));
				}
				//是否匹配
				if (projectList.contains(projectId)) {
					queryRedPacket(userId,userId,spare,projectId,redPacketsFacade);
				}
			} else {//无限制
				queryRedPacket(userId,userId,spare,projectId,redPacketsFacade);
			}
		}else {
			ApiRedpackets apiRedpacket = new ApiRedpackets();
			apiRedpacket.setAppointproject(String.valueOf(projectId));
			ApiPage<ApiRedpackets> redpackets = redPacketsFacade.queryByParam(apiRedpacket, 1, 20);
			if(redpackets.getTotal()>0){
				queryRedPacket(userId,userId,spare,projectId,redPacketsFacade);
			}
		}
	}
	
	/**
	 * 判断是否是指定项目
	 * @param projectId
	 * @param userId
	 */
	public static boolean IsQueryRedPacket(Integer projectId,IRedPacketsFacade redPacketsFacade) {

		try {
			ApiConfig apiConfig = new ApiConfig();
			apiConfig.setConfigKey("redPacketsProject");
			List<ApiConfig> list = commonFacade.queryList(apiConfig);
			String redProject = list.get(0).getConfigValue().trim();
			if (StringUtils.isNotEmpty(redProject)&&!"-1".equals(redProject)) {
				if (!"0".equals(redProject)) {//有限制
					String[] project = redProject.split(",");
					List<Integer> projectList = new ArrayList<Integer>();
					for (int i = 0; i < project.length; i++) {
						projectList.add(Integer.valueOf(project[i]));
					}
					//是否匹配
					if (projectList.contains(projectId)) {
						return true;
					}else {
						return false;
					}
				} else {//无限制
					return true;
				}
			}else {
				ApiRedpackets apiRedpacket = new ApiRedpackets();
				apiRedpacket.setAppointproject(String.valueOf(projectId));
				ApiPage<ApiRedpackets> redpackets = redPacketsFacade.queryByParam(apiRedpacket, 1, 20);
				if(redpackets.getTotal()>0){
					return true;
				}else {
					return false;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 取红包的方法 捐款成功用户自动获取一个红包
	 * 
	 * @param userId
	 */
	public static Integer queryRedPacket(Integer userId,Integer extensionpeople,String spare,Integer projectId,IRedPacketsFacade redPacketsFacade) {
		
		ApiRedpacketspool aRp = new ApiRedpacketspool();
		aRp.setAppointproject(String.valueOf(projectId));
		ApiRedpacketspool apiRedpacketspool = redPacketsFacade.queryOneRedPacket(aRp);
		if (apiRedpacketspool != null) {
			ApiUser_Redpackets apiUser_Redpackets = new ApiUser_Redpackets();
			apiUser_Redpackets.setUserId(userId);
			apiUser_Redpackets.setRedpacketspool_id(apiRedpacketspool.getId());
			apiUser_Redpackets.setRedpackets_id(apiRedpacketspool
					.getRedpackets_id());
			apiUser_Redpackets.setAmount(apiRedpacketspool.getAmount());
			apiUser_Redpackets.setStatus(401);// 待使用
			apiUser_Redpackets.setCreatetime(new Date());
			apiUser_Redpackets.setLastupdatetime(new Date());
			apiUser_Redpackets.setSpare(spare);
			Date date = DateUtil.addDay(new Date(),apiRedpacketspool.getHaveDays());
			if (DateUtil.isDateInRange(date,new Date(),apiRedpacketspool.getrEndTime())) {
				apiUser_Redpackets.setEndtime(date);
			} else {
				apiUser_Redpackets.setEndtime(apiRedpacketspool.getrEndTime());
			}

			apiUser_Redpackets.setExtensionpeople(extensionpeople);
			ApiResult result =redPacketsFacade.saveUserRedpacket(apiUser_Redpackets);

			apiRedpacketspool.setStatus(401);// 待使用
			apiRedpacketspool.setLastupdatetime(new Date());
			redPacketsFacade.updateRedpacketspool(apiRedpacketspool);
			return Integer.parseInt(result.getData());
		}else {
			return null;
		}
	}
	
	/**
	 * 个人红包的领取
	 * @param userId
	 * @param redPacketsFacade
	 * @return
	 */
	public static Integer queryPersonRedPacket(Integer userId,IRedPacketsFacade redPacketsFacade,Integer redPacketsId,Integer extensionpeople) 
	{
		
		ApiRedpacketspool aRp = new ApiRedpacketspool();
		aRp.setRedpackets_id(redPacketsId);
		ApiRedpacketspool apiRedpacketspool = redPacketsFacade.queryPersonOneRedPacket(aRp);
		if (apiRedpacketspool != null) {
			ApiUser_Redpackets apiUser_Redpackets = new ApiUser_Redpackets();
			apiUser_Redpackets.setUserId(userId);
			apiUser_Redpackets.setRedpacketspool_id(apiRedpacketspool.getId());
			apiUser_Redpackets.setRedpackets_id(apiRedpacketspool
					.getRedpackets_id());
			apiUser_Redpackets.setAmount(apiRedpacketspool.getAmount());
			apiUser_Redpackets.setStatus(401);// 待使用
			apiUser_Redpackets.setCreatetime(new Date());
			apiUser_Redpackets.setLastupdatetime(new Date());
			apiUser_Redpackets.setEndtime(apiRedpacketspool.getEndtime());
			
			apiUser_Redpackets.setExtensionpeople(extensionpeople);// 分享人id
			ApiResult result =redPacketsFacade.saveUserRedpacket(apiUser_Redpackets);

			apiRedpacketspool.setStatus(401);// 待使用
			apiRedpacketspool.setLastupdatetime(new Date());
			redPacketsFacade.updateRedpacketspool(apiRedpacketspool);// 修改红包池 随机取的当前红包状态为待使用
			return Integer.parseInt(result.getData());
		}else {
			return null;
		}
	}
	
	
	/**
	 * 根据code获取微信的识别信息openid，unionid，access_token
	 * @param code
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getAccessTokenAndopenidRequest(String code) {
		
		Map<String, Object> map = new HashMap<String, Object>(2);
		String result = GetAccess_tokenRequest+"&code="+code;
		try {
			URL urlGet = new URL(result);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("POST"); // POST方式请求
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setUseCaches(false);
			
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.parseObject(message);
			result = demoJson.getString("errcode");
			//code过期进行清除
			if(StringUtils.isNotEmpty(result)){
				redisService.saveObjectData("weixin_token", null, 0);
			}
			map.put("openid", demoJson.getString("openid"));
			map.put("unionid", demoJson.getString("unionid"));
			map.put("access_token", demoJson.getString("access_token"));
			redisService.saveObjectData("AccessToken", demoJson.getString("access_token"),DateUtil.DURATION_HOUR_S);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据code获取微信的识别信息openid，unionid，access_token
	 * @param code
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getAccessTokenAndopenidRequest(String code,ApiPayConfig apiConfig) {
		
		Map<String, Object> map = new HashMap<String, Object>(2);
		String result = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+apiConfig.getWeixinAppId()+"&secret="+apiConfig.getWeixinAppSecret()+"&grant_type=authorization_code"+"&code="+code;
		try {
			URL urlGet = new URL(result);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("POST"); // POST方式请求
			http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.setUseCaches(false);
			
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.parseObject(message);
			result = demoJson.getString("errcode");
			//code过期进行清除
			if(StringUtils.isNotEmpty(result)){
				redisService.saveObjectData("weixin_token", null, 0);
			}
			map.put("openid", demoJson.getString("openid"));
			map.put("unionid", demoJson.getString("unionid"));
			map.put("access_token", demoJson.getString("access_token"));
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 根据openid，access_token取到微信上的用户信息
	 * @param openid
	 * @param access_token
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getWeixinUserInfo(String openid,String access_token) {

		Map<String, Object> map = new HashMap<String, Object>(8);
		String result = null;
		result = GetAccess_userinfoRequest+"&openid="+openid+"&access_token="+access_token;
		try {
			URL urlGet = new URL(result);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("POST"); // POST方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String message = new String(jsonBytes, "UTF-8");
			JSONObject demoJson = JSONObject.parseObject(message);
			map.put("openid", openid);
			map.put("province", demoJson.getString("province"));
			map.put("city", demoJson.getString("city"));
			map.put("nickname", demoJson.getString("nickname"));
			map.put("sex", demoJson.getString("sex"));
			map.put("headimgurl", demoJson.getString("headimgurl"));
			is.close();
		} catch (Exception e) {

			e.printStackTrace();

		}
		return map;

	}
	
	/**
	 * 实现用户的自动登录
	 * @param request
	 * @param openId
	 * @param Token
	 * @param unionid
	 * @return
	 */
	public static ApiFrontUser queryUser(HttpServletRequest request,String openId,String Token,String unionid){
		//判段这个IP是否已经注册过
		String adressIp = SSOUtil.getUserIP(request);
		Integer userId =null;
		ApiFrontUser user = new ApiFrontUser();
		user.setRegisterIP(adressIp);
		if(StringUtils.isNotEmpty(Token) && StringUtils.isNotEmpty(openId)){
			ApiThirdUser tuser = new ApiThirdUser();
			//tuser.setAccountNum(openId);
			tuser.setUnionid(unionid);
			tuser.setType("weixin");
			tuser = userFacade.queryThirdUserByParam(tuser);
			//更新openid
			
			
			
			if(tuser !=null){
				userId = tuser.getUserId();
			}else {
				Map<String, Object> mapTourist = getWeixinUserInfo(openId,Token);
				//新增游客信息
				 String tName = (String)mapTourist.get("nickname");
				 String headimgurl = (String)mapTourist.get("headimgurl");
				 tName = StringUtil.filterEmoji(tName);
				//临时保存一下图片的路径
				user.setPersition(headimgurl);
				user.setUserType(PengPengConstants.PERSON_USER);
				user.setNickName(tName);
				user.setUserPass(StringUtil.randonNums(8));
				user.setUserName(PengPengConstants.WENXIN + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
				try {
	            	long ip = IPAddressUtil.ipToLong(adressIp);
	            	ApiIptable iptable = new ApiIptable();
	            	iptable.setEndIPNum(String.valueOf(ip));
	            	iptable.setStartIPNum(String.valueOf(ip));
	            	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
	            	if(ipList!=null && ipList.size()>0){
	            		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
	            		if(contents.length==1){
	            			user.setProvince(contents[0]);
	            		}
	            		else if(contents.length==2){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            		}
	            		else if(contents.length==3){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            			user.setArea(contents[2]);
	            		}
	            	}
				
			} catch (Exception e) {
			}
				tuser = new ApiThirdUser();
				tuser.setAccountNum(openId);
				tuser.setType("weixin");
				tuser.setUnionid(unionid);
				ApiResult result = userFacade.thirdUserRegistered(user, tuser);
				userId = Integer.valueOf(result.getMessage());
			}
			return userFacade.queryById(userId);
		}else{
			user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
			user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
			user = userFacade.queryUserByParam(user);
			//根据IP创建用户
			if(user !=null){
				return user;
			}else {
				user = new ApiFrontUser();
				try {
	            	long ip = IPAddressUtil.ipToLong(adressIp);
	            	ApiIptable iptable = new ApiIptable();
	            	iptable.setEndIPNum(String.valueOf(ip));
	            	iptable.setStartIPNum(String.valueOf(ip));
	            	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
	            	if(ipList!=null && ipList.size()>0){
	            		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
	            		if(contents.length==1){
	            			user.setProvince(contents[0]);
	            		}
	            		else if(contents.length==2){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            		}
	            		else if(contents.length==3){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            			user.setArea(contents[2]);
	            		}
	            	}
				
			} catch (Exception e) {
			}
				user.setUserName(PengPengConstants.VISITOR + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
				user.setUserPass(StringUtil.randonNums(8));
				user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
				user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
				user.setRegisterIP(adressIp);
				ApiResult apr =userFacade.registered(user);
				if(apr == null || apr.getCode() != 1){
					//注册不成功
					return null;
				}
				user = new ApiFrontUser();
				user.setRegisterIP(adressIp);
				user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
				user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
				user = userFacade.queryUserByParam(user);
				return user;
			}
		}
	}
	/**
	 * 实现用户的自动登录
	 * @param request
	 * @param openId
	 * @param Token
	 * @param unionid
	 * @return
	 */
	public static ApiFrontUser queryUser(HttpServletRequest request,String openId,String Token,String unionid,ApiPayConfig apiPayConfig){
		//判段这个IP是否已经注册过
		String adressIp = SSOUtil.getUserIP(request);
		Integer userId =null;
		ApiFrontUser user = new ApiFrontUser();
		user.setRegisterIP(adressIp);
		if(StringUtils.isNotEmpty(Token) && StringUtils.isNotEmpty(openId)){
			ApiThirdUser tuser = new ApiThirdUser();
			tuser.setAccountNum(openId);
			tuser.setUnionid(unionid);
			tuser.setType("weixin");
			tuser = userFacade.queryThirdUserByParam(tuser);
			if(tuser !=null){
				userId = tuser.getUserId();
			}else {
				Map<String, Object> mapTourist = getWeixinUserInfo(openId,Token);
				//新增游客信息
				 String tName = (String)mapTourist.get("nickname");
				 String headimgurl = (String)mapTourist.get("headimgurl");
				 tName = StringUtil.filterEmoji(tName);
				//临时保存一下图片的路径
				user.setPersition(headimgurl);
				//user.setUserType(PengPengConstants.PERSON_USER);
				user.setUserType("otherUsers("+apiPayConfig.getId()+")");
				user.setNickName(tName);
				user.setUserPass(StringUtil.randonNums(8));
				user.setUserName(PengPengConstants.WENXIN + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
				try {
	            	long ip = IPAddressUtil.ipToLong(adressIp);
	            	ApiIptable iptable = new ApiIptable();
	            	iptable.setEndIPNum(String.valueOf(ip));
	            	iptable.setStartIPNum(String.valueOf(ip));
	            	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
	            	if(ipList!=null && ipList.size()>0){
	            		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
	            		if(contents.length==1){
	            			user.setProvince(contents[0]);
	            		}
	            		else if(contents.length==2){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            		}
	            		else if(contents.length==3){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            			user.setArea(contents[2]);
	            		}
	            	}
				
			} catch (Exception e) {
			}
				tuser = new ApiThirdUser();
				tuser.setAccountNum(openId);
				tuser.setType("weixin");
				tuser.setUnionid(unionid);
				ApiResult result = userFacade.thirdUserRegistered(user, tuser);
				userId = Integer.valueOf(result.getMessage());
			}
			return userFacade.queryById(userId);
		}else{
			user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
			user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
			user = userFacade.queryUserByParam(user);
			//根据IP创建用户
			if(user !=null){
				return user;
			}else {
				user = new ApiFrontUser();
				try {
	            	long ip = IPAddressUtil.ipToLong(adressIp);
	            	ApiIptable iptable = new ApiIptable();
	            	iptable.setEndIPNum(String.valueOf(ip));
	            	iptable.setStartIPNum(String.valueOf(ip));
	            	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
	            	if(ipList!=null && ipList.size()>0){
	            		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
	            		if(contents.length==1){
	            			user.setProvince(contents[0]);
	            		}
	            		else if(contents.length==2){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            		}
	            		else if(contents.length==3){
	            			user.setProvince(contents[0]);
	            			user.setCity(contents[1]);
	            			user.setArea(contents[2]);
	            		}
	            	}
				
			} catch (Exception e) {
			}
				user.setUserName(PengPengConstants.VISITOR + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
				user.setUserPass(StringUtil.randonNums(8));
				user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
				user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
				user.setRegisterIP(adressIp);
				ApiResult apr =userFacade.registered(user);
				if(apr == null || apr.getCode() != 1){
					//注册不成功
					return null;
				}
				user = new ApiFrontUser();
				user.setRegisterIP(adressIp);
				user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
				user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
				user = userFacade.queryUserByParam(user);
				return user;
			}
		}
	}
	
	/**
	 * 微信功能调用的操作授权
	 * @param perfecturl 路径
	 * @param request 
	 * @param view 
	 * 
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static ModelAndView wxView(ModelAndView view,HttpServletRequest request,String perfecturl) throws JDOMException, IOException{
		String browser = UserUtil.Browser(request);
		if(browser.equals("wx")){
			//微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
			String jsTicket = (String) redisService.queryObjectData("JsapiTicket");
			String accessToken = (String) redisService.queryObjectData("AccessToken");
			if(jsTicket == null || accessToken == null){
				accessToken = TenpayUtil.queryAccessToken();
				redisService.saveObjectData("AccessToken" , accessToken, DateUtil.DURATION_HOUR_S);
				jsTicket = TenpayUtil.queryJsapiTicket(accessToken);
				redisService.saveObjectData("JsapiTicket" , jsTicket, DateUtil.DURATION_HOUR_S);
			}
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
		return view;
	}
}
