package com.guangde.home.controller.project;

import java.util.ArrayList;
import java.util.List;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiProjectTopic;
import com.guangde.home.utils.StringUtil;
import com.guangde.pojo.ApiPage;

@Controller
@RequestMapping("projectTopic")
public class H5ProjectTopicController {
	Logger logger = LoggerFactory.getLogger(H5ProjectTopicController.class);
	
	@Autowired
	private IProjectFacade projectFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	
	/**
	 * goto专题详情页
	 * @return
	 */
	@RequestMapping("gotoProjectTopicDetail")
	public ModelAndView gotoProjectTopicDetail(@RequestParam(value="id",required=true)Integer id){
		ModelAndView view = new ModelAndView();
		view.setViewName("h5/projectTopic/projectTopicDetail");
		ApiProjectTopic apiProjectTopic = projectFacade.selectProjectTopicById(id);
		ApiDonateRecord donateRecord = new ApiDonateRecord();
		if(apiProjectTopic!=null){
			List<Integer> ids = new ArrayList<Integer>();
			String[] projectIds =apiProjectTopic.getProjectIds().replaceAll("，", ",").split(",");
			for (int i = 0; i < projectIds.length; i++) {
				ids.add(Integer.valueOf(projectIds[i]));
			}
			donateRecord.setIdList(ids);
			donateRecord.setState(302);
		}
		ApiDonateRecord apiDonateRecord = donateRecordFacade.queryCompanyCenter(donateRecord);
		view.addObject("topic", apiProjectTopic);
		view.addObject("donate", apiDonateRecord);
		return view;
	}
	
	/**
	 * 初始加载和加载更多专题项目
	 * 
	 */
	@ResponseBody
	@RequestMapping("ajaxProjectTopic")
	public JSONObject ajaxProjectTopic(@RequestParam(value="id",required=true)Integer id,HttpServletRequest request,
			 HttpServletResponse response,@RequestParam(value="pageNum",required=false)int pageNum,
			 @RequestParam(value="pageSize",required=false)int pageSize){
		//Integer userId=UserUtil.getUserId(request, response);
		JSONObject data = new JSONObject();
        JSONArray items = new JSONArray();
		try {
			List<Integer> ids = new ArrayList<Integer>();
			 ApiProjectTopic ap=new ApiProjectTopic();
			 ap = projectFacade.selectProjectTopicById(id);
			 if(ap!=null&&ap.getProjectIds()!=""&&ap.getProjectIds()!=null){
				 String[] projectIds = ap.getProjectIds().split(",");
				 for (int i = 0; i < projectIds.length; i++) {
					ids.add(Integer.valueOf(projectIds[i]));
				}
				 ApiProject project = new ApiProject();
				 project.setIds(ids);
		         ApiPage<ApiProject> apList=projectFacade.queryProjectByIds(project, pageNum, pageSize);
		         List<ApiProject> list = apList.getResultData();
		         if(list.size()==0){
		        	 //无数据
		        	 data.put("result", 1);
		         }
		         else{
		        	 for(ApiProject projectinfo:list){
						 JSONObject item = new JSONObject();
						 item.put("coverImageUrl", projectinfo.getCoverImageUrl());
						 /*if(projectinfo.getTitle().length()>30){
							 projectinfo.setTitle(projectinfo.getTitle().substring(0, 30)+"...");
						 }*/
						 item.put("title", projectinfo.getTitle());
						 item.put("cryMoney", projectinfo.getCryMoney());
						 item.put("donatAmount", projectinfo.getDonatAmount());
						 //item.put("donatePercent", project.getDonatePercent());
						 //item.put("lastUpdateTime", DateUtil.parseToFormatDateString(project.getLastUpdateTime(),"yyyy-MM-dd"));
						 item.put("id", projectinfo.getId());
						 String stringContent = StringUtil.delHTMLTag(projectinfo.getContent());
						 if(stringContent.length()>25){
							 projectinfo.setContent(stringContent.substring(0, 25)+"...");
						 }
						 String stringSubTitle=projectinfo.getSubTitle();
						 if(stringSubTitle!=null&&!"".equals(stringSubTitle)&&stringSubTitle.length()>25){
							 stringSubTitle=stringSubTitle.substring(0, 25)+"...";
						 }
						 item.put("content", projectinfo.getContent());
						 item.put("subTitle", stringSubTitle==null?"":stringSubTitle);
						 item.put("peopleNum", projectinfo.getDonationNum());
						 items.add(item);
					}
					data.put("result",0);
					data.put("total",apList.getTotal());
					data.put("items", items);
		         }
			 }
			 else{
				//无数据
	        	 data.put("result", 1);
			 }
			 	
		} catch (Exception e) {
			data.put("result", 2);
		}
		return data;
	}
}
