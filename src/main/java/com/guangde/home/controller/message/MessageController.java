package com.guangde.home.controller.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ISystemNotifyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiNewLeaveWord;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiSystemNotify;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.message.PMessage;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping("message")
public class MessageController {
    
	private final Logger logger = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private ISystemNotifyFacade systemNotifyFacade;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private ICommonFacade CommonFacade;
	/**
	 * 当前用户的消息条数
	 * @param name
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 * 
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public Map<String,Object> list(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false,defaultValue="1") Integer type,
			@RequestParam(value = "page", required = false, defaultValue="1") Integer page,
			@RequestParam(value = "pageNum", required = false,defaultValue="10") Integer pageNum
			){
		//1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if(uid==null){
			return webUtil.resMsg(-1,"0001","未登入",null);
		}
		//处理时间
		Date bdate = null;
		Date edate = new Date();		
		if(type==1){
			//3个月内
			bdate = DateUtil.add(edate, -3*30);
		}else if(type==2){
			//半年
			bdate = DateUtil.add(edate, -6*30);
		}else if(type==3){
			//一年
			bdate = DateUtil.add(edate, -12*30);
		}else{
			//全部
			bdate = null;
			edate = null;
		}
		
		//调用获取消息列表的接口
		//todo
		ApiSystemNotify param = new ApiSystemNotify();
		param.setQueryEndDate(edate);
		param.setQueryStartDate(bdate);
		param.setUserId(new Integer(uid));
		ApiPage<ApiSystemNotify> rp = systemNotifyFacade.queryByCondition(param,page, pageNum);
		com.guangde.home.vo.common.Page  p = new com.guangde.home.vo.common.Page();
		List<PMessage> list = new ArrayList<PMessage>();
		p.setPage(page);
		p.setPageNum(pageNum);
		p.setData(list);
		PMessage msg = null;
		if(rp!=null){
			for(ApiSystemNotify notify:rp.getResultData()){
				msg = new PMessage();
				msg.setId(notify.getId());
				msg.setContent(notify.getContent());
				msg.setCreatTime(notify.getCreateTime());
				//msg.setFrom(01);
				msg.setSender(notify.getSender());
				msg.setTitle(notify.getSubject());
				msg.setTo(notify.getUserId());
				//msg.setType("0");
				msg.setStatus(notify.getState());
				list.add(msg);	
			}
			p.setNums(rp.getTotal());
		}else{
			p.setNums(0);
		}
		if(p.getTotal()==0){
			return webUtil.resMsg(2, "0002", "没有数据", p);
		}else{
			return webUtil.resMsg(1, "0000", "成功", p);
		}
	}
	
	/**
	 * 消息详情
	 * @param name
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "msgdetail")
	public ModelAndView detail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "type", required = false, defaultValue="current") String type 
			){
		 ModelAndView view = null;
		//1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if(uid==null){
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg","阅读消息请先登入");
			view.addObject("errorcode", -1);
			return view;
		}
		view = new ModelAndView("ucenter/msgDetial");
		if(id==null){
			view.addObject("errormsg","消息id不能为空");
			view.addObject("errorcode", 0);
			return view;
		}
		 
		view.addObject("userType", SSOUtil.getCurrentUserType(request, response));
		//获取根据type，获取是当前的消息，还是上一条，还是下一条
		Integer userId = new Integer(uid);
		ApiFrontUser user = userFacade.queryById(userId);
		 
		// 判断是否要显示我要求助的项目
		ApiProject apiProject = new ApiProject();
		apiProject.setUserId(userId);
		ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
				apiProject, 1, 9);
		if (apiPage.getTotal() > 0) {
			view.addObject("helpStatus", 1);
		}
		//显示善管家选项
		if (user.getLoveState() == 201 || user.getLoveState() == 203) {
			view.addObject("mylovegroupStatus", 1);
		}
		user = userFacade.queryPersonCenter(user);
		 view.addObject("user", user);
		ApiSystemNotify notify = null;
		if("next".equals(type)){
			notify = systemNotifyFacade.queryNextRecord(id, userId);
		}else if("pre".equals(type)){
			notify = systemNotifyFacade.queryPreviousRecord(id, userId);
		}else{
			notify = systemNotifyFacade.queryDetail(id, userId);
		}
		//调用消息详情接口
		if(notify!=null){
			ApiResult result = systemNotifyFacade.setToRead(notify.getId(),new Integer(uid));
			if(logger.isDebugEnabled()){
				logger.debug("将消息设为已读 msg: "+id+"result: "+result.getCode());
			}
			PMessage msg = new PMessage();
			msg.setId(notify.getId());
			msg.setContent(notify.getContent());
			msg.setCreatTime(notify.getCreateTime());
			//msg.setFrom(01);
			msg.setSender(notify.getSender());
			msg.setTitle(notify.getSubject());
			msg.setTo(notify.getUserId());
			//msg.setType("0");
			msg.setStatus(notify.getState());
			
			//处理是否有上一条，下一条
			ApiSystemNotify temp = null;
			if("next".equals(type)){
				temp = systemNotifyFacade.queryNextRecord(notify.getId(), userId);
				if(temp==null){
					msg.setNext(false);
				}
			}else if("pre".equals(type)){
				temp = systemNotifyFacade.queryPreviousRecord(notify.getId(), userId);
				if(temp==null){
					msg.setPre(false);
				}
			}else{
				temp = systemNotifyFacade.queryNextRecord(notify.getId(), userId);
				if(temp==null){
					msg.setNext(false);
				}
				temp = systemNotifyFacade.queryPreviousRecord(notify.getId(), userId);
				if(temp==null){
					msg.setPre(false);
				}
			}
			view.addObject("errormsg","成功");
			view.addObject("errorcode", 1);
			view.addObject("data", msg);
			return view;
		}else{
			view.addObject("errormsg","没有对应消息");
			view.addObject("errorcode", 0);
			return view;
		}
	}
	
	
	
	/**
	 * 删除消息
	 * @param name
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dmsg")
	@ResponseBody
	public Map<String,Object> dmsg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer id){
		//1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		if(uid==null){
			return webUtil.resMsg(-1,"0001","未登入",null);
		}
		if(id==null){
			return webUtil.resMsg(0,"0002","消息id不能为空",null);
		}
		//调用删除消息接口（验证是当前用户）
		//todo加uid参数
		ApiResult result = systemNotifyFacade.delete(id,new Integer(uid));
		if(result.getCode()==1){
			return webUtil.resMsg(1, "0000", "成功", null);
		}else{
			return webUtil.resMsg(0, "0001", "失败", null);
		}
	}
	
	/**
	 * 删除并跳到下一个消息
	 * @param name
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dmsgandnext")
	public ModelAndView dmsgandnext(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer id){
		//1.验证是否登入
		String uid = SSOUtil.verifyAuth(request, response);
		 ModelAndView view = null;
		if(uid==null){
			view = new ModelAndView("redirect:/user/sso/login.do");
			view.addObject("errormsg","阅读消息请先登入");
			view.addObject("errorcode", -1);
			return view;
		}
		if(id==null){
			view.addObject("errormsg","消息id不能为空");
			view.addObject("errorcode", 0);
			return view;
		}
		//调用删除消息接口（验证是当前用户）
		//todo加uid参数
		Integer userId = new Integer(uid);
		ApiResult result = systemNotifyFacade.delete(id,userId);
		if(result.getCode()==1){
			ApiSystemNotify  notify = systemNotifyFacade.queryNextRecord(id, new Integer(uid));
			view = new ModelAndView("ucenter/msgDetial");
			view.addObject("userType", SSOUtil.getCurrentUserType(request, response));
			ApiFrontUser user =userFacade.queryById(userId);
			// 判断是否要显示我要求助的项目
			ApiProject apiProject = new ApiProject();
			apiProject.setUserId(userId);
			ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(
					apiProject, 1, 9);
			if (apiPage.getTotal() > 0) {
				view.addObject("helpStatus", 1);
			}
			if(user.getLoveState() == 201 || user.getLoveState() == 203){
				view.addObject("mylovegroupStatus", 1);
			}
			user = userFacade.queryPersonCenter(user);
			view.addObject("user", user);
			if(notify!=null){
				ApiResult resulta = systemNotifyFacade.setToRead(notify.getId(),new Integer(uid));
				if(logger.isDebugEnabled()){
					logger.debug("将消息设为已读 msg: "+id+"result: "+resulta.getCode());
				}
				PMessage msg = new PMessage();
				msg.setId(notify.getId());
				msg.setContent(notify.getContent());
				msg.setCreatTime(notify.getCreateTime());
				//msg.setFrom(01);
				msg.setSender(notify.getSender());
				msg.setTitle(notify.getSubject());
				msg.setTo(notify.getUserId());
				//msg.setType("0");
				msg.setStatus(notify.getState());
				
				//处理是否有上一条，下一条
				ApiSystemNotify temp = null;
				temp = systemNotifyFacade.queryNextRecord(notify.getId(), new Integer(uid));
				if(temp==null){
					msg.setNext(false);
				}
				temp = systemNotifyFacade.queryPreviousRecord(notify.getId(), new Integer(uid));
				if(temp==null){
					msg.setPre(false);
				}
				
				view.addObject("errormsg","成功");
				view.addObject("errorcode", 1);
				view.addObject("data", msg);
				return view;
			}else{
				view.addObject("errormsg","没有对应消息");
				view.addObject("errorcode", 0);
				return view;
			}
		}else{
			view = new ModelAndView("redirect:/message/msgDetial.do?id"+id);
			return view;
		}
	}
	/**
	 * 发送消息
	 * @param name
	 * @param passWord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coredata/cmsg")
	@ResponseBody
	public Map<String,Object> createMsg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "receiver", required = false) Integer receiver
			){
		Integer userId = UserUtil.getUserId(request, response);
		if(userId==null){
			return webUtil.loginFailedRes(null);
		}
		if(StringUtils.isBlank(type)||receiver==null){
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG,"参数错误",null);
		}
		ApiSystemNotify apiSystemNotify  = null;
		if("eCertify".equals(type)){
			 apiSystemNotify = new ApiSystemNotify();
			apiSystemNotify.setState(0);
			apiSystemNotify.setSender("系统消息");
			apiSystemNotify.setSubject("企业认证");
			apiSystemNotify.setContent("你好，你在善基金的账号还未实名认证");
			apiSystemNotify.setCreateTime(new Date());
			apiSystemNotify.setIsShow(1);
			apiSystemNotify.setUserId(receiver);
		}
		if(apiSystemNotify!=null){
			 ApiResult  r = systemNotifyFacade.save(apiSystemNotify);
			 if(r.getCode()==1){
				 return webUtil.successRes(null);
			 }else{
				 return webUtil.failedRes(webUtil.ERROR_CODE_ADDFAILED, "发送失败", null); 
			 }
		}else{
			return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG,"未知类型", null);
		}		
	}
	
	/**
	 * 留言墙
	 * @param view
	 * @param request
	 * @param response
	 * @param projectId
	 * @return
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	@RequestMapping("messageWall_view")
	public ModelAndView messageWall_view(ModelAndView view,HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="projectId",required=true)Integer projectId,
			@RequestParam(value="show",required=false,defaultValue="1")String show) throws JDOMException, IOException{
		view = new ModelAndView("h5/messageWall/messageWall");
		Integer userId = UserUtil.getUserId(request, response);
		//==================微信登陆start================//
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();
        if(userId ==null)
        {
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("myShare >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
	   			
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
        				System.out.println("myShare >> CommonUtils.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        			userId = user.getId();
        		} catch (Exception e) {
        			logger.error("微信支付处理出现问题"+ e);
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
    			view = new ModelAndView("redirect:/ucenter/user/Login_H5.do");
    			return view ; 
        		
        	}
        }
		//==================微信登陆end================//
        if(userId==null){
        	userId=user.getId();
        }
        //轮播图
       /* Object obj = redisService.queryObjectData("TOGETHER_BANNER_LIST_"+projectId);
		if(obj != null){
			view.addObject("bannerList", obj);
		}else{
			List<ApiBFile> bfiles = null;
			ApiBFile apiBfile = new ApiBFile(); 
			apiBfile.setCategory("togetherBanner_"+projectId);
			bfiles = CommonFacade.queryApiBfile(apiBfile);
			view.addObject("bannerList", bfiles);
			redisService.saveObjectData("TOGETHER_BANNER_LIST_"+projectId, bfiles, DateUtil.DURATION_FIVE_S);
		}*/
        view.addObject("userId", userId);
        view.addObject("projectId", projectId);
        view.addObject("show", show);
        
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
			StringBuffer url2 = request.getRequestURL();
			String queryString2 = request.getQueryString();
			String perfecturl2 = url2 + "?" + queryString2;
			SortedMap<String, String> map = H5Demo.getConfigweixin(jsTicket,perfecturl);
			view.addObject("appId",map.get("appId"));
			view.addObject("timestamp",map.get("timeStamp"));
			view.addObject("noncestr",map.get("nonceStr"));
			view.addObject("signature",map.get("signature"));
		}
		return view;
	}
	
	/**
	 * 加载留言
	 * @param projectId
	 * @return
	 */
	@RequestMapping("loadMessageWallList")
	@ResponseBody
	public JSONObject messageWall_list(Integer projectId,
			//HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="page",required=true,defaultValue="1")Integer page,
			@RequestParam(value="pageNum",required=true,defaultValue="10")Integer pageNum){
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		//Integer userId = UserUtil.getUserId(request, response);
		try {
			ApiPage<ApiNewLeaveWord> apList =  projectFacade.queryMessageWall(projectId, page, pageNum);
			List<ApiNewLeaveWord> list = apList.getResultData();
	        if(list.size()==0){
	        	 //无数据
	        	 data.put("result", 1);
	         }
	         else {
				for(ApiNewLeaveWord newLeaveWord:list){
					 JSONObject item = new JSONObject();
					 item.put("id", newLeaveWord.getLeavewordUserId());
					 item.put("name", newLeaveWord.getLeavewordName());
					 item.put("text", newLeaveWord.getContent());
					 item.put("time", DateUtil.parseToFormatDateString(newLeaveWord.getCreateTime(),DateUtil.C_DATE_PATTON_DEFAULT));
					 item.put("donated", newLeaveWord.getReplyName()==null?0:newLeaveWord.getReplyName());
					 items.add(item);
				}
				data.put("result",0);
				data.put("total",apList.getTotal());
				data.put("items", items);
			}
	} catch (Exception e) {
		data.put("result", 2);
	}
	return data;
	}
}
