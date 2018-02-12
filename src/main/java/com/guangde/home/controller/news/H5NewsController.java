package com.guangde.home.controller.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.INewsFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiNews;
import com.guangde.pojo.ApiPage;

@Controller
@RequestMapping("h5News")
public class H5NewsController {
	@Autowired
	private INewsFacade newsFacade;
	@Autowired
	private ICommonFacade commonFacade;
	
	//public final String BASE_IMAGE_URL="http://res.17xs.org";
	//新闻view
	@RequestMapping("news_view")
	public ModelAndView news_view(ModelAndView view,@RequestParam(value="type",required=true)String type){
		if("慈善知识".equals(type)){
			view = new ModelAndView("h5/news/knowledge");
		}else if("善园快讯".equals(type)){
			view = new ModelAndView("h5/news/syNews");
		}else if("活动".equals(type)){
			view = new ModelAndView("h5/news/activity");
		}else if("公告".equals(type)){
			view = new ModelAndView("h5/news/publicity");
		}else if("爱心故事".equals(type)){
			view = new ModelAndView("h5/news/story");
		}
		 view.addObject("type", type);
		return view;
	}
	
	//loadList
	@ResponseBody
	@RequestMapping("ajaxNewsList")
	public JSONObject ajaxProjectList(@RequestParam(value="type",required=true)String type,
			@RequestParam(value="pageSize",required=false,defaultValue="1")Integer pageSize,
			@RequestParam(value="pageNum",required=false,defaultValue="10")Integer pageNum){
		JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
        ApiNews apiNews = new ApiNews();
		apiNews.setNews_column(type);
		apiNews.setOrderBy("id");
		apiNews.setOrderDirection("desc");
		ApiPage<ApiNews> page = newsFacade.queryNewsList(apiNews, pageSize, pageNum);
		List<ApiNews> list = page.getResultData();
		for(ApiNews an:list){
			StringBuilder builder = new StringBuilder();
			if(an.getContentImageId() != null && !"".equals(an.getContentImageId())){
				String contentImageIds[] = an.getContentImageId().split(",");
				ApiBFile apiBfileNews = new ApiBFile();
				for (String imgId : contentImageIds) {
					if(!"".equals(imgId)){
						apiBfileNews = commonFacade.queryBFileById(Integer.valueOf(imgId));
						if(apiBfileNews != null && !"video".equals(apiBfileNews.getCategory())){
							builder.append(apiBfileNews.getUrl()+",");
						}
					}
				}
			}
			JSONObject item = new JSONObject();
			item.put("id", an.getId());
			item.put("title",an.getTitle());
			item.put("addTime", an.getCreatetime());
			item.put("imgUrl", an.getCoverImageUrl());
			item.put("contentImgUrl", builder);
			items.add(item);
		}
		data.put("obj", items);
		data.put("total", page.getTotal());
		data.put("size", list.size());
		return data;
	}
	
	//detail
	@RequestMapping("newsDetail_view")
	public ModelAndView newsDetail_view(@RequestParam(value="id",required=true)Integer id,
			@RequestParam(value="type",required=true)String type){
		ModelAndView view = new ModelAndView("h5/news/detail");
		ApiNews news = newsFacade.queryNewsDetail(id);
		StringBuilder builder = new StringBuilder();
		if(news.getContentImageId() != null && !"".equals(news.getContentImageId())){
			String contentImageIds[] = news.getContentImageId().split(",");
			ApiBFile apiBfileNews = new ApiBFile();
			for (String imgId : contentImageIds) {
				if(!"".equals(imgId)){
					apiBfileNews = commonFacade.queryBFileById(Integer.valueOf(imgId));
					if(apiBfileNews != null && "video".equals(apiBfileNews.getCategory())){
						builder.append(apiBfileNews.getUrl().replace("http://res.17xs.org/picture/", "")+",");
					}
				}
			}
		}
		view.addObject("videos", builder);
		view.addObject("news", news);
		view.addObject("type", type);
		return view;
	}

}
