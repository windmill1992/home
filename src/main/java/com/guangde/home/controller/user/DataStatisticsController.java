package com.guangde.home.controller.user;

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.*;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.pojo.ApiPage;
import com.redis.utils.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("data")
public class DataStatisticsController {

	private final Logger logger = LoggerFactory.getLogger(DataStatisticsController.class);
	@Autowired
	private IUserFacade userFacade;
	@Autowired
	private IDonateRecordFacade donateRecordFacade;
	@Autowired
	private IGoodLibraryFacade goodLibraryFacade;
	@Autowired
	private ICommonFacade CommonFacade;
	@Autowired
	private RedisService redisService;
	
	@RequestMapping(value = "dataStatistics_index")
	public ModelAndView dataStatistics_index() {
		ModelAndView view = new ModelAndView("dataStatistics/dataStatictics");
		try {
		//善行排行榜
		Object objChartsNum = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_CHARTS_NUM);
		Object objChartsMoney = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_CHARTS_MONEY);
		Object objChartsTime = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_CHARTS_TIME);
		if(objChartsNum!=null && objChartsMoney!=null && objChartsTime!=null){
			view.addObject("num", objChartsNum);
			view.addObject("money", objChartsMoney);
			view.addObject("time", objChartsTime);
		}else{
			ApiDataStatistics apiDataStatistics = new ApiDataStatistics();
			apiDataStatistics.setSource("全部");
			ApiPage<ApiDataStatistics> page = userFacade.queryDataStatisticsList(apiDataStatistics, 1, 30);
			StringBuilder money= new StringBuilder(),time= new StringBuilder(),num=new StringBuilder();
			
			for(int i=29;i>=0;i--){
				if(i==29){
					money.append(page.getResultData().get(i).getDonatedMoney());
					time.append(DateUtil.dateStringChinesePatternYD("yyyy-MM-dd HH:mm:ss", page.getResultData().get(i).getCreateTime()));
					num.append(page.getResultData().get(i).getDonationPeopleNum());
				}
				else{
					money.append(","+page.getResultData().get(i).getDonatedMoney());
					time.append(","+DateUtil.dateStringChinesePatternYD("yyyy-MM-dd HH:mm:ss",page.getResultData().get(i).getCreateTime()));
					num.append(","+page.getResultData().get(i).getDonationPeopleNum());
				}
			}
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_CHARTS_NUM, num, DateUtil.DURATION_DAY_S);
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_CHARTS_MONEY, money, DateUtil.DURATION_DAY_S);
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_CHARTS_TIME, time, DateUtil.DURATION_DAY_S);
			view.addObject("num", num);
			view.addObject("money", money);
			view.addObject("time", time);
		}
		//累计捐款
		Integer Itotal =(userFacade.countDonateAmount(true)).intValue();
		view.addObject("Itotal", Itotal);
		ApiDonateRecord param = new ApiDonateRecord();
		param.setDonateCopies(null);
		//上周捐款总额
		Object objWeekTotalDonate = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_WEEK_MONEY);
		if(objWeekTotalDonate!=null){
			view.addObject("weekTotalDonate", objWeekTotalDonate);
		}
		else{
			ApiPage<ApiDonateRecord> pageWeekTotal = donateRecordFacade.countDonateByWeek(param,1, 10);
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_WEEK_MONEY, pageWeekTotal.getResultData().get(0).getDonatAmount(), DateUtil.DURATION_DAY_S*7);
			view.addObject("weekTotalDonate", pageWeekTotal.getResultData().get(0).getDonatAmount());
		}
		//上月捐款总额
		Object objMonthTotalDonate = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_MONTH_MONEY);
		if(objMonthTotalDonate!=null){
			view.addObject("monthTotalDonate", objMonthTotalDonate);
		}
		else{
			ApiPage<ApiDonateRecord> pageMonthTotal = donateRecordFacade.countDonateByMonth(param,1, 10);
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_MONTH_MONEY, pageMonthTotal.getResultData().get(0).getDonatAmount(), DateUtil.DURATION_DAY_S*30);
			view.addObject("monthTotalDonate", pageMonthTotal.getResultData().get(0).getDonatAmount());
		}
		
		//上年度捐款总额
		Object objYearTotalDonate = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_YEAR_MONEY);
		if(objYearTotalDonate!=null){
			view.addObject("yearTotalDonate", objYearTotalDonate);
		}
		else{
			ApiPage<ApiDonateRecord> pageYearTotal = donateRecordFacade.countDonateByYear(param,1, 10);
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_YEAR_MONEY, pageYearTotal.getResultData().get(0).getDonatAmount(), DateUtil.DURATION_DAY_S*365);
			view.addObject("yearTotalDonate", pageYearTotal.getResultData().get(0).getDonatAmount());
		}
		} catch (Exception e) {
		}
		return view;
		
	}
	
	/**
	 * 加载上周、上月排行
	 */
	@RequestMapping(value="loadWeekDonate")
	@ResponseBody
	public Map<String, Object> loadWeekDonate(){
		//上周排行
		Object objWeek = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_WEEK_LIST);
		//上月排行
		Object objMonth = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_MONTH_LIST);
		if(objWeek!=null && objMonth!=null){
			return webUtil.successResDoubleObject(objWeek,objMonth);
		}
		else{
			ApiDonateRecord param =new ApiDonateRecord();
			param.setDonateCopies(1);
			//上周排行
			ApiPage<ApiDonateRecord> pageWeek = donateRecordFacade.countDonateByWeek(param,1, 10);
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_WEEK_LIST, pageWeek.getTotal()>0?pageWeek.getResultData():null, DateUtil.DURATION_DAY_S*7);
			//上月排行
			ApiPage<ApiDonateRecord> pageMonth = donateRecordFacade.countDonateByMonth(param,1, 10);
			redisService.saveObjectData(PengPengConstants.DATASTATISTICS_MONTH_LIST, pageMonth.getTotal()>0?pageMonth.getResultData():null, DateUtil.DURATION_DAY_S*30);
			return webUtil.successResDoubleObject(pageWeek.getTotal()>0?pageWeek.getResultData():null,pageMonth.getTotal()>0?pageMonth.getResultData():null);
		}
	}
	
	/**
	 * 加载本年、全部排行
	 */
	@RequestMapping(value="loadYearDonate")
	@ResponseBody
	public Map<String, Object> loadYearDonate(){
		ApiPage<ApiDonateRecord> pageYear = null;
		ApiPage<ApiDonateRecord> page2 = null;
		//上年度排行
		Object objYear = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_YEAR_LIST);
		//全部排行
		Object objTotal = redisService.queryObjectData(PengPengConstants.DATASTATISTICS_TOTAL_LIST);
		if(objYear!=null && objTotal!=null){
			return webUtil.successResDoubleObject(objYear,objTotal);
		}
		else{
			if(objYear == null){
				ApiDonateRecord param =new ApiDonateRecord();
				param.setDonateCopies(1);
				//上年度排行
				pageYear = donateRecordFacade.countDonateByYear(param,1, 10);
				redisService.saveObjectData(PengPengConstants.DATASTATISTICS_YEAR_LIST, pageYear.getTotal()>0?pageYear.getResultData():null, DateUtil.DURATION_HOUR_S*365);
			}

			if(objTotal == null){
				//全部排行
				ApiDonateRecord donate = new ApiDonateRecord();
				donate.setState(302);
				page2 = donateRecordFacade.queryByDonate(donate, 1, 10);
				redisService.saveObjectData(PengPengConstants.DATASTATISTICS_TOTAL_LIST, page2.getTotal()>0?page2.getResultData():null, DateUtil.DURATION_DAY_S);
			}

			return webUtil.successResDoubleObject(pageYear==null?objYear:pageYear.getResultData(),page2==null?objTotal:page2.getResultData());
		}
	}
	
	/**
	 * 加载爱心企业、善管家
	 */
	@RequestMapping(value="loadCompany")
	@ResponseBody
	public Map<String, Object> loadCompany(){
		//爱心企业
		ApiGoodLibrary gl = new ApiGoodLibrary();
		gl.setState(201);
		gl.setOrderBy("usedmoney");
		gl.setOrderDirection("DESC");
		ApiPage<ApiGoodLibrary> list = goodLibraryFacade.queryByParam(gl,1,30);
		//善管家
		ApiFrontUser user = new ApiFrontUser();
		user.setLoveGroupMent(1);
		user.setLoveState(203);
		ApiPage<ApiFrontUser> apfronUsers = userFacade.queryUserList(user,1, 30);
		List<ApiFrontUser> users = apfronUsers.getResultData();
		if (users != null && users.size() > 0) {
			for (ApiFrontUser fu : users) {

				if (fu.getLogoId() != null) {
					ApiBFile apiBfileNews = CommonFacade.queryBFileById(fu.getLogoId());
					if (apiBfileNews != null) {
						fu.setCoverImageUrl(apiBfileNews.getUrl());
					}
				}
			}
		}
		return webUtil.successResDoubleObject(list.getResultData(),users);
	}
	
}
