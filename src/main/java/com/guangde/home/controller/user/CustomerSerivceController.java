package com.guangde.home.controller.user;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.AbstractDocument.Content;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.homepage.ICustomerServiceFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiCustomerService;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiServiceLeaveWord;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.message.PNews;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;


@Controller
@RequestMapping("user")
public class CustomerSerivceController {

	private final Logger logger = LoggerFactory.getLogger(CustomerSerivceController.class);
	private static final String imgcodeprex = "imgcode_r_";

	@Autowired
	private ICustomerServiceFacade customerServiceFacade;
	@Autowired
	private IUserFacade userFacade;
	/**
	 * 咨询、建议、投诉
	 * @param customerService
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "addCustomerService")
	@ResponseBody
	public Map<String, Object> addCustomerService(@ModelAttribute ApiCustomerService customerService,
			@RequestParam(value="flag",required=false)String flag,
			HttpServletRequest request, HttpServletResponse response) {
	
		logger.info(" addCustomerService param : "+customerService);
		if(customerService == null)
		{
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
		if(flag == null){
			// 校验图片验证码
			String codekey = getImgCode(request);
			//可以替换为其他的存储方式
			StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
			int codeR = CodeUtil.VerifiCode(codekey,storeManage,customerService.getCode(),true);
			if (codeR == -1) {
				return webUtil.resMsg(0, "0005", "验证码过期", null);
			} else if (codeR == 0) {
				return webUtil.resMsg(0, "0005", "验证码错误", null);
			}
		}
		ApiResult result = customerServiceFacade.saveCustomerService(customerService);
		if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "信息入库失败", null);
		}
		else
		{
			Map<String,Object> r = webUtil.successRes(null);
			return  r ;
		}
	}
	/**
	 * 评论
	 * @param sLeaveWord
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="addServiceLeaveWord")
	@ResponseBody
	public Map<String, Object> addServiceLeaveWord(@ModelAttribute ApiServiceLeaveWord sLeaveWord,
			HttpServletRequest request,HttpServletResponse response){
		logger.info("addServiceLeaveWord param is "+sLeaveWord);
		if(sLeaveWord == null){
			return webUtil.resMsg(-1, "0002", "参数错误", null);
		}
		if(StringUtils.isEmpty(sLeaveWord.getReply())){
			return webUtil.resMsg(0, "0005", "评论内容不能为空", null);
		}
		ApiResult result = customerServiceFacade.saveServiceLeaveWord(sLeaveWord);
		if(result.getCode() != 1){
			return webUtil.resMsg(0, "0007", "信息入库失败", null);
		}else {
			ApiCustomerService apiCustomerService = new ApiCustomerService();
			apiCustomerService.setId(sLeaveWord.getServiceId());
			apiCustomerService.setState(300);
			customerServiceFacade.updateCustomerService(apiCustomerService);
			Map<String, Object> r = webUtil.successRes(null);
			return r;
		}
		
	}
	/**
	 * 客服中心列表
	 * @param acs
	 * @return
	 */
	@RequestMapping(value="customerServiceList")
	@ResponseBody
	public JSONObject customerServiceList(ApiCustomerService acs,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum){
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		String content = "";
		try {
			ApiPage<ApiCustomerService> apiPage = customerServiceFacade.queryCustomerServiceList(acs, page, pageNum);
			List<ApiCustomerService> apiCustomerServices = apiPage.getResultData();
			if(apiCustomerServices.size() == 0){
				data.put("result", 1);
			}else {
				for(ApiCustomerService cs:apiCustomerServices){
					JSONObject item = new JSONObject();
					item.put("id", cs.getId());
					item.put("type", cs.getType());
					content = PNews.dealContent(cs.getContent());
					item.put("content", content);
					item.put("createTime", cs.getCreateTime());
					item.put("state", cs.getState());
					item.put("visit", cs.getVisit());
					items.add(item);
				}
				data.put("items", items);
				data.put("page", apiPage.getPageNum());// 当前页码
			    data.put("pageNum", apiPage.getPageSize()); // 每页行数
			    data.put("total", apiPage.getPages());// 总页数
			    data.put("result", 0);
			}
		} catch (Exception e) {
			//后台服务发生异常
			e.printStackTrace();
			data.put("result", 2);
			return data;
		}
		return data;
	}
	/**
	 * 进入详情页
	 * @param id
	 * @return
	 */
	@RequestMapping(value="toServiceDetail")
	public ModelAndView toServiceDetail(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id",required=false)Integer id){
		ModelAndView view = new ModelAndView("service/service_detail");
		ApiCustomerService acs = customerServiceFacade.queryById(id);
		ApiCustomerService cs = new ApiCustomerService();
		cs.setId(acs.getId());
		Integer visit = acs.getVisit() == null ? 0 : acs.getVisit();
		visit = visit +1;
		cs.setVisit(visit);
		customerServiceFacade.updateCustomerService(cs);
		Integer userId = UserUtil.getUserId(request, response);
		view.addObject("acs", acs);
		if(userId != null ){
			ApiFrontUser fu = userFacade.queryById(userId);
			view.addObject("user", fu);
		}
		view.addObject("userId", userId);
		
		return view;
	}
	/**
	 * 详情页
	 * @param slw
	 * @param page
	 * @param pageNum
	 * @return
	 */
	@RequestMapping(value="serviceDetail")
	@ResponseBody
	public JSONObject serviceDetail(ApiServiceLeaveWord slw,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum){
		JSONObject data = new JSONObject();
		JSONArray items = new JSONArray();
		String reply = "";
		try {
			ApiPage<ApiServiceLeaveWord> apiPage = customerServiceFacade.queryServiceLeaveWordlist(slw, page, pageNum);
			List<ApiServiceLeaveWord> list = apiPage.getResultData();
			if(list.size() == 0){
				data.put("result", 1);
			}else {
				for(ApiServiceLeaveWord aslWord:list){
					JSONObject item = new JSONObject();
					item.put("id", aslWord.getId());
					item.put("headUrl", aslWord.getHeadUrl());
					item.put("userNickName", aslWord.getUserNickName());
					item.put("customerId", aslWord.getCustomerId());
					item.put("customerName", aslWord.getCustomerName());
					reply = PNews.dealContent(aslWord.getReply());
					item.put("reply", reply);
					item.put("createTime", aslWord.getCreateTime());
					items.add(item);
				}
				data.put("items", items);
				data.put("page", apiPage.getPageNum());// 当前页码
			    data.put("pageNum", apiPage.getPageSize()); // 每页行数
			    data.put("total", apiPage.getPages());// 总页数
			    data.put("result", 0);
			}
		} catch (Exception e) {
			//后台服务器异常
			e.printStackTrace();
			data.put("result", 2);
			return data;
		}
		return data;
	}
	/**
	 * H5用户意见
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 */
	@RequestMapping(value="userAdvice")
	public ModelAndView H5UserAdvice(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="type",required=false,defaultValue="1") Integer type)
	{
		ModelAndView view = new ModelAndView("h5/service/user_advice");
		Integer userId = UserUtil.getUserId(request, response);
		view.addObject("type",type);
		view.addObject("userId", userId);
		return view;
	}
	
	//获取验证码
	public String getImgCode(HttpServletRequest request) {
		return imgcodeprex + getUuid(request);
	}
	
	private String getUuid(HttpServletRequest request) {
		String uuid = CookieManager.retrieve(CookieManager.COOKIE_UUID_NAME,
				request, true);
		if (uuid == null) {
			return request.getSession().getId();
		} else {
			return uuid;
		}
	}
	
	
}
