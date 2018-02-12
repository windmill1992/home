package com.guangde.home.controller.message;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiNews;
import com.guangde.home.utils.webUtil;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.message.PNews;
import com.guangde.pojo.ApiPage;

@Controller
@RequestMapping("news")
public class NewsController {
	@Autowired
	private INewsFacade newsFacade;
	@Autowired
	private ICommonFacade commonFacade;
	
	/**
	 * 新闻中心
	 * type: 1，最新；2，热点；3，爱心故事;7,结项报告
	 */
	@RequestMapping(value = "center")
	public ModelAndView center(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "type", required = false,defaultValue="1") Integer type){
		ModelAndView view = new ModelAndView();
		if(type==1){
			view = new ModelAndView("news/notice_detail");
		}else {
			view = new ModelAndView("news/activity_detail");
		}
		
		ApiNews apiNews = new ApiNews();
		//热点新闻
		apiNews.setNews_column("公告");
		apiNews.setOrderBy("visits");
		apiNews.setOrderDirection("desc");
		ApiPage<ApiNews>  page = newsFacade.queryNewsList(apiNews, 1, 5);
		view.addObject("hotNews", page.getResultData());
		//最新新闻
		apiNews.setNews_column("公告");
		apiNews.setOrderBy("ordertime");
		apiNews.setOrderDirection("desc");
		page = newsFacade.queryNewsList(apiNews, 1, 5);
		view.addObject("latestNews",page.getResultData());
	    //爱心故事
		apiNews.setNews_column("爱心故事");
		apiNews.setOrderBy("ordertime");
		apiNews.setOrderDirection("desc");
		page = newsFacade.queryNewsList(apiNews, 1, 5);
		view.addObject("loveNews",page.getResultData());
		if(id!=null){
			//某个新闻的详情
			ApiNews oneNew = newsFacade.queryNewsDetail(id);
			ApiNews temp = new ApiNews();
			temp.setId(id);
			temp.setVisits(oneNew.getVisits()+1);
			newsFacade.updateVisits(temp);
			if(oneNew!=null)
			  oneNew.setContent(PNews.dealContent(oneNew.getContent()));
			view.addObject("oneNew", oneNew);
			if(oneNew.getNews_column().equals("公告")){
				view.addObject("notice", "notice");
			}else if(oneNew.getNews_column().equals("结项报告")){
				view.addObject("project_end", "project_end");
			}
			temp.setId(null);
			temp.setNews_column(oneNew.getNews_column());
			temp.setOrdertime(oneNew.getOrdertime());
			view.addObject("preNews", newsFacade.queryPreviousRecord(temp));
			view.addObject("nextNews", newsFacade.queryNextRecord(temp));
		}else{
			if(type ==1){
				view.setViewName("news/notice_list");
				view.addObject("notice", "notice");
			}else if(type == 7){
				view.setViewName("news/project_end_list");
				view.addObject("project_end", "project_end");
			}else {
				view.setViewName("news/activity_list");
				view.addObject("activity", "activity");
			}
			view.addObject("type",type);
		}
		return view;
	}
	/**
	 * 新闻列表
	 * type: 1，最新；2，热点；3，爱心故事
	 * 	4，财务信息 ； 5，项目信息；6，人事管理制度;7.结项报告
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public Map<String,Object> list(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "type", required = false,defaultValue="1") Integer type,
			@RequestParam(value = "page", required = false, defaultValue="1") Integer page,
			@RequestParam(value = "pageSize", required = false,defaultValue="10") Integer pageSize){
		 
		    ApiNews apiNews = new ApiNews();
		    apiNews.setOrderBy("ordertime");
			apiNews.setOrderDirection("desc");
			apiNews.setNews_column("公告");
		    if(type==2){
		    	apiNews.setOrderBy("visits");
				apiNews.setOrderDirection("desc");
		    }else if(type==3){
		    	apiNews.setNews_column("爱心故事");
		    }else if(type==4){
		    	apiNews.setNews_column("财务信息");
		    }else if(type==5){
		    	apiNews.setNews_column("项目信息");
		    }else if(type==6){
		    	apiNews.setNews_column("人事管理制度");
		    }else if(type == 7){
				apiNews.setNews_column("结项报告");
			}
		    else if(type == 12){
				apiNews.setNews_column("理事会");
			}
			ApiPage<ApiNews>  r = newsFacade.queryNewsList(apiNews, page, pageSize);
			List<ApiNews> aNews = r.getResultData();
			for(ApiNews an:aNews){
	 			if(an.getCoverImageId() != null){
	 				ApiBFile apiBfileNews = commonFacade.queryBFileById(an.getCoverImageId());
	 				if(apiBfileNews != null){
	 					an.setCoverImageUrl(apiBfileNews.getUrl());
	 				}
	 			}
	 		}
			r.setResultData(aNews);
			Page p = new Page();
			p.setData(r.getResultData());
			p.setPage(r.getPageNum());
			p.setNums(r.getTotal());
			p.setTotal(r.getPages());
			p.setPageNum(r.getPageSize());
		    return webUtil.successRes(p);
	}
	/**
	 * 新闻详情
	 */
	@RequestMapping(value = "detail")
	@ResponseBody
	public Map<String,Object> detail(
			@RequestParam(value = "id", required = false) Integer id){
		   if(id==null){
			   return webUtil.failedRes(webUtil.ERROR_CODE_PARAMWRONG,"缺少新闻id", null);
		   }
		   ApiNews oneNew = newsFacade.queryNewsDetail(id);
		   if(oneNew!=null)
		     oneNew.setContent(PNews.dealContent(oneNew.getContent()));
		   return webUtil.successRes(oneNew);
	}
	
	/**
	 * 披露信息
	 * 	动态配置
	 * @param request
	 * @param response
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "infoCenter")
	public ModelAndView infoCenter(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "type", required = false,defaultValue="1") Integer type){
		ModelAndView view = new ModelAndView();
		
		view = new ModelAndView("news/infoDetail");
		if(type == 4)
		{
			//view.addObject("notice", "notice");
			view.addObject("lText", "财务信息");
			view.addObject("cwxx", "cwxx");
		}
		else if(type == 5)
		{
			view.addObject("lText", "项目信息");
			view.addObject("xmxx", "xmxx");
		}
		else if(type == 6)
		{
			view.addObject("lText", "人事管理制度");
		}
		else if(type == 7)
		{
			view.addObject("lText", "章程");
		}
		else if(type == 8)
		{
			view.addObject("lText", "基本信息");
		}
		else if(type == 9)
		{
			view.addObject("lText", "理事姓名、工作单位");
		}
		else if(type == 10)
		{
			view.addObject("lText", "项目管理制度");
		}
		else if(type == 11)
		{
			view.addObject("lText", "财务管理办法");
		}
		else if(type == 12)
		{
			view.addObject("lText", "理事会");
			view.addObject("lishi", "lishi");
		}
		view.addObject("type",type);
		
		if(id!=null){
			//某个新闻的详情
			ApiNews oneNew = newsFacade.queryNewsDetail(id);
			ApiNews temp = new ApiNews();
			temp.setId(id);
			temp.setVisits(oneNew.getVisits()==null?0:oneNew.getVisits()+1);
			newsFacade.updateVisits(temp);
			if(oneNew!=null)
			  oneNew.setContent(PNews.dealContent(oneNew.getContent()));
			view.addObject("oneNew", oneNew);
			//if(oneNew.getNews_column().equals("公告")){
				view.addObject("notice", "notice");
			//}
			temp.setId(null);
			temp.setNews_column(oneNew.getNews_column());
			temp.setOrdertime(oneNew.getOrdertime());
			view.addObject("preNews", newsFacade.queryPreviousRecord(temp));
			view.addObject("nextNews", newsFacade.queryNextRecord(temp));
		}else{
			
			view.setViewName("news/infolist");
			
		}
		return view;
	}
	

	@RequestMapping(value = "infoByItem")
	public ModelAndView infoByItem(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "item", required = false) String item,
			@RequestParam(value = "type", required = false,defaultValue="1") Integer type){
		ModelAndView view = new ModelAndView();
		
		view = new ModelAndView("news/infoDetail");
		if(type == 4)
		{
			//view.addObject("notice", "notice");
			view.addObject("lText", "财务信息");
			view.addObject("cwxx", "cwxx");
		}
		else if(type == 5)
		{
			view.addObject("lText", "项目信息");
			view.addObject("xmxx", "xmxx");
		}
		else if(type == 6)
		{
			view.addObject("lText", "人事管理制度");
			view.addObject("renshi", "renshi");
		}
		else if(type == 7)
		{
			view.addObject("lText", "章程");
			view.addObject("rules", "rules");
		}
		else if(type == 8)
		{
			view.addObject("lText", "基本信息");
			view.addObject("baseInfo", "baseInfo");
		}
		else if(type == 9)
		{
			view.addObject("lText", "理事信息");
			view.addObject("workUnit", "workUnit");
		}
		else if(type == 10)
		{
			view.addObject("lText", "项目管理制度");
			view.addObject("xmglzd", "xmglzd");
		}
		else if(type == 11)
		{
			view.addObject("lText", "财务管理办法");
			view.addObject("caiwu", "caiwu");
		}
		else if(type == 12)
		{
			view.addObject("lText", "理事会");
			view.addObject("lishi", "lishi");
		}
		view.addObject("type",type);
		
		if(item!=null){
			//某个新闻的详情
			ApiNews oneNew = new ApiNews() ; 
			ApiNews  apiNews = new ApiNews();
			apiNews.setNews_column(item);
			ApiPage<ApiNews>  r = newsFacade.queryNewsList(apiNews, 1, 1);
			if(r.getResultData() != null && r.getResultData().size() > 0){
				oneNew = r.getResultData().get(0);
				ApiNews temp = new ApiNews();
				temp.setId(oneNew.getId());
				temp.setVisits(oneNew.getVisits()==null?0:oneNew.getVisits()+1);
				newsFacade.updateVisits(temp);
				if(oneNew!=null)
					oneNew.setContent(PNews.dealContent(oneNew.getContent()));
				view.addObject("oneNew", oneNew);
				view.addObject("notice", "notice");
			}
		}else{
			view.setViewName("news/infolist");
		}
		return view;
	}

}
