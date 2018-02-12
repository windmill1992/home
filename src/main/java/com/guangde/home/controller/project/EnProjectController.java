package com.guangde.home.controller.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.dto.ApiCompanyDto;
import com.guangde.dto.ApiEmployeeDto;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiCompany;
import com.guangde.entry.ApiCompany_GoodHelp;
import com.guangde.entry.ApiEmployee;
import com.guangde.entry.ApiFrontUser;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.user.Activity;
import com.guangde.home.vo.user.EDSituation;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;

/**
 * 
 * @author px
 * 企业助善相关业务
 *
 */

@Controller
@RequestMapping("enproject")
public class EnProjectController {

	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private ICompanyFacade companyFacade;
	/**
	 * 创建企业助善项目页面
	 * @param request
	 * @param response
	 * @param activity
	 * @return
	 */
	@RequestMapping(value = "core/zhuShanCreate")
	public ModelAndView zhuShanCreate(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute Activity activity) {
		//1.如果要充值，调用郭奇伟那边改版的支付方法
		//2.不用充值，直接发起助善
		//3.有错误返回（原页面）错误信息或成功页面
		  Integer userId = UserUtil.getUserId(request, response);
		  String userType = SSOUtil.getCurrentUserType(request, response);
		  if(!PUser.USER_TYPE_E.equals(userType)){
			  ModelAndView view = new ModelAndView("enterprise/zhuShanCreate");
			  webUtil.failedView(view,webUtil.ERROR_CODE_ILLEGAL,"非企业用户");
			  return view;
		  }
		  if(!activity.valideCreateParam()){
			  ModelAndView view = new ModelAndView("enterprise/zhuShanCreate");
			  webUtil.failedView(view,webUtil.ERROR_CODE_PARAMWRONG,"参数错误");
			  return view;
		  }
		  if(userId == null){
			  ModelAndView view = new ModelAndView("enterprise/zhuShanCreate");
			  webUtil.failedView(view,webUtil.ERROR_CODE_LOGINFAILED,"用户未登录");
			  return view;
		  }
		  ApiCompany_GoodHelp apiGoodHelp = new ApiCompany_GoodHelp();
		  apiGoodHelp.setUserId(userId);
		  apiGoodHelp.setProject_id(activity.getpId());
		  apiGoodHelp.setFreezAmount(activity.getTotalMoney());
		  apiGoodHelp.setPerMoney(activity.getPerMoney());
		  
		  //获取companyId 统一加缓存处理
		  ApiCompany apiCompany = new ApiCompany();
		  apiCompany.setUserId(userId);
		  apiCompany = companyFacade.queryCompanyByParam(apiCompany);
		  apiGoodHelp.setCompany_id(apiCompany.getId());
		  
		  ApiCapitalinout inOut = new ApiCapitalinout(); 
		  inOut.setPayType("freezType");
		  inOut.setSource("PC");
		  inOut.setMoney(activity.getTotalMoney());
		  inOut.setUserId(userId);
		  inOut.setTranNum(StringUtil.uniqueCode());
		  ApiResult result = companyFacade.launchCompanyGoodHelp(apiGoodHelp,inOut);
		  if(result.getCode()==1){
			  ModelAndView view = new ModelAndView("redirect:/enproject/core/zhuShanList.do");
			  return view;
		  }else{
			  ModelAndView view = new ModelAndView("redirect:/enterprise/core/entcreatehelpgood.do");
			  view.addObject("projectId", activity.getpId());
			  webUtil.failedView(view,webUtil.ERROR_CODE_ADDFAILED,"发起助善失败");
			  return view;
		  }  
	}
	/**
	 * 企业助善列表页面
	 */
	@RequestMapping(value = "core/zhuShanList")
	public ModelAndView zhuShanList(HttpServletRequest request,
			HttpServletResponse response) {
		  Integer userId = UserUtil.getUserId(request, response);
		  ModelAndView view = new ModelAndView("enterprise/zhuShanList");
		  /*ApiFrontUser user = new ApiFrontUser();
			user.setId(userId);
			user = userFacade.queryPersonCenter(user);*/
		  ApiFrontUser user = userFacade.queryById(userId);
		  view.addObject("user", user);
		  view.addObject("guideName", "助善项目");
		  return view;
	}
	/**
	 * 企业助善列表页面
	 */
	@RequestMapping(value = "company")
	public ModelAndView company(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "userId", required = false) Integer userId) {
		  if(userId==null){
			  userId = UserUtil.getUserId(request, response); 
		  }
		  ModelAndView view = new ModelAndView("enterprise/company");
		  ApiCompany apiCompany = new ApiCompany();
		  if(userId != null){
			apiCompany.setUserId(userId);
			ApiCompany company = companyFacade.queryCompanyByParam(apiCompany);
			view.addObject("company", company);
			ApiCompanyDto companyDto = companyFacade.queryCompanyDetailCount(userId);
			view.addObject("companyDto", companyDto);
		  }
		  return view;
	}
	/**
	 * 修改企业助善项目
	 * @return
	 */
	@RequestMapping("/coredata/updateZhuShan")
	@ResponseBody
	public Map<String,Object> updateZhuShan(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "aid", required = false) Integer aid,
			@RequestParam(value = "perMoney", required = false) Double perMoney,
			@RequestParam(value = "totalMoney", required = false) Double totalMoney){
		      
		     if(aid==null||perMoney==null){
		    	return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误", null); 
		     }
			 Integer userId = UserUtil.getUserId(request, response);
			 
			 ApiCompany_GoodHelp param = new ApiCompany_GoodHelp();
			 param.setUserId(userId);
			 param.setId(aid);
			 
			 ApiPage<ApiCompany_GoodHelp> r =  companyFacade.queryCompanyGoodHelpList(param, 1, 1);
			 if(r.getTotal()==0){
				 return webUtil.failedRes(webUtil.ERROR_CODE_ILLEGAL, "没有对应助善项目", null); 
			 }
			 param = r.getResultData().get(0);
			 if(param.getLeaveAmount()<perMoney){
				 return webUtil.failedRes(webUtil.ERROR_CODE_DATAWRONG, "金额大于剩余可捐金额", null); 
			 }
			 ApiCompany_GoodHelp apiGoodHelp = new ApiCompany_GoodHelp();
		     apiGoodHelp.setUserId(userId);
		     apiGoodHelp.setId(aid);
		     apiGoodHelp.setPerMoney(perMoney);
		     apiGoodHelp.setGoodHelpAmount(null);
		     ApiResult result = companyFacade.updateCompanyGoodHelp(apiGoodHelp);
		     if(result.getCode()==1){
		    	 return webUtil.successRes(null);
			 }else{
				 return webUtil.failedRes(webUtil.ERROR_CODE_UPDATEFAILED, "更新失败", null);	 
			 }  
	}
	/**
	 * 结束企业助善项目
	 * @return
	 */
	@RequestMapping("/coredata/stopZhuShan")
	@ResponseBody
	public Map<String,Object> stopZhuShan(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "aid", required = false) Integer aid,
			@RequestParam(value = "reason", required = false) String reason
			){
		     
		     if(aid==null||StringUtils.isBlank(reason)){
		    	return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误", null); 
		     }
			 Integer userId = UserUtil.getUserId(request, response);
			 ApiCompany_GoodHelp apiGoodHelp = new ApiCompany_GoodHelp();
		     apiGoodHelp.setUserId(userId);
		     apiGoodHelp.setId(aid);
		     apiGoodHelp.setStopReason(reason);
		     ApiResult result = companyFacade.stopCompanyGoodHelp(apiGoodHelp);
		     if(result.getCode()==1){
		    	 return webUtil.successRes(null);
			 }else{
				 return webUtil.failedRes(webUtil.ERROR_CODE_UPDATEFAILED, "结束助善项目失败", null); 
			 }  
	}
	
	/**
	 * 企业助善列表
	 * @return
	 */
	@RequestMapping("/zhuShanList")
	@ResponseBody
	public Map<String,Object> zhuShanList(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false, defaultValue="1") Integer page,
			@RequestParam(value = "pageNum", required = false,defaultValue="10") Integer pageNum,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "type", required = false) String type
			){
			if(userId==null){
				// 1.验证是否登入
				userId = UserUtil.getUserId(request, response);
				if (userId == null) {
					return webUtil.loginFailedRes(null);
				}
			}
		    Page p = new Page();
		    p.setPage(page);
		    p.setPageNum(pageNum);
		     
		    List<Activity> list = new ArrayList<Activity>(pageNum);
		    p.setData(list);
		    ApiCompany_GoodHelp goodHelp = new ApiCompany_GoodHelp();
		    goodHelp.setUserId(userId);
		    if(!StringUtils.isBlank(type)){
		    	List<Integer> states = new ArrayList<Integer>(3);
		    	states.add(203);
		    	states.add(205);
		    	states.add(207);
		    	goodHelp.setStateList(states);
		    
		    }
			goodHelp.setPayState(302);
			goodHelp.setOrderBy("createTime");
			goodHelp.setOrderDirection("desc");
		    ApiPage<ApiCompany_GoodHelp> result = companyFacade.queryCompanyGoodHelpList(goodHelp, page, pageNum);
		    if(result!=null){
		    	Activity activity=null;
		    	for(ApiCompany_GoodHelp g:result.getResultData()){
		    		activity = new Activity();
		    		activity.setAid(g.getId());
		    		activity.setcTime(g.getCreateTime()==null?0:g.getCreateTime().getTime());
		    		activity.setState(g.getState());
		    		activity.setpName(g.getProjectName());
		    		activity.setPerMoney(g.getPerMoney());
		    		activity.setPeopleNums(g.getCallNumber());
		    		activity.setTotalMoney(g.getFreezAmount());
					activity.setProcess(StringUtil.doublePercentage(g.getGoodHelpAmount(),g.getFreezAmount(),0));
					activity.setPayMoney(g.getGoodHelpAmount());
					activity.setStopReason(g.getStopReason());
					activity.setpId(g.getProject_id());
					list.add(activity);
		    	}
		    	p.setNums(result.getTotal());
		    }else{
		    	p.setNums(0);
		    }
		    p.setInfo(companyFacade.countGoodHelAmount(goodHelp));
		    return webUtil.successRes(p);
	}
	
	/**
	 * 善员工动态
	 * @return
	 */
	@RequestMapping("/edsituation")
	@ResponseBody
	public Map<String,Object> edsituation(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", required = false, defaultValue="1") Integer page,
			@RequestParam(value = "pageNum", required = false,defaultValue="10") Integer pageNum,
			@RequestParam(value = "userId", required = false) Integer userId
			){
	
			if(userId==null){
				// 1.验证是否登入
				userId = UserUtil.getUserId(request, response);
				if (userId == null) {
					return webUtil.loginFailedRes(null);
				}
			}
		    Page p = new Page();
		    p.setPage(page);
		    p.setPageNum(pageNum);
		    List<EDSituation> list = new ArrayList<EDSituation>(pageNum);
		    p.setData(list);
		    
		    Date now = new Date();
		    ApiEmployee emploee = new ApiEmployee();
		    emploee.setCompany_userId(userId);
		    emploee.setQueryStartDate(DateUtil.add(now, -30));
		    emploee.setQueryEndDate(now);
		    ApiPage<ApiEmployeeDto> result = companyFacade.queryEmployeeDtoList(emploee, page, pageNum);
		    if(result!=null){
		    	EDSituation eDSituation=null;
		    	for(ApiEmployeeDto e:result.getResultData()){
		    		eDSituation = new EDSituation();
		    		eDSituation.seteName(e.getUserName());
		    		eDSituation.setpCtime(e.getDonatTime()==null?0:e.getDonatTime().getTime());
		    		eDSituation.setpName(e.getTitle());
		    		eDSituation.setpProcess(StringUtil.doublePercentage(e.getDonatAmount(), e.getCryMoney(),0));
		    		eDSituation.setpId(e.getProjectId());
		    		eDSituation.setField(e.getField());
		    	    list.add(eDSituation); 
		    	}
		    	p.setNums(result.getTotal());
		    }else{
		    	p.setNums(0);
		    }
		    return webUtil.successRes(p);
	}
	
	
	/**
	 * 获取企业用户对应项目的助善信息
	 * @return
	 */
	@RequestMapping("/coredata/projectZhuShan")
	@ResponseBody
	public Map<String,Object> projectZhuShan(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "projectId", required = false) Integer projectId
			){
		    Integer userId = UserUtil.getUserId(request, response);
		    if(projectId==null){
		    	return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG, "参数错误", null);
		    }
		    ApiCompany_GoodHelp goodHelp = new ApiCompany_GoodHelp();
		    goodHelp.setProject_id(projectId);
		    ApiPage<ApiCompany_GoodHelp> result =  companyFacade.queryCompanyGoodHelpList(goodHelp, 1, 1);
		    Activity activity = new Activity();
		    if(result!=null&&result.getTotal()!=0){
		    	goodHelp = result.getResultData().get(0);
		    	activity.setAid(goodHelp.getId());
		    	activity.setPerMoney(goodHelp.getPerMoney());
		    	activity.setTotalMoney(goodHelp.getFreezAmount());
		    	activity.setPayMoney(goodHelp.getGoodHelpAmount());
		    	activity.setState(goodHelp.getState());
		    	activity.setOwn(goodHelp.getUserId().equals(userId));
		    	
		    	//获取companyCode 统一加缓存处理
				ApiCompany apiCompany = new ApiCompany();
				apiCompany.setUserId(userId);
				apiCompany = companyFacade.queryByCompany(apiCompany);
		    	activity.setCompanyCode(apiCompany.getGoodPassWord());
		    }
		    
		    return webUtil.successRes(activity);
	}
}
