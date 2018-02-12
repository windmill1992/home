package com.guangde.home.controller.mutualAid;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.huzhu.IMaUserFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiMaUser;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.UserUtil;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.http.HttpConnect;

@Controller
@RequestMapping("mutualAid")
public class MutualAidController {
	private final Logger logger = LoggerFactory.getLogger(MutualAidController.class);
	private static final String HUZHU_USER_ADD="http://hz.17xs.org/huzhu/user/add";
	@Autowired
	private RedisService redisService;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IMaUserFacade maUserFacade;
	@Autowired
	private static IDonateRecordFacade donateRecordFacade;
	/**
	 * 是否登录互助系统
	 * @return
	 */
	@RequestMapping("isOrNotLogin")
	@ResponseBody
	public JSONObject IsOrNotLogin(HttpServletRequest request,HttpServletResponse response){
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		Integer userId = UserUtil.getUserId(request, response);
		JSONObject data = new JSONObject();
		data.put("user_id", userId);
		return data;
	}
	
	/**
	 * 微信登录
	 * @param view
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("wxLogin")
	public ModelAndView wxLogin(ModelAndView view, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="url",required=false)String redirect_url){
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		//view.setViewName("redirect:http://huzhu.17xs.org");
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		ApiFrontUser user = new ApiFrontUser();
        if(userId == null || userId == 0)
        {
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 //System.out.println("userCenter_h5 >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				token = mapToken.get("access_token").toString();
        				unionid = mapToken.get("unionid").toString();
        				//System.out.println("userCenter_h5 >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
        			logger.error("H5RedPacketsController>>getRedPaket>>"+ e);
        		}
        		
        		if (user.getCoverImageId() == null) {
        			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
        		} else {
        			if(user.getCoverImageId() != null && user.getCoverImageId() == 0){
        				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getCoverImageId()));
        				user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
        			}
        		}
        		try{
        			
        			// 自动登录
        			SSOUtil.login(user, request, response);
        			/*//添加互助用户
        			ApiFrontUser apiFrontUser = new ApiFrontUser();
        			apiFrontUser=userFacade.queryById(user.getId());
        			ApiMaUser apiMaUser = new ApiMaUser();
        			apiMaUser.setUser_id(user.getId());
        			apiMaUser = maUserFacade.selectByParam(apiMaUser);
        			if(apiMaUser==null){
        				HttpConnect httpConnect = new HttpConnect();
        				URL url2 = new URL(HUZHU_USER_ADD);
        				if(apiFrontUser.getRealName()!=null && apiFrontUser.getIdCard()!=null){
        					httpConnect.FormPost(url2, "userId="+apiFrontUser.getId()+"&realName="+apiFrontUser.getRealName()+"&idNumber="+apiFrontUser.getIdCard()+"&status=0");
        				}
        				else{
        					httpConnect.FormPost(url2, "userId="+apiFrontUser.getId()+"&status=0");
        				}
        				apiMaUser = new ApiMaUser();
        				apiMaUser.setUser_id(apiFrontUser.getId());
        				apiMaUser.setReal_name(apiFrontUser.getRealName());
        				apiMaUser.setId_number(apiFrontUser.getIdCard());
        				apiMaUser.setStatus(0);
        				apiMaUser.setCreate_time(new Date());
        				apiMaUser.setUpdate_time(new Date());
        				maUserFacade.save(apiMaUser);
        			}
        			//主站添加互助用户标志
        			if(apiFrontUser.getHuzhu_state()==0){
        				apiFrontUser.setHuzhu_state(1);
        				userFacade.updateUser(apiFrontUser);
        			}*/
        			//userId存入redis
        			//redisService.saveObjectData(PengPengConstants.LOGIN_USERID+"_"+userId, user.getId(),DateUtil.DURATION_TEN_S);
        			if(redirect_url!=null && redirect_url!=""){
        				view = new ModelAndView("redirect:http://hz.17xs.org"+redirect_url);
        			}
        			else {
        				view = new ModelAndView("redirect:http://hz.17xs.org/huzhu/index");
					}
					return view;
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        	}
        	else
        	{
        		view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
				view.addObject("flag", "huzhu");
				return view;
        		
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
        if(user!=null && user.getId()!=null){
        	CookieManager.create(CookieManager.HUZHU_TOKEN, user.getId()+"_TOKEN", CookieManager.EXPIRED_30MINUTE, response, false);
        }
        else if(userId!=null && userId!=0) {
        	CookieManager.create(CookieManager.HUZHU_TOKEN, userId+"_TOKEN", CookieManager.EXPIRED_30MINUTE, response, false);
		}
    	
        if(redirect_url!=null && redirect_url!=""){
			view = new ModelAndView("redirect:http://hz.17xs.org"+redirect_url);
		}
		else {
			view = new ModelAndView("redirect:http://hz.17xs.org/huzhu/index");
		}
        return view;
	}
	
	/**
	 * 微信充值页面（互助）
	 * @param view
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("huzhu_recharge")
	public ModelAndView huzhu_recharge(ModelAndView view, HttpServletRequest request, HttpServletResponse response){
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		view.setViewName("h5/huzhu/huzhu_recharge");
		//--------------------授权登录-------------------------
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		ApiFrontUser user = new ApiFrontUser();
        if(userId == null || userId == 0)
        {
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 //System.out.println("userCenter_h5 >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
        			if ("".equals(openId) || openId == null || OToken == null) {
        				if ("".equals(weixin_code) || weixin_code == null
        						|| weixin_code.equals("authdeny")) {
        					String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
        					view = new ModelAndView("redirect:" + url_weixin_code);
        					return view;
        				}
        				mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
        				openId = mapToken.get("openid").toString();
        				token = mapToken.get("access_token").toString();
        				unionid = mapToken.get("unionid").toString();
        				//System.out.println("userCenter_h5 >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
        			logger.error("H5RedPacketsController>>getRedPaket>>"+ e);
        		}
        		
        		if (user.getCoverImageId() == null) {
        			user.setCoverImageUrl("http://www.17xs.org/res/images/user/4.jpg"); // 个人头像
        		} else {
        			if(user.getCoverImageId() != null && user.getCoverImageId() == 0){
        				ApiBFile aBFile = fileFacade.queryBFileById(Integer.valueOf(user.getCoverImageId()));
        				user.setCoverImageUrl(aBFile.getUrl()); // 个人头像
        			}
        		}
        		try{
        			
        			// 自动登录
        			SSOUtil.login(user, request, response);
        			//添加自助用户
        			ApiMaUser apiMaUser = new ApiMaUser();
        			apiMaUser.setUser_id(user.getId());
        			apiMaUser = maUserFacade.selectByParam(apiMaUser);
        			if(apiMaUser==null){
        				apiMaUser = new ApiMaUser();
        				apiMaUser.setUser_id(user.getId());
        				maUserFacade.save(apiMaUser);
        			}
        			//userId存入redis
        			redisService.saveObjectData(PengPengConstants.LOGIN_USERID+"_"+userId, user.getId(),DateUtil.DURATION_TEN_S);
        			view = new ModelAndView("redirect:" + perfecturl);
					return view;
        		}
        		catch(Exception e)
        		{
        			logger.error("",e);
        		}
        	}
        	else
        	{
        		view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
				view.addObject("flag", "huzhu");
				return view;
        		
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
		return view;
	}
	
	/**
	 * 获取互助用户信息
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserInfo")
	@ResponseBody
	public JSONObject getUserInfo(@RequestParam(value="token",required=true)String token, HttpServletRequest request,
			HttpServletResponse response){
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		//Integer userId = UserUtil.getUserId(request, response);
		JSONObject data= new JSONObject();
		//String cookie = CookieManager.retrieve("HUZHU_TOKEN", request, false);
		Integer userId=Integer.valueOf(token.split("_")[0]);
		logger.info("huzhu_user_id>>>>"+token);
		//if(token.equals(cookie)){
			ApiFrontUser user = new ApiFrontUser();
			user = userFacade.queryById(userId);
			if(user!=null){
				data.put("id", user.getId());
				data.put("realName", user.getRealName());
				data.put("idCard", user.getIdCard());
			}
		//}
		return data;
	}
	
	/**
	 * 获取home用户头像、手机号
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserHeadImgAndMobile")
	@ResponseBody
	public JSONObject getUserHeadImgAndMobile(@RequestParam(value="token",required=true)String token, HttpServletRequest request,
			HttpServletResponse response){
		//让其进行跨域请求，所有请求都允许访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		//Integer userId = UserUtil.getUserId(request, response);
		JSONObject data= new JSONObject();
		//String cookie = CookieManager.retrieve("HUZHU_TOKEN", request, false);
		Integer userId=Integer.valueOf(token.split("_")[0]);
		logger.info("huzhu_user_id>>>>"+token);
		//if(token.equals(cookie)){
			ApiFrontUser user = new ApiFrontUser();
			user = userFacade.queryById(userId);
			if(user!=null){
				data.put("realName", user.getRealName());
				data.put("headImg", user.getCoverImageUrl());
				data.put("mobileNum", user.getMobileNum());
			}
		//}
		return data;
	}
}
