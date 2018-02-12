package com.guangde.home.controller.goodlibrary;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
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
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiGoodLibrary;
import com.guangde.entry.ApiGoodLibraryProple;
import com.guangde.entry.BaseBean;
import com.guangde.home.controller.user.UserRelationInfoController;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.MathUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.pojo.ApiPage;
import com.redis.utils.RedisService;
@Controller
@RequestMapping("myGoodLibrary")
public class MyGoodLibraryController {
	
	private final Logger logger = LoggerFactory.getLogger(UserRelationInfoController.class);
	private static final String phonecodeprex = "phonecode_r_";
	@Autowired
	private IGoodLibraryFacade goodLibraryFacade;
	@Autowired
	private RedisService redisService;
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IRedPacketsFacade redPacketsFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private ICommonFacade CommonFacade;
	@Autowired
	private IProjectFacade projectFacade;
	
	@Autowired 
	private IFileFacade fileFacade;
	
	@Autowired 
	private ICompanyFacade CompanyFacade;
	
	@RequestMapping("gotoMyGoodLibraryDetails")
	public ModelAndView  gotoMyGoodLibraryDetails(@RequestParam(value="id",required=true)Integer libraryId,
			HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv=new ModelAndView();
		Integer userId = UserUtil.getUserId(request, response);
    	if(userId==null){
    		mv.setViewName("redirect:/user/sso/login.do");
			mv.addObject("entrance", "http://www.17xs.org/myGoodLibrary/gotoMyGoodLibraryDetails.do?id="+libraryId);
    		return mv;
    	}
		ApiGoodLibraryProple apiGoodLibraryProple1=new ApiGoodLibraryProple();
		apiGoodLibraryProple1.setId(libraryId);
		//暂时考虑一个用户只能创建一个善库
		ApiGoodLibrary goodLibrary = goodLibraryFacade.selectById(libraryId);
		//ApiGoodLibraryProple apiGoodLibraryProple=goodLibraryFacade.selectByParam(apiGoodLibraryProple1);
		//根据用户id去查询用户的信息
		ApiFrontUser frontUser=userFacade.queryById(goodLibrary.getUserId());
		mv.addObject("frontUser", frontUser);
		if(goodLibrary!=null){
			//项目个数
			Integer goodlibraryProNum = donateRecordFacade.
					countProjectNumByUserId(goodLibrary.getUserId());
			mv.addObject("goodlibraryProNum", goodlibraryProNum);
		}
		//善园家族的头像
		String logoUrl=null;
		if(goodLibrary.getLogoId()!=null&&!goodLibrary.getLogoId().equals(0)){
			ApiBFile apiBFile= CommonFacade.queryBFileById(goodLibrary.getLogoId());
			if(apiBFile!=null){
				logoUrl=apiBFile.getUrl();
			}	
		}
		mv.addObject("logoUrl",logoUrl);
		//带动捐款
		Double money=0.0;
		ApiDonateRecord donate = new ApiDonateRecord();
		//donate.setGoodLibraryId(libraryId);
		donate.setState(302);
		ApiGoodLibraryProple param = new ApiGoodLibraryProple();
		param.setLibraryId(libraryId);
		ApiPage<ApiGoodLibraryProple> list = goodLibraryFacade.queryByParam(param, 1, 500);
		if(list.getTotal()>0){
			for(ApiGoodLibraryProple model:list.getResultData()){
				donate.setUserId(model.getUserId());
				money=MathUtil.add(money, donateRecordFacade.countQueryByCondition(donate));
			}
		}
		money = MathUtil.sub(money, goodLibrary.getUsedmoney());
		if(money<0)
			money=0.0;
		//根据userid查询捐款总数 donateRecordFacade
		/*ApiGoodLibraryProple apigp = goodLibraryFacade.countByGoodLibraryId(libraryId);
		double daiDongMoney=0.0;
		if(apigp!=null){
			if(apigp.getTotalDonateMoney()==null)
				apigp.setTotalDonateMoney(0.0);
			daiDongMoney=MathUtil.sub(apigp.getTotalDonateMoney(),goodLibrary.getUsedmoney());
		}
		if(daiDongMoney<0){
			daiDongMoney=0;
		}*/
		mv.addObject("daiDongMoney", money);
		mv.addObject("libraryId", libraryId);
		mv.addObject("goodLibrary", goodLibrary);
		mv.setViewName("goodlibrary/my_goodlibrary");
		return mv;
	}
	@RequestMapping("myGoodLibraryDetails")
	@ResponseBody
	public Map<String,Object>myGoodLibraryDetails(@RequestParam(value="id",required=true)Integer libraryId,Integer pageNum,Integer pageSize){
		ApiGoodLibraryProple goodLibraryProple=new ApiGoodLibraryProple();
		goodLibraryProple.setLibraryId(libraryId);
		ApiPage<ApiGoodLibraryProple> apList=goodLibraryFacade.queryByParam(goodLibraryProple, pageNum, pageSize);
		List<ApiGoodLibraryProple> list=apList.getResultData();
		JSONObject data=new JSONObject();
		JSONArray  items=new JSONArray();
		DecimalFormat df = new DecimalFormat("######0.00"); 
		if(list.size()>0){
			for (ApiGoodLibraryProple apiGoodLibraryProple:list) {
				if(apiGoodLibraryProple.getLibraryId()!=null){
					ApiFrontUser frontUser=userFacade.queryById(apiGoodLibraryProple.getUserId());
					//行善的总的次数
					ApiDonateRecord apiDonateRecord=new ApiDonateRecord();
					apiDonateRecord.setUserId(apiGoodLibraryProple.getUserId());
					ApiPage<ApiDonateRecord> aplist=donateRecordFacade.queryDonateRecordByParam(apiDonateRecord, 1, 10);
					//查询某个用户的捐款金额
					ApiDonateRecord donateRecord=new ApiDonateRecord();
					donateRecord.setUserId(apiGoodLibraryProple.getUserId());
					donateRecord.setState(302);
					double donateMoney=donateRecordFacade.countQueryByCondition(donateRecord);	
					//根据用户id统计善库充值的记录数
					ApiCapitalinout capitalinout=new ApiCapitalinout();
					capitalinout.setUserId(apiGoodLibraryProple.getUserId());
					capitalinout.setPayType("善库充值");
					capitalinout.setPayState(302);
					long  chargeNum=0;
					chargeNum=donateRecordFacade.countChargeNumByParam(capitalinout);
					int donateNum=0;
					if(aplist!=null){
						donateNum=(int)aplist.getTotal();
					}
					JSONObject item=new JSONObject();
					item.put("userId", frontUser.getId());
					item.put("userName", frontUser.getNickName());
					item.put("mobileNum",frontUser.getMobileNum());
					item.put("familyAddress", apiGoodLibraryProple.getFamilyAddress());
					item.put("donateNum", donateNum);
					item.put("createTime", DateUtil.dateStringChinese(" yyyy-MM-dd",apiGoodLibraryProple.getCreatetime()));
					item.put("balance", apiGoodLibraryProple.getBalance());
					item.put("donateAmountSum", donateMoney);
					item.put("chargeNum", chargeNum);
					items.add(item);
				}
			}
			data.put("items",items);
			data.put("total", apList.getPages());
			return webUtil.resMsg(1,"0001", "成功", data);
		}else{
			return webUtil.resMsg(0,"0001", "暂无善库成员",null);
		}	
	}
	
	@RequestMapping("goodLibraryUserDonationlist")
    @ResponseBody
    public Map<String, Object> donationlist(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "id", required = false) Integer id,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize)
    {
        
        if (id == null)
        {
            return webUtil.resMsg(0, "0001", "用户id不能为空", null);
        }
        //分页计算
        JSONObject data=new JSONObject();
        JSONArray items=new JSONArray();
        ApiDonateRecord r = new ApiDonateRecord();
        r.setUserId(id);
        r.setState(302);
        /*List<String> llist = new ArrayList<String>(1);
        llist.add(ApiDonateRecord.getCacheRange(r.getClass().getName(), BaseBean.RANGE_WHOLE, id));
        r.initCache(true, DateUtil.DURATION_MIN_S, llist, "projectId", "state");*/
        ApiPage<ApiDonateRecord> resultPage = donateRecordFacade.queryByCondition(r, page, pageSize);
        
        if (resultPage.getTotal() == 0)
        {
        	return webUtil.resMsg(0, "0001", "暂无数据", null);
        }
        else
        {   data.put("total", resultPage.getTotal());
            
            for (ApiDonateRecord dr : resultPage.getResultData())
            {          JSONObject item=new JSONObject();
              	    //捐款记录
	                if("individualUsers".equals(dr.getDonatType())){
	                	item.put("donateType", "个人捐款");
	                }else if("enterpriseDonation".equals(dr.getDonatType())){
	                	item.put("donateType", "企业捐款");
	                }else{
	                	item.put("donateType", "游客捐款");  //表示游客捐款
	                }
	                item.put("id", dr.getId());
	                item.put("donatAmount", dr.getDonatAmount());
	                item.put("dTime", dr.getDonatTime());
	                if (dr.getDonateCopies() != null)
	                {
	                    item.put("copies", dr.getDonateCopies());
	                }
	                String name = dr.getNickName();
	                if(!StringUtils.isEmpty(name) && name.contains("游客")){
	                	name="爱心人士";
	                	if (!StringUtils.isEmpty(dr.getTouristMessage())){
	                        JSONObject dataJson = (JSONObject)JSONObject.parse(dr.getTouristMessage());
	                        name = dataJson.getString("name");
	                        if (StringUtils.isEmpty(name)){
	                            name = "爱心人士";
	                        }
	                        item.put("imageurl", dataJson.getString("headimgurl"));
	                    }
	                }else {
	                	if(dr.getCoverImageId() >0){
	                		ApiBFile bFile = fileFacade.queryBFileById(dr.getCoverImageId());
	                    	item.put("imageurl", bFile.getUrl());
	                	}
					}
	                item.put("name", name);
	                item.put("title", dr.getProjectTitle());
	                if(dr.getLeaveWord()==null){
	                	item.put("leaveWord",null);
	                }else if (dr.getLeaveWord().contains("transfer:")) {
	                	item.put("leaveWord",dr.getLeaveWord().replace("transfer:", ""));
	                	item.put("dType",6);
					}else {
						item.put("leaveWord",dr.getLeaveWord());
					}
	                
	                int d_value = DateUtil.minutesBetween(dr.getDonatTime(),
							DateUtil.getCurrentTimeByDate());
					Boolean flag = DateUtil.dateFormat(dr.getDonatTime()).equals(
							DateUtil.dateFormat(DateUtil.getCurrentTimeByDate()));
					if (d_value / 60 > 24 || !flag) {
						dr.setTimeStamp(DateUtil.dateStringChinesePatternYD("yyyy年MM月dd日",dr.getDonatTime()));
					} else {
						if (d_value / 60 >= 1) {
							dr.setTimeStamp(d_value / 60 + "小时前");
						} else {
							if (d_value == 0) {
								dr.setTimeStamp("刚刚");
							} else {
								dr.setTimeStamp(d_value + "分钟前");
							}
						}
					}
	                item.put("showTime", dr.getTimeStamp());
	                items.add(item);
            }
        }
        	data.put("items",items);
        	data.put("page", resultPage.getPageNum());// 当前页码
            data.put("pageNum", resultPage.getPageSize()); // 每页行数
            data.put("total", resultPage.getPages());// 总页数
        	return webUtil.successRes(data);
    }
	
	@RequestMapping("shankuRecharge")
	@ResponseBody
	public Map<String,Object>shankuRecharge(Integer shanKuUserId,Integer libraryId,String chargeMoney){
		
		ApiCapitalinout capitalinout=new ApiCapitalinout();
		capitalinout.setTranNum(StringUtil.uniqueCode());
		Integer ret=CompanyFacade.updateShankuRecharge(shanKuUserId, libraryId, chargeMoney, capitalinout);
		if(ret==1){
			return	webUtil.resMsg(1, "0001", "成功", null);
		}
		return webUtil.resMsg(0, "0002", "失败",null);
	}
}
