package com.guangde.home.controller.ningbo778;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.commons.IFileFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiNews;
import com.guangde.entry.ApiProject;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.pojo.ApiPage;
import com.redis.utils.RedisService;
/**
 * @author zhl 2017-8-8
 */
@Controller
public class NingBo778Controller {
	@Autowired
	private INewsFacade newsFacade;
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IFileFacade fileFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ICommonFacade commonFacade;
	
	/**
	 * 778 index
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/778/index",method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mv){
		mv = new ModelAndView("resourceCenter778/index");
		//轮播图
		banner(mv);
		//中心大事件  bigEvents;公益科普 sicencePopulation
		newsList(mv);
		//推荐项目
		weekProject(mv);
		//专项基金
		specialFund(mv);
		//合作伙伴
		setFriendLink(mv);
		return mv;
	}
	
	/**
	 * 778 news
	 * @param mv type
	 * @return
	 */
	@RequestMapping(value="/778/news",method=RequestMethod.GET)
	public ModelAndView news(ModelAndView mv,@RequestParam("type")String type){
		mv = new ModelAndView("resourceCenter778/news");
		ApiNews apiNews = new ApiNews();
		apiNews.setNews_column(type);
		apiNews.setOrderBy("ordertime");
		apiNews.setOrderDirection("desc");
		ApiPage<ApiNews> page = newsFacade.queryNewsList(apiNews, 1, 10);
		mv.addObject("bigEvents",page.getResultData());
		mv.addObject("type", type);
		return mv;
	}
	
	/**
	 * 778 news detail
	 * @param mv id
	 * @return
	 */
	@RequestMapping(value="/778/news/detail",method=RequestMethod.GET)
	public ModelAndView detail(ModelAndView mv,@RequestParam("id")Integer id,@RequestParam("type")String type){
		mv = new ModelAndView("resourceCenter778/newsDetail");
		ApiNews news = newsFacade.queryNewsDetail(id);
		mv.addObject("news", news);
		mv.addObject("type", type);
		return mv;
	}
	
	/**
	 * ajax加载 news list
	 * @param type
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@RequestMapping(value="/778/news/ajaxLoadNewList",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject ajaxLoadNewList(@RequestParam("type")String type,
			@RequestParam(value="pageSize",defaultValue="10")Integer pageSize,
			@RequestParam(value="pageNum",defaultValue="1")Integer pageNum){
		JSONObject item = new JSONObject();
		JSONArray items = new JSONArray();
		ApiNews apiNews = new ApiNews();
		apiNews.setNews_column(type);
		apiNews.setOrderBy("ordertime");
		apiNews.setOrderDirection("desc");
		ApiPage<ApiNews> page = newsFacade.queryNewsList(apiNews, pageNum, pageSize);
		item.put("total", page.getTotal());
		if(page!=null && page.getTotal()>0 && (page.getTotal()-pageNum*pageSize>=0 || pageNum*pageSize-page.getTotal()<pageSize)){
			if(page.getTotal()-pageNum*pageSize>0){
				item.put("state", 1);
			}else{
				item.put("state", 0);
			}
			for (ApiNews news : page.getResultData()) {
				JSONObject json = new JSONObject();
				json.put("id", news.getId());
				json.put("title", news.getTitle());
				json.put("createTime", DateUtil.dateString(news.getCreatetime()));
				json.put("coverImageUrl", news.getCoverImageUrl());
				json.put("abstracts", news.getAbstracts());
				items.add(json);
			}
		}else{
			item.put("state", 0);
		}
		item.put("result", items);
		return item;
	}
	
	/**
	 * 778 about
	 * @param mv 
	 * @return
	 */
	@RequestMapping(value="/778/about",method=RequestMethod.GET)
	public ModelAndView about(ModelAndView mv){
		mv = new ModelAndView("resourceCenter778/about");
		return mv;
	}
	
	/**
	 * 778 relation
	 * @param mv 
	 * @return
	 */
	@RequestMapping(value="/778/relation",method=RequestMethod.GET)
	public ModelAndView relation(ModelAndView mv){
		mv = new ModelAndView("resourceCenter778/relation");
		return mv;
	}

	//轮播图
	public void banner (ModelAndView view){
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_778_BANNER_LIST);
		if(obj != null){
			view.addObject("bannerList", obj);
		}else{
			List<ApiBFile> bfiles = null;
			ApiBFile apiBfile = new ApiBFile(); 
			apiBfile.setCategory("banner_index_778");
			bfiles = commonFacade.queryApiBfile(apiBfile);
			view.addObject("bannerList", bfiles);
			redisService.saveObjectData(PengPengConstants.INDEX_778_BANNER_LIST, bfiles, DateUtil.DURATION_FIVE_S);
		}
	}
	//专项基金  specialFund
	public void specialFund(ModelAndView view){
		Object specialFund1 = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_1");
		Object specialFund2 = redisService.queryObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_2");
		if(specialFund1 != null && specialFund2 != null){
			//专项基金的项目
	        view.addObject("specialFundList", (List<String>)specialFund2);
	        //专项基金的tab标题名字
	        view.addObject("tvbTitleList", (List<ApiProject>)specialFund1);
		}else{
			ApiConfig aConfig = new ApiConfig();
			aConfig.setConfigKey("specialFund");
			List<ApiConfig> acg = commonFacade.queryList(aConfig);
			if(acg.size() > 0){
				aConfig = acg.get(0);
				String tvbTitle = null;
				String tvbProjectId = null;
				String[] pNum = aConfig.getConfigValue().split(";");
				int totalSF = pNum.length;
				List<ApiProject> specialFundList = new ArrayList<ApiProject>(totalSF);
				List<String> tvbTitleList = new ArrayList<String>(totalSF);
				if (!aConfig.getConfigValue().contains(":")) {
					//不显示专项基金
					view.addObject("isShowSF", 0);
				}else {
					for (int i = 0; i < (pNum.length>=3?3:pNum.length); i++) {
						tvbTitle = pNum[i].split(":")[0];
						tvbTitleList.add(tvbTitle);
						tvbProjectId = pNum[i].split(":")[1];
						ApiProject apiProject = projectFacade.queryProjectDetail(Integer.valueOf(tvbProjectId));
						if(StringUtils.isNotEmpty(apiProject.getSubTitle()) && apiProject.getSubTitle().length() > 34){
							apiProject.setSubTitle(apiProject.getSubTitle().subSequence(0, 34)+"...");
						}
						specialFundList.add(apiProject);
					}
					//专项基金的项目
			        view.addObject("specialFundList", specialFundList);
			        //专项基金的tab标题名字
			        view.addObject("tvbTitleList", tvbTitleList);
					redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_1", tvbTitleList, DateUtil.DURATION_TEN_S);
					redisService.saveObjectData(PengPengConstants.INDEX_PROJECT_SPECIALFUND + "_2", specialFundList, DateUtil.DURATION_TEN_S);
				}
			}else{
				view.addObject("isShowSF", 0);
			}
		}
	}
	
	public void weekProject(ModelAndView view) {
		//本周推荐
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_WEEKPROJECT_LIST);
		if (obj != null) {
			view.addObject("rdList", obj);
		} else {
			 ApiProject ap = new ApiProject();
	         ap.setOrderDirection("desc");
	         ap.setIsHide(0);
	         ap.setIsRecommend(1);
	         ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, 1, 3);
	         view.addObject("rdList", apiPage.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_WEEKPROJECT_LIST,
					apiPage.getResultData(), DateUtil.DURATION_FIVE_S);
		}
		//众筹项目
		/*Object obj2 = redisService.queryObjectData(PengPengConstants.INDEX_CROWDFUND_ROJECT_LIST);
		if (obj2 != null) {
			view.addObject("gardenList", obj2);
		} else {
			 ApiProject ap = new ApiProject();
			 ap.setField("garden");
			ap.setState(240);
			ap.setIsHide(0);
	         ApiPage<ApiProject> apiPage = projectFacade.queryProjectList(ap, 1, 2);
	         view.addObject("gardenList", apiPage.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_CROWDFUND_ROJECT_LIST,
					apiPage.getResultData(), DateUtil.DURATION_FIVE_S);
		}*/
	}
	
	/**
	 * 友情链接
	 * @param view
	 */
	public void setFriendLink(ModelAndView view){
		try {
			Object objImg = redisService.queryObjectData(PengPengConstants.INDEX_FRIENDLINK_IMG);
			if(objImg !=null){
				view.addObject("linkImgs", objImg);
			}else{
				List<ApiBFile> bfiles = null;
				ApiBFile apiBfile = new ApiBFile(); 
				apiBfile.setCategory("friendLink");
				bfiles = commonFacade.queryApiBfile(apiBfile);
				view.addObject("linkImgs", bfiles);
				redisService.saveObjectData(PengPengConstants.INDEX_FRIENDLINK_IMG, bfiles, DateUtil.DURATION_MONTH_S);
			}
		
		} catch (Exception e) {
			// 后台服务发生异常
			e.printStackTrace();
			
		}
	}
	
	public void newsList(ModelAndView view) {
		//最新中心大事件
		Object obj = redisService.queryObjectData(PengPengConstants.INDEX_BIGEVENTS_LIST);
		if (obj != null) {
			view.addObject("bigEvents", obj);
		} else {
			ApiNews apiNews = new ApiNews();
			 apiNews.setNews_column("中心大事件");
			 apiNews.setOrderBy("ordertime");
			 apiNews.setOrderDirection("desc");
			 ApiPage<ApiNews> page = newsFacade.queryNewsList(apiNews, 1, 3);
			 view.addObject("bigEvents",page.getResultData());
			redisService.saveObjectData(PengPengConstants.INDEX_BIGEVENTS_LIST,
					page.getResultData(), DateUtil.DURATION_TEN_S);
		}
		//公益科普
		Object obj2 = redisService.queryObjectData(PengPengConstants.INDEX_SICENCE_POPULATION_LIST);
		if (obj2 != null) {
			view.addObject("sicencePopulation", obj2);
		} else {
			ApiNews apiNews = new ApiNews();
			apiNews.setNews_column("公益科普");
			apiNews.setOrderBy("ordertime");
			apiNews.setOrderDirection("desc");
			 ApiPage<ApiNews> page = newsFacade.queryNewsList(apiNews, 1, 3);
			List<ApiNews> list = page.getResultData();
			for(ApiNews an:list){
				if(an.getCoverImageId() != null){
					ApiBFile apiBfileNews = commonFacade.queryBFileById(an.getCoverImageId());
					if(apiBfileNews != null){
						an.setCoverImageUrl(apiBfileNews.getUrl());
					}
				}
			}
			view.addObject("sicencePopulation",list);
			redisService.saveObjectData(PengPengConstants.INDEX_SICENCE_POPULATION_LIST,
					page.getResultData(), DateUtil.DURATION_TEN_S);
		}
	}
}
