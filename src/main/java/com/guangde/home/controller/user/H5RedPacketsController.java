package com.guangde.home.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiPayConfig;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiRedpackets;
import com.guangde.entry.ApiRedpacketspool;
import com.guangde.entry.ApiUser_Redpackets;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping(value="redPackets")
public class H5RedPacketsController {
	
	private final Logger logger = LoggerFactory.getLogger(H5RedPacketsController.class);
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IUserFacade  userFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IFileFacade fileFacade;
	/**
	 * 收到红包页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getRedPaper")
	public ModelAndView getRedPaper(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "status", required = false ,defaultValue = "401") Integer status){
		
		ModelAndView view = new ModelAndView("h5/redPackets/redPaper");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
        
		return view;
	}
	/**
	 * 我发出的红包
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="mySendRedPaper")
	public ModelAndView mySendRedPaper(HttpServletRequest request,HttpServletResponse response){
		
		ModelAndView view = new ModelAndView("h5/redPackets/myRedPaper");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
			return view ; 
		}
        
		return view;
	}
	
	/**
	 * 收到红包列表
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @param status 401 ： 未使用 402 ： 已使用 403 ： 已过期
	 * @return
	 */
	@RequestMapping("redPaper")
    @ResponseBody
    public Map<String, Object> redPaper(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId,
        @RequestParam(value = "status", required = false ,defaultValue = "401") Integer status)
    {
        
        if (userId == null)
        {
            // 1.验证是否登入
            userId = UserUtil.getUserId(request, response);
            if (userId == null)
            {
                return webUtil.loginFailedRes(null);
            }
        }
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        ApiPage<ApiUser_Redpackets> data =null;
        ApiUser_Redpackets ur = new ApiUser_Redpackets();
        
        if(status == 401){
        	ur.setUserId(userId);
        	ur.setStatus(401);
        	data = redPacketsFacade.queryUserRedpacketsByCondition(ur, page, pageNum);
        	if(data.getResultData() != null && data.getResultData().size() >0){
        		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        		for(ApiUser_Redpackets ar:data.getResultData()){
        			if(ar.getEndtime() != null)
        			{
        				ar.setShowDate(sm.format(ar.getEndtime()));
        			}
        		}
        	}
        }else if(status == 402){
        	ur.setUserId(userId);
        	ur.setStatus(402);
        	data = redPacketsFacade.queryRedPaperByDonated(ur, page, pageNum);
        	if(data.getResultData() != null && data.getResultData().size() >0){
        		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        		for(ApiUser_Redpackets ar:data.getResultData()){
        			if(ar.getDonatTime() != null){
        				ar.setShowDate(sm.format(ar.getDonatTime()));
        			}
        			if(StringUtils.isEmpty(ar.getCoverImageUrl())){
        				ar.setCoverImageUrl("http://www.17xs.org/res/images/logo-def.jpg");
        			}
        		}
        	}
        }else if(status == 403){
        	ur.setUserId(userId);
        	ur.setStatus(403);
        	data = redPacketsFacade.queryUserRedpacketsByCondition(ur, page, pageNum);
        }
        
        if (data != null)
        {
        	
        	p.setData(data.getResultData());
        	p.setNums(data.getTotal());
        	
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "红包列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
	
	
	/**
	 * 发出去的红包列表
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @param status 401 ： 未使用 402 ： 已使用 403 ： 已过期
	 * @return
	 */
	@RequestMapping("redPaperUsed")
    @ResponseBody
    public Map<String, Object> redPaperUsed(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId,
        @RequestParam(value = "status", required = false ,defaultValue = "401") Integer status)
    {
        
        if (userId == null)
        {
            // 1.验证是否登入
            userId = UserUtil.getUserId(request, response);
            if (userId == null)
            {
                return webUtil.loginFailedRes(null);
            }
        }
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        ApiPage<ApiUser_Redpackets> data =null;
        ApiUser_Redpackets ur = new ApiUser_Redpackets();
        
        ur.setUserId(userId);
        ur.setStatus(status);
        
        if(status == 403)
        {
        	data = redPacketsFacade.queryEndbackUserRedpacketsByCondition(ur, page, pageNum);
        	if(data.getResultData() != null && data.getResultData().size() >0){
        		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        		for(ApiUser_Redpackets ar:data.getResultData()){
        			if(ar.getCreatetime() != null){
        				ar.setShowDate(sm.format(ar.getCreatetime()));
        			}
        		}
        	}
        }
        else
        {
        	ur.setStatus(status);
        	data = redPacketsFacade.querySendRedPaperByDonated(ur, page, pageNum);
        	if(data.getResultData() != null && data.getResultData().size() >0){
        		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        		for(ApiUser_Redpackets ar:data.getResultData()){
        			if(ar.getDonatTime() != null){
        				ar.setShowDate(sm.format(ar.getDonatTime()));
        			}
        			if(StringUtils.isEmpty(ar.getCoverImageUrl())){
        				ar.setCoverImageUrl("http://www.17xs.org/res/images/logo-def.jpg");
        			}
        		}
        	}
        }
        
        
        if (data != null)
        {
        	
        	p.setData(data.getResultData());
        	p.setNums(data.getTotal());
        	
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "红包列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
	
	
	/**
	 * 已被领取红包页 （延后做）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getHasGetRedPaper")
	public ModelAndView getHasGetRedPaper(HttpServletRequest request,HttpServletResponse response){
		ModelAndView view = new ModelAndView("h5/redPackets/hasGetRedPaper");
		return view;
	}
	/**
	 * 领红包成功
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping(value="getRedPaket")
	public ModelAndView getRedPaket(@RequestParam("extensionpeople") Integer extensionpeople,
			@RequestParam("tradeNo") String tradeNo,@RequestParam(value="projectId",required=false,defaultValue="-1") Integer projectId,
			HttpServletRequest request,HttpServletResponse response) throws JDOMException, IOException{
		
		ModelAndView view = new ModelAndView("h5/redPackets/sendRedPaper");
		String browser = UserUtil.Browser(request);
		
		Integer userId = UserUtil.getUserId(request, response);
		//===========微信用户自动登陆start==============//
		
				String openId ="";
				String token = "";
				String unionid = "";
				StringBuffer url = request.getRequestURL();
				String queryString = request.getQueryString();
				String perfecturl = url + "?" + queryString;
				
				ApiFrontUser user = new ApiFrontUser();//捐款用户
				
		        if(userId == null || userId == 0)
		        {
		        	/*
					 * shareType : 0 分享指引
					 * shareType : 1 分享成功
					 */
		        	//browser = "wx";
		        	if(browser.equals("wx")){
		        		String weixin_code = request.getParameter("code");
		        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
		        		try {
			   				 Object OToken = redisService.queryObjectData("weixin_token");
			   				 token = (String)OToken;
			   				 System.out.println("userCenter_h5 >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
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
		        				System.out.println("userCenter_h5 >> tenpay.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
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
		        			//view = new ModelAndView("redirect:" + perfecturl);
        					//return view;
		        		}
		        		catch(Exception e)
		        		{
		        			logger.error("",e);
		        		}
		        		
		        	}
		        	else
		        	{
		        		//to do >> 暂时跳转到登陆页
						view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
						return view;
		        		
		        	}
		        }else {
		        	user = userFacade.queryById(userId);
				}
				
				//===========微信用户自动登陆end==============//
		      //微信调度的唯一凭证AccessToken失效，要同时更新jsTicket这个临时凭证
		        if(browser.equals("wx")){
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
	        		view.addObject("payway", browser);
	        		view.addObject("tradeNo", tradeNo);
	        		view.addObject("nickName", user.getNickName());
		        }
		
		
		//取红包
		Integer urId = null;
		if (userId != null ) {
			ApiUser_Redpackets apiUR = new ApiUser_Redpackets();
			apiUR.setExtensionpeople(extensionpeople);
			apiUR.setSpare(tradeNo);
			apiUR.setStatusList(null);
			Integer count = redPacketsFacade.countRedPaperNumByCondition(apiUR);
			if(count >= 500){
				apiUR.setUserId(userId);
				apiUR.setExtensionpeople(extensionpeople);
				apiUR.setSpare(tradeNo);
				ApiPage<ApiUser_Redpackets> aResult = redPacketsFacade.queryUserRedpacketsByCondition(apiUR, 1,10);
				if(aResult.getResultData().size() > 0 ){
					//获取随机捐赠的项目
					ApiProject ap = new ApiProject();
					ap.setState(240);//募捐中
					ap.setIsHide(0);
					ApiPage<ApiProject> apiPage = projectFacade.queryProjectListNew(ap, 1, 20);
					int random = (int) (Math.random()*19);
					ApiProject project = apiPage.getResultData().get(random);
					if(StringUtils.isEmpty(project.getCoverImageUrl())){
						project.setCoverImageUrl("http://www.17xs.org/res/images/logo-def.jpg");
					}
					view.addObject("p", project);
					view.addObject("ur", aResult.getResultData().get(0));
					
				}else {
					view.addObject("ur", apiUR);
				}
			}else {
				apiUR.setUserId(userId);
				apiUR.setExtensionpeople(extensionpeople);
				apiUR.setSpare(tradeNo);
				apiUR.setStatusList(null);
				count = redPacketsFacade.countRedPaperNumByCondition(apiUR);
				if(count == 0){//未领取
					urId = CommonUtils.queryRedPacket(userId,extensionpeople,tradeNo,projectId,redPacketsFacade);
					if(urId != null){
						ApiUser_Redpackets ur = redPacketsFacade.queryUserRedpacketsById(urId);
						view.addObject("ur", ur);
					}
				
				}else {//已领取
					ApiPage<ApiUser_Redpackets> aResult = redPacketsFacade.queryUserRedpacketsByCondition(apiUR, 1,10);
					if(aResult.getResultData().size() > 0 ){
						view.addObject("ur", aResult.getResultData().get(0));
					}
				}
				//获取随机捐赠的项目
				ApiProject ap = new ApiProject();
				ap.setState(240);//募捐中
				ap.setIsHide(0);
				ApiPage<ApiProject> apiPage = projectFacade.queryProjectListNew(ap, 1, 20);
				int random = (int) (Math.random()*19);
				ApiProject project = apiPage.getResultData().get(random);
				if(StringUtils.isEmpty(project.getCoverImageUrl())){
					project.setCoverImageUrl("http://www.17xs.org/res/images/logo-def.jpg");
				}
				view.addObject("p", project);
			}
			
		}
			
		return view;
	}
	
	
	/**
	 * 个人发送红包的领取
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@RequestMapping(value="getPersonRedPaket")
	public ModelAndView getPersonRedPaket(@RequestParam(value="extensionpeople",required=false) Integer extensionpeople
			,@RequestParam("id") Integer id,@RequestParam(value="type",required=false,defaultValue="0") Integer type,
			HttpServletRequest request,HttpServletResponse response) throws JDOMException, IOException{
		
		ModelAndView view = new ModelAndView("h5/redPackets/sendRedPaper");
		String browser = UserUtil.Browser(request);
		Integer userId = UserUtil.getUserId(request, response);
		//===========微信用户自动登陆start==============//
				String openId ="";
				String token = "";
				String unionid = "";
				StringBuffer url = request.getRequestURL();
				String queryString = request.getQueryString();
				String perfecturl = url + "?" + queryString;
				
				ApiFrontUser user = new ApiFrontUser();//捐款用户
		        if(userId == null || userId == 0)
		        {
		        	if(browser.equals("wx")){
		        		String weixin_code = request.getParameter("code");
		        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
		        		try {
			   				 Object OToken = redisService.queryObjectData("weixin_token");
			   				 token = (String)OToken;
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
		        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
		        			}
		        			user = CommonUtils.queryUser(request,openId,token,unionid);
		        		} catch (Exception e) {
		        			logger.error("H5RedPacketsController>>getPersonRedPaket>>"+ e);
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
		        			//view = new ModelAndView("redirect:" + perfecturl);
		        		}
		        		catch(Exception e)
		        		{
		        			logger.error("",e);
		        		}
		        		
		        	}
		        	else
		        	{
		        		//to do >> 暂时跳转到登陆页
						view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
						return view;
		        		
		        	}
		        }else {
		        	user = userFacade.queryById(userId);
				}
		        //===========微信用户自动登陆end==============//
				
		    
		 CommonUtils.wxView(view, request, perfecturl);
			    	
		    	
		
		//取红包
		Integer urId = null;
//		if(userId == null)
//		{
//			view = new ModelAndView("redirect:ucenter/user/Login_H5.do");
//		}

				if (userId != null ) {
					ApiUser_Redpackets apiUR = new ApiUser_Redpackets();
					apiUR.setUserId(userId);
					apiUR.setRedpackets_id(id);
					apiUR.setStatusList(null);
					int count = redPacketsFacade.countRedPaperNumByCondition(apiUR);
					if(type != 110)
					{
						if(count == 0){//未领取
							urId = CommonUtils.queryPersonRedPacket(userId,redPacketsFacade,id,extensionpeople);
							if(urId != null){
								ApiUser_Redpackets ur = redPacketsFacade.queryUserRedpacketsById(urId);
								view.addObject("ur", ur);
							}
							else
							{
								ApiRedpackets  apiRedpackets = new ApiRedpackets();
								apiRedpackets.setId(id);
								ApiPage<ApiRedpackets> aResult = redPacketsFacade.queryByCondition(apiRedpackets, 1, 1);
								if(aResult.getResultData().size() > 0 ){
									ApiUser_Redpackets ur = new  ApiUser_Redpackets();
									ur.setCompanyName(aResult.getResultData().get(0).getCompanyname());
									view.addObject("ur", ur);
								}
							}
							
						}else {//已领取
							ApiPage<ApiUser_Redpackets> aResult = redPacketsFacade.queryUserRedpacketsByCondition(apiUR, 1,10);
							if(aResult.getResultData().size() > 0 ){
								view.addObject("ur", aResult.getResultData().get(0));
							}
						}
						
					}
					//获取随机捐赠的项目
					ApiProject ap = new ApiProject();
					ap.setState(240);//募捐中
					ap.setIsHide(0);
					ApiPage<ApiProject> apiPage = projectFacade.queryProjectListNew(ap, 1, 20);
					int random = (int) (Math.random()*19);
					ApiProject project = apiPage.getResultData().get(random);
					if(StringUtils.isEmpty(project.getCoverImageUrl())){
						project.setCoverImageUrl("http://www.17xs.org/res/images/logo-def.jpg");
					}
					view.addObject("p", project);
					
				}
		if(user != null)
		{
			view.addObject("nickName",user.getNickName());
		}
		view.addObject("redpacketsId",id);
		view.addObject("type",type);
		return view;
	}
	
	
	
	/**
	 * 红包详情页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="sendRedPaper")
	public ModelAndView sendRedPaper(@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response,
			   @RequestParam(value="redpacketsId",required=false) Integer redpacketsId)
	{
		ModelAndView view = new ModelAndView("h5/redPackets/sendRedPaper");
		if(id != null){
			ApiUser_Redpackets ur = redPacketsFacade.queryUserRedpacketsById(id);
			view.addObject("ur", ur);
		}
		
		//获取随机捐赠的项目
		ApiProject ap = new ApiProject();
		ap.setState(240);//募捐中
		ap.setIsHide(0);
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectListNew(ap, 1, 20);
		int random = (int) (Math.random()*19);
		ApiProject project = apiPage.getResultData().get(random);
		if(StringUtils.isEmpty(project.getCoverImageUrl())){
			project.setCoverImageUrl("http://www.17xs.org/res/images/logo-def.jpg");
		}
		view.addObject("p", project);
		view.addObject("redpacketsId",redpacketsId);
		
		return view;
	}
	/**
	 * 查看红包详情朋友手气列表
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @param status
	 * @return
	 */
	@RequestMapping("redPaperList")
    @ResponseBody
    public Map<String, Object> redPaperList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "extensionpeople", required = false) Integer extensionpeople,
        @RequestParam(value = "status", required = false ,defaultValue = "401") Integer status,
        @RequestParam(value="spare",required=false) String spare,
        @RequestParam(value="redpacketsId",required=false) Integer redpacketsId){
		
        
        // 分页计算
        Page p = new Page();
        p.setPageNum(pageNum);
        p.setPage(page);
        
        ApiPage<ApiUser_Redpackets> data =null;
        ApiUser_Redpackets ur = new ApiUser_Redpackets();
        
        ur.setExtensionpeople(extensionpeople);
        if(!StringUtils.isEmpty(spare))
        {
        	ur.setSpare(spare);
        }
        if(redpacketsId != null)
        {
        	ur.setRedpackets_id(redpacketsId);
        }
      
        data = redPacketsFacade.queryByRedPaperList(ur, page, pageNum);
    
        if (data != null)
        {
        	SimpleDateFormat sm = new SimpleDateFormat("MM-dd");
    		for(ApiUser_Redpackets ar:data.getResultData()){
    			if(ar.getCreatetime() != null){
    				ar.setShowDate(sm.format(ar.getCreatetime()));
    			}
    		}
        	p.setData(data.getResultData());
        	p.setNums(data.getTotal());
        	
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "朋友手气列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
	
	/**
	 * 红包支付
	 * 
	 * @param uredId
	 *            用户红包id
	 * 
	 */
	@RequestMapping(value = "/payRedpacket")
	@ResponseBody
	public Map<String, Object> redPackets(
			@RequestParam(value = "uredId", required = true) Integer uredId,
			@RequestParam(value = "projectId", required = true) Integer projectId,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		ApiProject project = projectFacade.queryProjectDetail(projectId);
		//查询支付配置
		ApiPayConfig param = new ApiPayConfig();
		param.setUserId(project.getUserId());
		List<ApiPayConfig> list = projectFacade.queryByParam(param);
		ApiUser_Redpackets aup = redPacketsFacade
				.queryUserRedpacketsById(uredId);
		ApiDonateRecord dRecord = new ApiDonateRecord();
		String browser = UserUtil.Browser(request);
		if(project != null && browser.equals("wx")){
			ApiFrontUser user = userFacade.queryById(userId);
			if(list.size()>0){
				dRecord.setPartnerName(list.get(0).getName());
				dRecord.setPayConfigId(list.get(0).getId());
			}
			dRecord.setUserId(userId);
			dRecord.setProjectId(project.getId());
			dRecord.setProjectTitle(project.getTitle());
			dRecord.setMonthDonatId(123);// 未完成
			dRecord.setDonorType(user.getUserType()==null?"":user.getUserType());
			dRecord.setDonatType(user.getUserType()==null?"":user.getUserType());// 红包支付
			dRecord.setDonatAmount(aup.getAmount());
			dRecord.setDonateCopies(0);
			dRecord.setTranNum(StringUtil.uniqueCode());
			dRecord.setSource("H5");
			dRecord.setCompanyId(user.getCompanyId());
			ApiResult res = donateRecordFacade.buyDonate(dRecord, null, aup, "redpay");
			res.setData(dRecord.getTranNum());
			if(res.getCode() == 1){
				return webUtil.successRes(res);
			}else {
				return webUtil.failedRes("10113", "红包支付失败", null);
			}
			
		}
		return webUtil.failedRes("10113", "红包支付失败", null);
	}

	/**
	 * 用户的红包
	 * @param uredId
	 * 用户红包id
	 */
	@RequestMapping(value = "/queryRedpacket")
	@ResponseBody
	public Map<String, Object> queryUserRedpackets(HttpServletRequest request,HttpServletResponse response
			,@RequestParam(value="status",required=false,defaultValue="401") Integer status) {
		Integer userId = UserUtil.getUserId(request, response);
		// 在微信上可以使用
		String browser = UserUtil.Browser(request);
	
		if ( userId != null) {
			if (browser.equals("wx")) {
				ApiUser_Redpackets aur = new ApiUser_Redpackets();
				aur.setUserId(userId);
				aur.setStatus(status);// 待使用
				ApiPage<ApiUser_Redpackets> apiAur = redPacketsFacade
						.queryUserRedpacketsByCondition(aur, 1, 20);
				return webUtil.successRes(apiAur);
			} else {
				return webUtil.failedRes("10111", "不在微信的浏览器下", null);
			}
		}else {
			return webUtil.loginFailedRes(null);
		}
		
	}
	/**
	 * 获取用户协议
	 * @return
	 */
	@RequestMapping(value="getAgreement")
	public ModelAndView getAgreement(){
		ModelAndView view = new ModelAndView("h5/common/agreement");
		return view;
	}
	
	/**
	 * 个人红包发布
	 * @param request
	 * @param response
	 * @param redPackets
	 * @return
	 */
	@RequestMapping(value="addRedpackets")
	@ResponseBody
	public Map<String,Object> addRedpackets(HttpServletRequest request,HttpServletResponse response
			,ApiRedpackets redpackets)
	{
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}

		ApiFrontUser user = userFacade.queryById(userId);
		
		if(user.getBalance() < redpackets.getTotalamount())
		{
			return webUtil.resMsg(2, "0002", "余额不足", null);
		}
		
		//redpackets.setAdminUserid(userId);
		redpackets.setType(0);// 0 - 随机 ， 1 - 平均
		redpackets.setBtype(0);
		redpackets.setHavedays(7);
		redpackets.setUserid(userId);
		redpackets.setStatus(200);
		redpackets.setCreatetime(new Date());
		redpackets.setUsednum(0);
		redpackets.setBackamount(0.0);
		redpackets.setUsedamount(0.0);
		redpackets.setAppointproject("person");// 个人红包
		
		if(user != null)
		{
			redpackets.setCompanyname(user.getNickName());
		}
		
		ApiResult res = redPacketsFacade.saveRedpacket(redpackets);
		
		if(res.getCode() == 1)
		{
			return webUtil.successRes(res);
			
		}
		else 
		{
			return webUtil.failedRes("20000", "红包包装失败", null);
		}
		
	}
	
	
	@RequestMapping(value="sendRedPackets")
	public ModelAndView sendRedPackets(HttpServletResponse response,
			HttpServletRequest request,
			@RequestParam(value="rid",required=false) Integer rid) throws JDOMException, IOException
	{
		ModelAndView view = new ModelAndView("h5/redPackets/red_input");
		Integer userId = UserUtil.getUserId(request, response);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		
				String browser = UserUtil.Browser(request);
				//===========微信用户自动登陆start==============//
						String openId ="";
						String token = "";
						String unionid = "";
						String perfecturl = "" ;
						StringBuffer url = request.getRequestURL();
						perfecturl = url.toString();
							
				        if(userId == null || userId == 0)
				        {
				        	if(browser.equals("wx")){
				        		String weixin_code = request.getParameter("code");
				        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
				        		try {
					   				 Object OToken = redisService.queryObjectData("weixin_token");
					   				 token = (String)OToken;
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
				        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
				        			}
				        			user = CommonUtils.queryUser(request,openId,token,unionid);
				        		} catch (Exception e) {
				        			logger.error("H5RedPacketsController>>getPersonRedPaket>>"+ e);
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
				        		}
				        		catch(Exception e)
				        		{
				        			logger.error("",e);
				        		}
				        		
				        	}
				        	else
				        	{
				        		//to do >> 暂时跳转到登陆页
								view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
								return view;
				        		
				        	}
				        }
				        //===========微信用户自动登陆end==============//
				        CommonUtils.wxView(view, request, perfecturl);
			
		        user = userFacade.queryById(userId);
		    	if(user == null)
				{
					view = new ModelAndView("redirect:ucenter/user/Login_H5.do");
				}
				view.addObject("user",user);	
				view.addObject("redpacketsId",rid);
		      
		
		return view ; 
	}
	/**
	 * 我的红包
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="myRedPackets")
	public ModelAndView myRedPackets(HttpServletRequest request
			,HttpServletResponse response)
	{
		ModelAndView view = new ModelAndView("h5/redPackets/red_payment");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null)
		{
			view = new ModelAndView("redirect:/ucenter/user/Login_h5.do");
		}
		ApiRedpacketspool apiRedpacketspool = new ApiRedpacketspool();
		
		apiRedpacketspool.setUserId(userId);
		Integer sendCount = redPacketsFacade.countByCondition(apiRedpacketspool);
		
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(401);
		statusList.add(402);
		apiRedpacketspool.setStatusList(statusList);
		Integer getedCount = redPacketsFacade.countByCondition(apiRedpacketspool);
		
		view.addObject("sendCount",sendCount);
		view.addObject("getedCount",getedCount);
		
		ApiUser_Redpackets apiUserRedpackets = new ApiUser_Redpackets();
		apiUserRedpackets.setUserId(userId);
		apiUserRedpackets.setStatusList(null);
		Integer userGetedCount = redPacketsFacade.countRedPaperNumByCondition(apiUserRedpackets);
		
		view.addObject("userGetedCount",userGetedCount);
		
		apiUserRedpackets.setStatus(402);
		Integer usedGetedCount = redPacketsFacade.countRedPaperNumByCondition(apiUserRedpackets);
		
		view.addObject("usedGetedCount",usedGetedCount);
		
		return view;
	}

	
	@RequestMapping(value="mySendRedpackets")
	@ResponseBody
	public Map<String,Object> mySendRedpackets(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
	        @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
	        @RequestParam(value = "userId", required = false) Integer userId,
	        @RequestParam(value = "status", required = false ) Integer status)
	{
		Page p = new Page();
		p.setPage(page);
		p.setPageNum(pageNum);
		
		ApiRedpackets apiRedpackets = new ApiRedpackets();
		if(userId == null)
		{
			userId = UserUtil.getUserId(request, response);
		}
		if(userId == null)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		apiRedpackets.setUserid(userId);
		if(status != null)
		{
			apiRedpackets.setStatus(status);
		}
		ApiPage<ApiRedpackets> data =null;
		
		data = redPacketsFacade.queryByCondition(apiRedpackets, page, pageNum);
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(401);
		statusList.add(402);
        if (data != null)
        {
        	SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
    		for(ApiRedpackets ar:data.getResultData())
    		{
    			if(ar.getCreatetime() != null)
    			{
    				ar.setSpare(sm.format(ar.getCreatetime()));
  
    				ApiUser_Redpackets userRedpackets = new ApiUser_Redpackets();
    				userRedpackets.setRedpackets_id(ar.getId());
    				userRedpackets.setStatusList(statusList);
    				Integer getedCount = redPacketsFacade.countRedPaperNumByCondition(userRedpackets);
    				
    				ar.setUsednum(getedCount);
    			}
    		}
        	p.setData(data.getResultData());
        	p.setNums(data.getTotal());
        	
        }
        else
        {
            p.setNums(0);
        }
        
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "红包列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
        
	}
	
	@RequestMapping(value="receiveRedPackets")
	public ModelAndView receiveRedPackets(HttpServletRequest request ,
			HttpServletResponse response,@RequestParam(value="redpacketsId",
					required=true) Integer redpacketsId)
	{
		ModelAndView view = new ModelAndView("h5/redPackets/red_already");
		
		view.addObject("redpacketsId",redpacketsId);
		
		return view ;
	}


}
