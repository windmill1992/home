package com.guangde.home.controller.user;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.api.user.IUserRelationInfoFacade;
import com.guangde.entry.*;
import com.guangde.home.utils.*;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.project.Donation;
import com.guangde.home.vo.project.UserInvoice;
import com.guangde.home.vo.user.UInvoice;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("user")
public class UserRelationInfoController {

	private final Logger logger = LoggerFactory.getLogger(UserRelationInfoController.class);

	@Autowired
	private IUserRelationInfoFacade userRelationInfoFacade;
	
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	
	@Autowired
	private IUserFacade userFacade;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private IFileFacade fileFacade ; 
	
	@Autowired
	private IProjectFacade projectFacade ; 
	@Autowired
	private ICommonFacade commonFacade;
	/**
	 * 礼品列表页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "giftList")
	public ModelAndView giftList(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/userRelation/giftList");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view ; 
		}
		
		return view;
	}
	
	/**
	 * 礼品列表页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "giftDetail")
	public ModelAndView giftDetail(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "id", required = false, defaultValue = "") Integer id) {
		ModelAndView view = new ModelAndView("h5/userRelation/giftDetail");
		
		String openId ="";
		String token = "";
		String unionid = "";
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		String perfecturl = url + "?" + queryString;
		String browser = UserUtil.Browser(request);
		ApiFrontUser user = new ApiFrontUser();//捐款用户
		ApiThirdUser tuser = null ;
		  //验证是否登入
        Integer  loginUserId = UserUtil.getUserId(request, response);
        if(loginUserId != null && loginUserId != 0)
        {
     	  user = userFacade.queryById(loginUserId);
     	  view.addObject("user", user);
        }
        else
        {
        	if(browser.equals("wx")){
        		String weixin_code = request.getParameter("code");
        		Map<String, Object> mapToken = new HashMap<String, Object>(8);
        		try {
	   				 Object OToken = redisService.queryObjectData("weixin_token");
	   				 token = (String)OToken;
	   				 System.out.println("giftDetail >> weixin_code = "+weixin_code + "  openId = "+openId+"  OToken = "+OToken);
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
        				System.out.println("giftDetail >> CommonUtils.getAccessTokenAndopenidRequest openId "+openId+" token = "+token);
        				redisService.saveObjectData("weixin_token", token, DateUtil.DURATION_HOUR_S);
        			}
        			user = CommonUtils.queryUser(request,openId,token,unionid);
        		} catch (Exception e) {
        			e.printStackTrace();
        			logger.error("giftDetail >> "+ e);
        		}
        		view.addObject("payway", browser);
        		
        		try
        		{
        			SSOUtil.login(user, request, response);
        		}
        		catch(Exception e)
        		{
        		
        			logger.error("giftDetail >> SSOUtil.login : ",e);
        		}
        	}
        	else
        	{
				view = new ModelAndView("redirect:/user/sso/login.do");
				return view ; 
        	}
        	
        }
	
		view.addObject("id",id);
		return view;
	}
	
	/**
	 * 索取发票
	 * 	填写地址页
	 * @return
	 */
	@RequestMapping(value = "toAddress")
	public ModelAndView toAddress(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/userRelation/invoiceAddress");
		//Integer userId = UserUtil.getUserId(request, response);
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
				view.addObject("flag", "toAddress");
				return view;
        		
        	}
        }else {
        	user = userFacade.queryById(userId);
		}
		//===========微信用户自动登陆end==============//
		//ApiFrontUser user = userFacade.queryById(userId);
		view.addObject("user", user);
		return view;
	}

	/**用户地址入库
	 * @param userAddress
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "addAdress")
	@ResponseBody
	public Map<String, Object> addAdress(@ModelAttribute ApiFrontUser_address userAddress,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		if(StringUtils.isEmpty(userAddress.getDetailAddress())){
			return webUtil.resMsg(0, "0007", "地址不能为空", null);
		}
		if(StringUtils.isEmpty(userAddress.getMobile())){
			return webUtil.resMsg(0, "0007", "联系电话不能为空", null);
		}
		if(StringUtils.isEmpty(userAddress.getName())){
			return webUtil.resMsg(0, "0007", "收件人不能为空", null);
		}
		userAddress.setCreateTime(new Date());
		userAddress.setLastUpdateTime(new Date());
		userAddress.setUserId(userId);
		userAddress.setIsSelected(1);
		ApiResult result = userRelationInfoFacade.saveUserAddress(userAddress);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "用户地址入库失败", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
	
	/**用户地址更新
	 * @param userAddress
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "editAdress")
	@ResponseBody
	public Map<String, Object> editAdress(@ModelAttribute ApiFrontUser_address userAddress,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}else if(userAddress.getId()==null){
			 return webUtil.resMsg(-1, "0002", "地址id为空", null);
		}
		if(StringUtils.isEmpty(userAddress.getDetailAddress())){
			return webUtil.resMsg(0, "0007", "地址不能为空", null);
		}
		if(StringUtils.isEmpty(userAddress.getMobile())){
			return webUtil.resMsg(0, "0007", "联系电话不能为空", null);
		}
		if(StringUtils.isEmpty(userAddress.getName())){
			return webUtil.resMsg(0, "0007", "收件人不能为空", null);
		}
		//userAddress.setCreateTime(new Date());
		userAddress.setLastUpdateTime(new Date());
		userAddress.setUserId(userId);
		ApiResult result = userRelationInfoFacade.updateUserAddress(userAddress);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "用户地址入库失败", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
	
	
	//删除地址
	@RequestMapping(value = "deleteAdress",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteAdress(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "addressIds", required = false) String addressIds) {
		String[] list=addressIds.split(",");
		List<Integer> list1 = new ArrayList<Integer>();
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}else{
			for (String string : list) {
				list1.add(Integer.valueOf(string));
			}
			ApiResult result = userRelationInfoFacade.deleteUserAddress(list1,userId);
			if (result != null && result.getCode() == 1){
				return webUtil.resMsg(1, "0000", "删除成功", null);
			}
			return webUtil.resMsg(2, "0002", "删除失败", null);
		}
	}
	
	/**
	 * 索取发票
	 * 	发票金额页
	 * @return
	 */
	@RequestMapping(value = "toInvoice")
	public ModelAndView toInvoice(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/userRelation/invoice");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view ; 
		}
		return view;
	}
	
	/**
	 * 索取发票
	 * 	发票详情页
	 * @return
	 */
	@RequestMapping(value = "detailInvoice")
	public ModelAndView toDetailInvoice(HttpServletRequest request,
			HttpServletResponse response,@RequestParam(value = "id", required = false, defaultValue = "") Integer id) {
		ModelAndView view = new ModelAndView("h5/userRelation/detailInvoice");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view ; 
		}
		view.addObject("id",id);
		return view;
	}
	
	/**
	 * 索取发票
	 * 	发票记录列表
	 * @return
	 */
	@RequestMapping(value = "invoiceRecordList")
	public ModelAndView invoiceRecordList(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView view = new ModelAndView("h5/userRelation/invoiceRecordList");
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view ; 
		}
		return view;
	}
	
	@RequestMapping(value = "addInvoice")
	@ResponseBody
	public Map<String, Object> addInvoice(@ModelAttribute ApiFrontUser_invoice userInvoice,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		if(userInvoice == null)
		{
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
		else
		{
			if(userInvoice.getInvoiceAmount() >= 100)
			{
				userInvoice.setIsFree(0);// 0 包邮  1 不包邮(到付)
			}
			else
			{
				userInvoice.setIsFree(1);
			}
		}
		if(StringUtils.isEmpty(userInvoice.getInvoiceHead())){
			return webUtil.resMsg(0, "0007", "发票抬头不能空", null);
		}
		if(userInvoice.getInvoiceAmount() == 0){
			return webUtil.resMsg(0, "0007", "发票金额不能为0", null);
		}
		userInvoice.setUserId(userId);
		userInvoice.setCreateTime(new Date());
		userInvoice.setLastUpdateTime(new Date());
		userInvoice.setState(300);
		ApiResult result = userRelationInfoFacade.saveUserInvoice(userInvoice);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "用户发票入库失败", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
	
	/**
	 * 发票记录列表
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @return
	 */
 
    @RequestMapping("invoiceList")
    @ResponseBody
    public Map<String, Object> invoiceList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId,@RequestParam(value = "state", required = false) Integer state)
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
        
        ApiFrontUser_invoice r = new ApiFrontUser_invoice();
        r.setUserId(userId);
        r.setState(state);
        ApiPage<ApiFrontUser_invoice> invoices = userRelationInfoFacade.queryInvoiceByCodition(r, p.getPage(), p.getPageNum());
        List<UInvoice> list = new ArrayList<UInvoice>();
        if (invoices != null)
        {
        	SimpleDateFormat sm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        	for(ApiFrontUser_invoice inv :invoices.getResultData() )
        	{
        		UInvoice  ui = new UInvoice();
        		ui.setAddressId(inv.getAddressId());
        		ui.setName(inv.getName());
        		ui.setMobile(inv.getMobile());
        		ui.setProvince(inv.getProvince());
        		ui.setCity(inv.getCity());
        		ui.setArea(inv.getArea());
        		ui.setDetailAddress(inv.getDetailAddress());
        		ui.setContent(inv.getContent());
        		ui.setCreateTime(sm.format(inv.getCreateTime()));
        		ui.setLastUpdateTime(sm.format(inv.getLastUpdateTime()));
        		ui.setId(inv.getId());
        		ui.setInvoiceAmount(inv.getInvoiceAmount());
        		ui.setInvoiceHead(inv.getInvoiceHead());
        		ui.setMailAmount(inv.getMailAmount());
        		ui.setMailCode(inv.getMailCode());
        		ui.setMailCompany(inv.getMailCompany());
        		ui.setIsFree(inv.getIsFree());
        		ui.setState(inv.getState());
        		ui.setUserId(inv.getUserId());
        		list.add(ui);
        	}
        	p.setData(list);
            p.setNums(invoices.getTotal());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "发票记录列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    

    @ResponseBody
    @RequestMapping("detailInvoiceInfo")
    public JSONObject detailInvoiceInfo(UserInvoice from)
    {
        JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
       
        try
        {
        	int id = from.getId();
         
        	ApiFrontUser_invoice userInvoice = userRelationInfoFacade.queryInvoiceById(id);
        	
            // 无数据
            if (userInvoice == null)
            {
                data.put("result", 1);
            }
            else
            {
            		ApiFrontUser_address  userAddress = null ;
            		if(userInvoice.getId() != null && userInvoice.getId() != 0)
            		{
            			userAddress = userRelationInfoFacade.queryById(userInvoice.getAddressId());
            			
            		}
                    JSONObject item = new JSONObject();
                    item.put("invoiceAmount", userInvoice.getInvoiceAmount());
                    item.put("content", userInvoice.getContent() == null ? "" : userInvoice.getContent());
                    item.put("isFree", userInvoice.getIsFree());
                    item.put("invoiceHead", userInvoice.getInvoiceHead());
                    item.put("mailAmount", userInvoice.getMailAmount()==null ? 0 : userInvoice.getMailAmount());
                    item.put("mailCompany", userInvoice.getMailCompany()==null ? "" :userInvoice.getMailCompany());
                    item.put("state", userInvoice.getState());
                    item.put("mailCode", userInvoice.getMailCode()== null ?"" : userInvoice.getMailCode());
                    SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String ctime = ""; 
                    if(userInvoice.getCreateTime() != null){
                    	ctime = sm.format(userInvoice.getCreateTime());
                    }
                    item.put("createTime",ctime);
                    String utime = ""; 
                    if(userInvoice.getCreateTime() != null){
                    	utime = sm.format(userInvoice.getLastUpdateTime());
                    }
                    item.put("lastUpdateTime",utime);
                   
                    item.put("info", userInvoice.getInfo());
                    if(userAddress != null)
                    {
                    	  item.put("name", userAddress.getName() == null ? "": userAddress.getName());
                          item.put("mobile",userAddress.getMobile()==null?"":userAddress.getMobile());
                          item.put("detailAddress", userAddress.getDetailAddress()==null?"":userAddress.getDetailAddress());
                          item.put("province", userAddress.getProvince());
                          item.put("city", userAddress.getCity());
                          item.put("area", userAddress.getArea());
                    }
                    if(!StringUtils.isEmpty(userInvoice.getInfo()))
                    {
                    	List<Integer> idList = new ArrayList<Integer>();
                    	try
                    	{
                    		
                    		String [] ary = userInvoice.getInfo().split("_");
                    		if(ary != null && ary.length>0)
                    		{
                    			for(int i = 0 ;i<ary.length;i++)
                    			{
                    				idList.add(Integer.parseInt(ary[i]));
                    			}
                    		}
                    	}
                    	catch(Exception e)
                    	{
                    		logger.error("userInvoice.getInfo >> ",e);
                    	}
                    	
                    	if(idList!=null && idList.size()>0)
                    	{
                    		
                    		ApiDonateRecord  dr = new ApiDonateRecord();
                    		dr.setIdList(idList);
                    		List<ApiDonateRecord> drList = donateRecordFacade.queryByIdList(dr);
                    		if(drList != null && drList.size()>0)
                    		{
                    			JSONObject itemDr = null;
                    			for(ApiDonateRecord d : drList)
                    			{
                    				itemDr = new JSONObject();
                    				itemDr.put("title", d.getProjectTitle());
                    				itemDr.put("donateAmount", d.getDonatAmount());
                    				items.add(itemDr);
                    			}
                    		}
                    	}
                    	
                    	
                    }
                    data.put("item", item);
                    data.put("donateList", items);
                    data.put("result", 0);
            }
        }
        catch (Exception e)
        {
            // 后台服务发生异常
            e.printStackTrace();
            data.put("result", 2);
            return data;
        }
        
        return data;
    }
    /**
     * 礼品详情
     * @param from
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("detailGiftInfo")
    public JSONObject detailGiftInfo(UserInvoice from,HttpServletRequest request, HttpServletResponse response)
    {
        JSONObject data = new JSONObject();
      
        try
        {
        	int id = from.getId();
         
        	ApiGift gift = userRelationInfoFacade.queryGiftById(id);
        	
            // 无数据
            if (gift == null)
            {
                data.put("result", 1);
            }
            else
            {
                 
              
            		SimpleDateFormat sm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    JSONObject item = new JSONObject();
                    item.put("id", id);// 礼品id
                    item.put("giftName", gift.getGiftName() == null ? "" : gift.getGiftName());// 礼品名称
                    item.put("content", gift.getContent() == null ? "" : gift.getContent());// 礼品描述信息
                    item.put("isUnder", gift.getIsUnder() == null ? "" : gift.getIsUnder());// 是否下架
                    item.put("score", gift.getScore() == null ? "0" : gift.getScore());// 所需积分
                    item.put("sendType", gift.getSendType() == null ? "" : gift.getSendType());// 发货方式
                    item.put("number", gift.getNumber() == null ? "0" : gift.getNumber());// 每人可领取的分数
                    item.put("copies", gift.getCopies() == null ? "0" : gift.getCopies());// 总份数
                    item.put("leaveCopies", gift.getLeaverCopies() == null ? "0" : gift.getLeaverCopies());// 剩余份数
                    item.put("imageUrl", gift.getCoverImageUrl() == null ? "" : gift.getCoverImageUrl());// 图片地址
                    if(null != gift.getEndTime())
                    {
                    	String time = sm.format(gift.getEndTime());
                    	item.put("endTime", time);// 截止时间
                    }
                    // 是否可领取
                    // 1.验证是否登入
                    Integer userId = UserUtil.getUserId(request, response);
                     if (userId == null)
                     {
                    	 item.put("isShow", 1);// 1 不可领取
                     }
                     else
                     {
                    	 ApiFrontUser_giftRecord apiGiftRecord = new ApiFrontUser_giftRecord();
                    	 apiGiftRecord.setGiftId(gift.getId());
                    	 apiGiftRecord.setUserId(userId);
                    	 Integer num = userRelationInfoFacade.countGiftRecordByCodition(apiGiftRecord);
                    	 if(num >= gift.getNumber()){
                    		 item.put("isShow", 1);
                    	 }
                    	 else
                    	 {
                    		 item.put("isShow", 0);// 可领取
                    	 }
                     }
                  
                    data.put("item", item);
                    data.put("result", 0);
            }
        }
        catch (Exception e)
        {
            // 后台服务发生异常
            e.printStackTrace();
            data.put("result", 2);
            return data;
        }
        
        return data;
    }
    
    /**
     * 地址详情
     * @param from
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("detailAddress")
    public JSONObject detailAddress(UserInvoice from,HttpServletRequest request, HttpServletResponse response)
    {
        JSONObject data = new JSONObject();
      
        try
        {
        	int id = from.getId();
         
        	ApiFrontUser_address userAddress = userRelationInfoFacade.queryById(id);
        	
            // 无数据
            if (userAddress == null)
            {
                data.put("result", 1);
            }
            else
            {
                    JSONObject item = new JSONObject();
                    
                    item.put("name", userAddress.getName() == null ? "" : userAddress.getName());// 收件人
                    item.put("mobile", userAddress.getMobile() == null ? "" : userAddress.getMobile());// 联系号码
                    item.put("province", userAddress.getProvince() == null ? "" : userAddress.getProvince());// 省
                    item.put("city", userAddress.getCity() == null ? "" : userAddress.getCity());// 市
                    item.put("area", userAddress.getArea() == null ? "" : userAddress.getArea());// 区
                    item.put("detailAddress", userAddress.getDetailAddress() == null ? "" : userAddress.getDetailAddress());// 每人可领取的分数
                    item.put("email", userAddress.getEmail() == null ? "0" : userAddress.getEmail());// 
                    item.put("code", userAddress.getCode()== null ? "":userAddress.getCode());
                    item.put("isSelected", userAddress.getIsSelected()== null ? "":userAddress.getIsSelected());
                    data.put("item", item);
                    data.put("result", 0);
            }
        }
        catch (Exception e)
        {
            // 后台服务发生异常
            e.printStackTrace();
            data.put("result", 2);
            return data;
        }
        
        return data;
    }
    
    
	/**
	 * 用户地址列表
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @return
	 */
    @RequestMapping("userAddressList")
    @ResponseBody
    public Map<String, Object> userAddressList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId)
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
        
        ApiFrontUser_address r = new ApiFrontUser_address();
        r.setUserId(userId);
       // r.setState(302);
        List<ApiFrontUser_address> addresses = userRelationInfoFacade.queryUserAddress(r, page, pageNum);
      //  List<ApiFrontUser_address> addresses = ret.getResultData();
    
        if (addresses != null)
        {
        	p.setData(addresses);
            p.setNums(addresses.size());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(1, "0002", "用户地址列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    
    
    /**
     * 我的捐助
     * @param request
     * @param response
     * @param type
     * @param page
     * @param pageNum
     * @param userId
     * @return
     */
    @RequestMapping("donationlist_invoice")
    @ResponseBody
    public Map<String, Object> donationlist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "type", required = false, defaultValue = "0") Integer type,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId)
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
        List<Donation> list = new ArrayList<Donation>();
        p.setData(list);
        
        ApiDonateRecord r = new ApiDonateRecord();
        r.setUserId(userId);
        r.setState(302);
        ApiFrontUser_invoice inv = new ApiFrontUser_invoice();
        inv.setUserId(userId);
        ApiPage<ApiFrontUser_invoice> invoices = userRelationInfoFacade.queryInvoiceByCodition(inv, 0, 51);
        List<Integer> idList = new ArrayList<Integer>();
        try
        {
        	if(invoices.getResultData() != null && invoices.getResultData().size()>0)
        	{
        		for(ApiFrontUser_invoice invo : invoices.getResultData())
        		{
        			if(!StringUtils.isEmpty(invo.getInfo()))
        			{
        				String[] idary = invo.getInfo().split("_");
        				if(idary != null && idary.length>0)
        				{
        					for(int i = 0 ; i<idary.length;i++)
        					{
        						
        						idList.add(Integer.parseInt(idary[i]));
        					}
        				}
        			}
        		}
        	}
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	logger.error("idList >> ", e);
        }
        
        if(idList != null && idList.size()>0)
        {
        	r.setIdList(idList);
        }

        ApiPage<ApiDonateRecord> donats = donateRecordFacade.queryDonateListByInvoice(r, p.getPage(), p.getPageNum());
        Donation d = null;
        if (donats != null)
        {
            for (ApiDonateRecord donat : donats.getResultData())
            {
            	d = new Donation();
                d.setdMoney(donat.getDonatAmount()); //捐款金额
                d.setId(donat.getId());    
                d.setPid(donat.getProjectId());  //项目id
                d.setdTime(donat.getDonatTime()); //捐款时间
                d.setTitle(donat.getProjectTitle()); //项目名称
                d.setTranNum(donat.getTranNum()); //订单号
                d.setField(donat.getField());
          
                list.add(d);
            }
            p.setNums(donats.getTotal());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "捐款列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    
    

    @ResponseBody
    @RequestMapping("userInfo")
    public JSONObject userInfo(HttpServletRequest request, HttpServletResponse response)
    {
        JSONObject data = new JSONObject();
       
        try
        {
        	
        	Integer userId = UserUtil.getUserId(request, response);
    		if(userId == null || userId == 0){
    			 data.put("result", 1);
    		}
    		else
    		{
    			ApiFrontUser user = userFacade.queryById(userId);
    		     
            	
                // 无数据
                if (user == null)
                {
                    data.put("result", 1);
                }
                else
                {
                	
                        JSONObject item = new JSONObject();
                        item.put("userName", user.getUserName());
                        item.put("nickName", user.getNickName());
                        item.put("headImageUrl", user.getCoverImageUrl());
                        item.put("totalAmount", user.getTotalAmount());
                        data.put("item", item);
                        data.put("result", 0);
                }
    		}
       
   
        }
        catch (Exception e)
        {
            // 后台服务发生异常
            e.printStackTrace();
            data.put("result", 2);
            return data;
        }
        
        return data;
    }
    
    
    /**
     * 礼品
     * @param request
     * @param response
     * @param page
     * @param pageNum
     * @param userId
     * @param isUnder
     * @param type  0 ： 可兑换的礼品  ,  1 : 失效的礼品 , 2  : 已拥有的礼品
     * 	
     * @return
     */
    @RequestMapping("giftInfo")
    @ResponseBody
    public Map<String, Object> giftInfo(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId,
        @RequestParam(value = "isUnder", required = false ,defaultValue = "0") Integer isUnder,
        @RequestParam(value = "type", required = false ,defaultValue = "0") Integer type)
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
        
        ApiPage<ApiGift> giftList = null ; 
        ApiGift apiGift = new ApiGift();
       
        if(type == 0)
        {
        	apiGift.setIsUnder(0);
        	apiGift.setUserId(userId);
        	giftList = userRelationInfoFacade.queryGiftByCoditionNew(apiGift, page, pageNum);
        }
        else if(type == 1)
        {
        	apiGift.setIsUnder(1);
        	apiGift.setUserId(userId);
        	giftList = userRelationInfoFacade.queryGiftByCodition(apiGift, page, pageNum);
        }
        else if(type == 2)
        {
        	apiGift.setUserId(userId);
        	giftList = userRelationInfoFacade.queryHasGiftList(apiGift, page, pageNum);
        }
        
    
        if (giftList != null)
        {
        	
        	for(ApiGift g : giftList.getResultData())
        	{
        		if(null != g.getCoverImageUrl())
        		{
        			g.setCoverImageUrl("http://res.17xs.org/picture/"+g.getCoverImageUrl());
        		}
        
        	}
        	p.setData(giftList.getResultData());
        	p.setNums(giftList.getTotal());
        	/*
        	ApiFrontUser_giftRecord apiGiftRecord = null;
        	List<ApiGift> glist = new ArrayList<ApiGift>();
        	for(ApiGift g : giftList.getResultData())
        	{
        		if(null != g.getCoverImageUrl())
        		{
        			g.setCoverImageUrl("http://res.17xs.org/picture/"+g.getCoverImageUrl());
        		}
        		apiGiftRecord = new ApiFrontUser_giftRecord();
        		apiGiftRecord.setGiftId(g.getId());
            	apiGiftRecord.setUserId(userId);
        		Integer num = userRelationInfoFacade.countGiftRecordByCodition(apiGiftRecord);
        		if(num < g.getNumber()){
        			glist.add(g);
        		}
        		
        	}
        	p.setData(glist);
        	if(glist.size() == 0){
        		 p.setNums(0);
        	}
        	else
        	{
        		p.setNums(giftList.getTotal());
        	}
        	*/
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "礼品列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    
    /**
     * 礼品领取记录
     * @param request
     * @param response
     * @param page
     * @param pageNum
     * @param userId
     * @return
     */
    @RequestMapping("giftRecordList")
    @ResponseBody
    public Map<String, Object> giftRecordList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId)
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
        
        ApiFrontUser_giftRecord apiGiftRecord = new ApiFrontUser_giftRecord();
        apiGiftRecord.setUserId(userId);
        ApiPage<ApiFrontUser_giftRecord> giftRecordList = userRelationInfoFacade.queryGiftRecordByCodition(apiGiftRecord, page, pageNum);
    
        if (giftRecordList != null)
        {
        	for(ApiFrontUser_giftRecord gr : giftRecordList.getResultData())
        	{
        		if(null != gr.getGiftHeadImageUrl())
        		{
        			gr.setGiftHeadImageUrl("http://res.17xs.org/picture/"+gr.getGiftHeadImageUrl());
        		}
        	}
        	p.setData(giftRecordList.getResultData());
            p.setNums(giftRecordList.getTotal());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "礼品领取记录为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    
    /**
     * 统计用户对某种礼品领取的数量
     * @param from
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("countGiftRecord")
    public JSONObject countGiftRecord(UserInvoice from,HttpServletRequest request, HttpServletResponse response)
    {
        JSONObject data = new JSONObject();
       
        try
        {
        	  // 1.验证是否登入
           Integer userId = UserUtil.getUserId(request, response);
            if (userId == null)
            {
            	 data.put("result", 1);
            }
         
        	ApiFrontUser_giftRecord apiGiftRecord = new ApiFrontUser_giftRecord();
        	apiGiftRecord.setGiftId(from.getGiftId());
        	apiGiftRecord.setUserId(userId);
        	Integer num = userRelationInfoFacade.countGiftRecordByCodition(apiGiftRecord);
        	
            // 无数据
            if (num == null || num == 0)
            {
                data.put("result", 1);
            }
            else
            {
                    data.put("num", num);
                    data.put("result", 0);
            }
        }
        catch (Exception e)
        {
            // 后台服务发生异常
            e.printStackTrace();
            data.put("result", 2);
            return data;
        }
        
        return data;
    }
    
    /**
     * 用户礼品领取记录入库
     * @param giftRecord
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "addGiftRecord")
	@ResponseBody
	public Map<String, Object> addGiftRecord(@ModelAttribute ApiFrontUser_giftRecord giftRecord,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		giftRecord.setCreateTime(new Date());
		giftRecord.setLastUpdateTime(new Date());
		giftRecord.setUserId(userId);
		ApiResult result = userRelationInfoFacade.saveUserGiftRecord(giftRecord);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "礼品领取记录入库失败", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
	
	/**
	 * 用户礼品领取记录更新
	 * @param giftRecord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "editGiftRecord")
	@ResponseBody
	public Map<String, Object> editGiftRecord(@ModelAttribute ApiFrontUser_giftRecord giftRecord,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0){
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		giftRecord.setLastUpdateTime(new Date());
		giftRecord.setUserId(userId);
		ApiResult result = userRelationInfoFacade.updateUserGiftRecord(giftRecord);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "用户礼品领取记录更新失败", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
	
	/**
	 * 分享的项目
	 * @param request
	 * @param response
	 * @param page
	 * @param pageNum
	 * @param userId
	 * @return
	 */
    @RequestMapping("shareProjectList")
    @ResponseBody
    public Map<String, Object> shareProjectList(HttpServletRequest request, HttpServletResponse response,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
        @RequestParam(value = "userId", required = false) Integer userId)
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
        
        ApiProject r = new ApiProject();
        r.setUserId(userId);
        
        ApiPage<ApiProject> ret = projectFacade.queryShareProject(r, page, pageNum);
        
        List<ApiProject> pros = ret.getResultData();
    
        if (pros != null)
        {
        	p.setData(pros);
            p.setNums(pros.size());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(1, "0002", "分享项目列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    /**
     * 获取日捐月捐开通页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="getDonateTime")
    public ModelAndView getDonateTime(HttpServletRequest request,HttpServletResponse response){
    	
    	ModelAndView view = new ModelAndView("h5/userRelation/monthly_donate");
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId == null || userId == 0){
	    	String browser = UserUtil.Browser(request);
			String openId ="";
			String Token = "";
			String unionid = "";
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			if(browser.equals("wx")){
				String weixin_code = request.getParameter("code");
				Map<String, Object> mapToken = new HashMap<String, Object>(8);
				try {
					if ("".equals(openId) || openId == null) {
						if ("".equals(weixin_code) || weixin_code == null
								|| weixin_code.equals("authdeny")) {
							String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
							view = new ModelAndView("redirect:" + url_weixin_code);
							return view;
						}
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
						openId = mapToken.get("openid").toString();
						Token = mapToken.get("access_token").toString();
						unionid = mapToken.get("unionid").toString();
						redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
					}
				} catch (Exception e) {
					logger.error("微信支付处理出现问题"+ e);
				}
				
				 ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
				 try
				 {
					 // 自动登录
					 SSOUtil.login(user, request, response);
				 }
				 catch(Exception e)
				 {
					 logger.error("weixindeposit2 >> SSOUtil.login : "+e);
				 }
				 view.addObject("user",user);
			}else {
				view = new ModelAndView("redirect:/ucenter/user/Login_H5.do?flag=monthlyDonate");
				return view ; 
			}
		}else {
			ApiFrontUser user = userFacade.queryById(userId);
			view.addObject("user", user);
		}
		List<ApiTypeConfig> atc = commonFacade.queryList();
        view.addObject("atc", atc);
		return view;
    }
    /**
     * 我的日捐月捐
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getMyDonateTime")
    public ModelAndView getMyDonateTime(HttpServletRequest request, HttpServletResponse response){
    	ModelAndView view = new ModelAndView("h5/userRelation/my_monthly_donate");
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId == null || userId == 0){
	    	String browser = UserUtil.Browser(request);
			String openId ="";
			String Token = "";
			String unionid = "";
			StringBuffer url = request.getRequestURL();
			String queryString = request.getQueryString();
			String perfecturl = url + "?" + queryString;
			if(browser.equals("wx")){
				String weixin_code = request.getParameter("code");
				Map<String, Object> mapToken = new HashMap<String, Object>(8);
				try {
					if ("".equals(openId) || openId == null) {
						if ("".equals(weixin_code) || weixin_code == null
								|| weixin_code.equals("authdeny")) {
							String url_weixin_code = H5Demo.getCodeRequest(perfecturl);
							view = new ModelAndView("redirect:" + url_weixin_code);
							return view;
						}
						mapToken = CommonUtils.getAccessTokenAndopenidRequest(weixin_code);
						openId = mapToken.get("openid").toString();
						Token = mapToken.get("access_token").toString();
						unionid = mapToken.get("unionid").toString();
						redisService.saveObjectData("weixin_token", Token, DateUtil.DURATION_HOUR_S);
					}
				} catch (Exception e) {
					logger.error("微信登录处理出现问题"+ e);
				}
				
				 ApiFrontUser user = CommonUtils.queryUser(request,openId,Token,unionid);
				 try
				 {
					 // 自动登录
					 SSOUtil.login(user, request, response);
				 }
				 catch(Exception e)
				 {
					 logger.error("weixindeposit2 >> SSOUtil.login : "+e);
				 }
				 view.addObject("user",user);
			}else {
				view = new ModelAndView("redirect:/ucenter/user/Login_H5.do?flag=myMonthlyDonate");
				return view ; 
			}
		}else {
			ApiFrontUser user = userFacade.queryById(userId);
			view.addObject("user", user);
		}
    	
    	return view;
    }
    /**
     * 返回我的日捐月捐列表
     * @param request
     * @param response
     * @param page
     * @param pageNum
     * @return
     */
    @RequestMapping("myDonateTime")
    @ResponseBody
    public Map<String, Object> myDonateTime(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
    		@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
    		@RequestParam(value = "userId", required = false) Integer userId){
    	
    	if(userId == null) {
    		userId = UserUtil.getUserId(request, response);
    		if(userId == null ) {
    			return webUtil.loginFailedRes(null);
    		}
    	}
    	Page p = new Page();
    	p.setPage(page);
    	p.setPageNum(pageNum);
    	
    	ApiPage<ApiDonateTime> list = donateRecordFacade.queryByUserId(userId, page, pageNum);
    	
    	if(list != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		ApiTypeConfig tc = new ApiTypeConfig();
    		for (ApiDonateTime dt : list.getResultData()) {
    			List<String> cs = new ArrayList<String>();
    			if(dt.getProjectIds()!=null && !"".equals(dt.getProjectIds()) && dt.getProjectIds().split(",").length==1){
    				ApiProject project = projectFacade.queryProjectDetail(Integer.valueOf(dt.getProjectIds()));
    				cs.add(project.getTitle());
    			}
    			else{
				if(StringUtils.isNotEmpty(dt.getCategory())) {
					String[] categorys = dt.getCategory().split(",");
					for(String str:categorys){
						if(StringUtils.isNotEmpty(str)){
							tc.setTypeName_e(str);
							ApiTypeConfig atc = commonFacade.queryApiTypeConfig(tc);
							if (atc != null) {
								cs.add(atc.getTypeName());
							}
						}
					}
				}}
				dt.setCategorys(cs);
				dt.setCTime(sdf.format(dt.getCreatetime()));
			}
    		p.setData(list.getResultData());
    		p.setNums(list.getTotal());
    	}else {
			p.setNums(0);
		}
    	if(p.getTotal() == 0) {
    		return webUtil.resMsg(2, "0002", "记录为空", p);
    	}else {
			return webUtil.resMsg(1, "0000", "成功", p);
		}
    }
    /**
     * 日捐月捐发起时入库
     * @param dt
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "addDonateTime")
	@ResponseBody
	public Map<String, Object> addDonateTime(@ModelAttribute ApiDonateTime dt,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		if(dt == null)
		{
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
		ApiFrontUser user = userFacade.queryById(userId); 
		
		dt.setUserId(userId);
		if(dt.getId()!=null){
			ApiDonateTime time = new ApiDonateTime();
			time.setId(dt.getId());
			ApiPage<ApiDonateTime> page = donateRecordFacade.queryDonateTimeByParam(time, 1, 1);
			dt=page.getResultData().get(0);
		}
		if(dt.getMoney() > user.getBalance()) {
			dt.setState(200);
			ApiResult result = donateRecordFacade.saveDonateTime(dt);
			Map<String,Object> r = webUtil.failedRes("0007", "您的余额不足！", result.getData());
			return r;
		}
		dt.setState(201);
		if(dt.getDayNumber() == null){
			dt.setDayNumber(-1);//无到期天数
		}
		ApiResult result = donateRecordFacade.saveDonateTime(dt);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "日捐月捐发起失败！", null);
		}
		else
		{
			if(dt.getNotice() == 0) { //选择短信通知时
				//发起月捐成功短信通知
				ApiAnnounce apiAnnounce = new ApiAnnounce();
				if(dt.getType() == 0) { //日捐
					apiAnnounce.setCause("日捐通知");
					apiAnnounce.setContent(user.getNickName()+"，感谢您对公益事业的支持，您的日捐计划已经开始执行，每次捐助情况"
			    			+"将通过短信通知您，您还可以关注善园基金会公众号了解项目执行进展。");
				}else {
					apiAnnounce.setCause("月捐通知");
					apiAnnounce.setContent(user.getNickName()+"，感谢您对公益事业的支持，您的月捐计划已经开始执行，每次捐助情况"
			    			+"将通过短信通知您，您还可以关注善园基金会公众号了解项目执行进展。");
				}
		    	apiAnnounce.setDestination(dt.getMobileNum());
		    	apiAnnounce.setType(1);
		    	apiAnnounce.setPriority(1);
		    	commonFacade.sendSms(apiAnnounce, false);
			}
			
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
    
    /**
     * 新版日捐月捐发起时入库
     * @param dt
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "addDonateNewTime")
	@ResponseBody
	public Map<String, Object> addDonateNewTime(@ModelAttribute ApiDonateTime dt,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		if(dt == null)
		{
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
		ApiFrontUser user = userFacade.queryById(userId); 
		
		dt.setUserId(userId);
		if(MathUtil.mul(dt.getMoney(), dt.getDayNumber()) > user.getBalance()) {
			dt.setState(200);
			ApiResult result = donateRecordFacade.saveDonateTime(dt);
			Map<String,Object> r = webUtil.failedRes("0007", "您的余额不足！", result.getData());
			return r;
		}
		dt.setState(201);
		ApiResult result = donateRecordFacade.saveDonateTime(dt);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "日捐月捐发起失败！", null);
		}
		else
		{
			if(dt.getNotice() == 0) { //选择短信通知时
				//发起月捐成功短信通知
				ApiAnnounce apiAnnounce = new ApiAnnounce();
				if(dt.getType() == 0) { //日捐
					apiAnnounce.setCause("日捐通知");
					apiAnnounce.setContent(user.getNickName()+"，您的日捐已开通，每日捐赠"+dt.getMoney()+"元，持续"+dt.getDayNumber()+"天，感谢您的善举");
				}else {
					apiAnnounce.setCause("月捐通知");
					apiAnnounce.setContent(user.getNickName()+"，感谢您对公益事业的支持，您的月捐计划已经开始执行，每次捐助情况"
			    			+"将通过短信通知您，您还可以关注善园基金会公众号了解项目执行进展。");
				}
		    	apiAnnounce.setDestination(dt.getMobileNum());
		    	apiAnnounce.setType(1);
		    	apiAnnounce.setPriority(1);
		    	commonFacade.sendSms(apiAnnounce, false);
			}
			
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
    
    /**
     * 更新日捐月捐
     * @param dt
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "updateDonateTime")
    @ResponseBody
    public Map<String, Object> updateDonateTime(@ModelAttribute ApiDonateTime dt,
			HttpServletRequest request, HttpServletResponse response){
    	
    	Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		if(dt == null)
		{
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
		ApiResult result = donateRecordFacade.updateDonateTime(dt);
		if(result.getCode() != 1){
			return webUtil.resMsg(0, "0007", "停止月捐失败！", null);
		}else {
			Map<String,Object> r = webUtil.successRes(null);
			return r;
		}
    	
    }
    
    /**
     * 众筹项目用户奖励记录入库
     * @param dt
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "addUserProjectPrize")
	@ResponseBody
	public Map<String, Object> addUserProjectPrize(@ModelAttribute ApiFrontUserProjectPrize apiupPrize,
			HttpServletRequest request, HttpServletResponse response) {
		Integer userId = UserUtil.getUserId(request, response);
		if(userId == null || userId == 0)
		{
			 return webUtil.resMsg(-1, "0001", "未登入", null);
		}
		if(apiupPrize == null)
		{
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
		apiupPrize.setUserId(userId);
		apiupPrize.setState(200);//未支付
		apiupPrize.setOrderNum(StringUtil.uniqueCode());
		apiupPrize.setCreateTime(DateUtil.getCurrentTimeByDate());
		apiupPrize.setDonateTime(DateUtil.getCurrentTimeByDate());
		ApiResult result = userRelationInfoFacade.saveUserProjectPrize(apiupPrize);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "商品众筹入库失败！", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			r.put("obj", apiupPrize.getOrderNum());
			return  r ;
		}
	}
	
}
