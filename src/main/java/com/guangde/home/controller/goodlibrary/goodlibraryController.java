package com.guangde.home.controller.goodlibrary;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IGoodLibraryFacade;
import com.guangde.api.user.IRedPacketsFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiBFile;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiConfig;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiGoodLibrary;
import com.guangde.entry.ApiGoodLibraryProple;
import com.guangde.entry.ApiProject;
import com.guangde.entry.ApiRedpackets;
import com.guangde.home.controller.user.UserRelationInfoController;
import com.guangde.home.utils.CodeUtil;
import com.guangde.home.utils.CommonUtils;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.MathUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.utils.webUtil;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.home.vo.common.Page;
import com.guangde.home.vo.user.PUser;
import com.guangde.pojo.ApiPage;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;
import com.tenpay.utils.MD5Util;

@Controller
@RequestMapping("goodlibrary")
public class goodlibraryController {
	private final Logger logger = LoggerFactory.getLogger(UserRelationInfoController.class);
	private static final String phonecodeprex = "phonecode_r_";
	private static final String imgcodeprex = "imgcode_r_";
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
	private ICompanyFacade companyFacade;
	@RequestMapping("index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "lirbraryId", required = true) Integer lirbraryId){
		ModelAndView view = new ModelAndView("h5/goodlibrary/goodlibrary_index");
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
					logger.error("微信登陆出现问题"+ e);
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
				 ApiFrontUser user1 = userFacade.queryById(userId);
					ApiGoodLibrary apiGoodLibrary = goodLibraryFacade.selectById(lirbraryId);
					String[] tagsData=null;
					String tags=null;
					List<String> taglist = new ArrayList<String>();
					if(apiGoodLibrary.getTag()!=null){
						tagsData = apiGoodLibrary.getTag().split(",");
						for(int i=0;i<tagsData.length;i++){
							if(i==0)
								tags=tagsData[i];
							else{
								tags=tags+","+tagsData[i];
							}
							taglist.add(tagsData[i]);
						}
					}
					String logoUrl=null;
					if(apiGoodLibrary.getLogoId()!=null&&apiGoodLibrary.getLogoId().equals(0)){
						ApiBFile apiBFile= CommonFacade.queryBFileById(apiGoodLibrary.getLogoId());
						logoUrl=apiBFile.getUrl();
					}
					
					
					ApiGoodLibrary goodLibrary = new ApiGoodLibrary();
					goodLibrary.setId(lirbraryId);
					//暂时考虑一个用户只能创建一个善库
					ApiPage<ApiGoodLibrary> list = goodLibraryFacade.queryByParam(goodLibrary,1,20);
					view.addObject("goodLibrary", list.getResultData().get(0));
					
					//善库指定标签的相关项目数
					ApiProject project = new ApiProject();
					project.setTaglist(taglist);
					Integer Num = projectFacade.countProjectNumByTags(project);
					
					//项目个数
					Integer goodlibraryProNum = donateRecordFacade.
							countProjectNumByUserId(apiGoodLibrary.getUserId());
					//用户的普通捐款统计排除红包和善库（金额、次数、项目数）
					ApiDonateRecord apidr = donateRecordFacade.countDonateByUser(apiGoodLibrary.getUserId());
					//善库的捐款金额、次数、号召人数
					ApiGoodLibraryProple apigp = goodLibraryFacade.
							countByGoodLibraryId(list.getResultData().get(0).getId());
					//用户红包的使用个数和金额
					ApiRedpackets apiRed = redPacketsFacade.countUserdNumAndUserdMoney(apiGoodLibrary.getUserId());
					//总的捐赠项目
					int totalProjectNum = goodlibraryProNum;
					//号召人数
					int totalPeople = apigp.getTotalPeople();
					//行善次数
					int usednum =0;
					double usedamount=0.0;
					int donateNum=0;
					int ttotalDonateNum=0;
					double totalAmount=0.0;
					double totalDonateMone=0.0;
					DecimalFormat df = new DecimalFormat("######0.00"); 
					if(apiRed!=null){
						usednum = apiRed.getUsednum();
						usedamount = apiRed.getUsedamount();
					}
					if(apidr!=null){
						donateNum=apidr.getDonateNum();
						if(apidr.getTotalAmount()!=null){
						totalAmount=apidr.getTotalAmount();
						}
					}
					if(apigp!=null){
						ttotalDonateNum=apigp.getTotalDonateNum();
						totalDonateMone=apigp.getTotalDonateMoney();
					}
					int totalDonateNum = donateNum + ttotalDonateNum + usednum;
					//捐款金额
					String totalMoney = df.format(totalAmount + totalDonateMone + usedamount);
					
					
					//前10个善库成员的头像
					List<ApiFrontUser> headimgs = userFacade.queryByIdList(lirbraryId);
					
					view.addObject("user", user1);
					view.addObject("goodlibrary", apiGoodLibrary);
					view.addObject("totalProjectNum", totalProjectNum);
					view.addObject("totalPeople", totalPeople);
					view.addObject("totalDonateNum", totalDonateNum);
					view.addObject("totalMoney", totalMoney);
					view.addObject("tags", tags);
					view.addObject("Num", Num);
					view.addObject("headimgs", headimgs);
					view.addObject("logoImg", logoUrl);
					view.addObject("libraryUserId", apiGoodLibrary.getUserId());
					view.addObject("nowDate",new Date());
			}else {
				view = new ModelAndView("h5/goodlibrary/goodlibrary_index");
				
				return view ; 
			}
		}else {
			ApiFrontUser user = userFacade.queryById(userId);
			ApiGoodLibrary apiGoodLibrary = goodLibraryFacade.selectById(lirbraryId);
			String[] tagsData=null;
			String tags=null;
			List<String> taglist = new ArrayList<String>();
			if(apiGoodLibrary.getTag()!=null){
				tagsData = apiGoodLibrary.getTag().split(",");
				for(int i=0;i<tagsData.length;i++){
					if(i==0)
						tags=tagsData[i];
					else{
						tags=tags+","+tagsData[i];
					}
					taglist.add(tagsData[i]);
				}
			}
			String logoUrl=null;
			if(apiGoodLibrary.getLogoId()!=null&&!apiGoodLibrary.getLogoId().equals(0)){
				ApiBFile apiBFile= CommonFacade.queryBFileById(apiGoodLibrary.getLogoId());
				logoUrl=apiBFile.getUrl();
			}
			
			
			ApiGoodLibrary goodLibrary = new ApiGoodLibrary();
			goodLibrary.setId(lirbraryId);
			//暂时考虑一个用户只能创建一个善库
			ApiPage<ApiGoodLibrary> list = goodLibraryFacade.queryByParam(goodLibrary,1,20);
			view.addObject("goodLibrary", list.getResultData().get(0));
			
			//善库指定标签的相关项目数
			ApiProject project = new ApiProject();
			project.setTaglist(taglist);
			Integer Num = projectFacade.countProjectNumByTags(project);
			
			//项目个数
			Integer goodlibraryProNum = donateRecordFacade.
					countProjectNumByUserId(apiGoodLibrary.getUserId());
			//用户的普通捐款统计排除红包和善库（金额、次数、项目数）
			ApiDonateRecord apidr = donateRecordFacade.countDonateByUser(apiGoodLibrary.getUserId());
			//善库的捐款金额、次数、号召人数
			ApiGoodLibraryProple apigp = goodLibraryFacade.
					countByGoodLibraryId(list.getResultData().get(0).getId());
			//用户红包的使用个数和金额
			ApiRedpackets apiRed = redPacketsFacade.countUserdNumAndUserdMoney(apiGoodLibrary.getUserId());
			//总的捐赠项目
			int totalProjectNum = goodlibraryProNum;
			//号召人数
			int totalPeople = apigp.getTotalPeople();
			//行善次数
			int usednum =0;
			double usedamount=0.0;
			int donateNum=0;
			int ttotalDonateNum=0;
			double totalAmount=0.0;
			double totalDonateMone=0.0;
			DecimalFormat df = new DecimalFormat("######0.00"); 
			if(apiRed!=null){
				usednum = apiRed.getUsednum();
				usedamount = apiRed.getUsedamount();
			}
			if(apidr!=null){
				donateNum=apidr.getDonateNum();
				if(apidr.getTotalAmount()!=null){
				totalAmount=apidr.getTotalAmount();
				}
			}
			if(apigp!=null && apigp.getTotalDonateNum()!=null){
				ttotalDonateNum=apigp.getTotalDonateNum();
				totalDonateMone=apigp.getTotalDonateMoney();
			}
			int totalDonateNum = donateNum + ttotalDonateNum + usednum;
			//捐款金额
			String totalMoney = df.format(totalAmount + totalDonateMone + usedamount);
			
			
			//前10个善库成员的头像
			List<ApiFrontUser> headimgs = userFacade.queryByIdList(lirbraryId);
			
			view.addObject("user", user);
			view.addObject("goodlibrary", apiGoodLibrary);
			view.addObject("totalProjectNum", totalProjectNum);
			view.addObject("totalPeople", totalPeople);
			view.addObject("totalDonateNum", totalDonateNum);
			view.addObject("totalMoney", totalMoney);
			view.addObject("tags", tags);
			view.addObject("Num", Num);
			view.addObject("headimgs", headimgs);
			view.addObject("logoImg", logoUrl);
			view.addObject("libraryUserId", apiGoodLibrary.getUserId());
			view.addObject("nowDate",new Date());
		}
    	
    	return view;
	}
	
	
	/**
	 * 获取手机验证码
	 * 
	 * @param type
	 * @param phone
	 * @param code
	 * @param request
	 * @return
	 */
	/*@RequestMapping(value = "phoneCode", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> phoneCode(@RequestParam(value = "phone", required = false) String phone,
			HttpServletRequest request) {
		String tempType = "phonecode_r_";
		// 发送验证码
		if (StringUtil.isMobile(phone)) {
			String phonekey = tempType + phone;
			StoreManage storeManage = StoreManage.create(
					StoreManage.STROE_TYPE_SESSION, request.getSession());
			String result = CodeUtil.phoneCode(phonekey, phone, storeManage);
			if (result == null) {
				return webUtil.resMsg(0, "0003", "发送失败", null);
			}
		} else {
			return webUtil.resMsg(0, "0002", "手机格式错误", null);
		}

		return webUtil.resMsg(1, "0000", "成功", null);
	}*/
	
	/**
	 * 手机号绑定用户
	 * @param hUser
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bindUser")
	@ResponseBody
	public Map<String, Object> bindUser(@ModelAttribute PUser hUser,@RequestParam(value = "lirbraryId", required = false) Integer lirbraryId,
			@RequestParam(value="phoneCode",required=true)String phoneCode,
    		@RequestParam(value="code",required=true)String code,
			HttpServletRequest request, HttpServletResponse response) {
		// msg:0000成功，0001手机格式不对,0003密码格式错误，004无法识别用户身份
		// msg:0005验证码错误,0006手机验证码错误
		if (logger.isDebugEnabled()) {
			logger.debug("手机号绑定用户，开通善库信息：" + hUser.toString());
		}
		String msg = "0000";
		String phonekey = null;
		int codeR;
		StoreManage storeManage = StoreManage.create(StoreManage.STROE_TYPE_SESSION, request.getSession());
		String codekey = getImgCode(request);
		
		codeR = CodeUtil.VerifiCode(codekey,storeManage,code,true);
		if (codeR == -1) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码过期", null);
		} else if (codeR == 0) {
			msg = "0005";
			return webUtil.resMsg(0, msg, "验证码错误", null);
		}
		// 1.校验参数
		ApiFrontUser user = userFacade.queryById(hUser.getId());
		user.setMobileState(201);
		if (isPhoneUser(hUser)){
			// 手机注册用户
			if (StringUtil.isMobile(hUser.getPhone())) {
				// 校验手机验证码
				phonekey = CodeUtil.certificationprex + hUser.getPhone();

				 codeR = CodeUtil.VerifiCode(phonekey, storeManage,
						 phoneCode, true);
				 if(codeR==-1){
				 return webUtil.resMsg(0, "0005","手机验证码过期", null);
				 }else if(codeR==0){
				 return webUtil.resMsg(0, "0005","手机验证码错误", null);
				 }
				user.setMobileNum(hUser.getPhone());
				user.setMobileState(203);
				hUser.setName(hUser.getPhone());
			} else {
				// 手机格式错误
				msg = "0001";
				return webUtil.resMsg(0, msg, "手机格式错误", null);
			}
		} else {
			// 账号注册用户
			msg = UserUtil.verifyUserName(hUser.getName());
			if (msg != null) {
				// 用户名格式错误
				msg = "0002";
				return webUtil.resMsg(0, msg, msg, null);
			}
		}
		if(user.getUserName()==null){
			user.setUserName(hUser.getName());
		}
		user.setRealName(hUser.getRealname());
		if(user.getUserPass()==null){
			user.setUserPass(MD5Util.MD5Encode("a123456", ""));
		}
		if(user.getUserType()==null){
		user.setUserType("individualUsers");
		}
		//获取cookie中的推广人id
		/*if(CookieManager.retrieve("ePeopleId", request, false)!=null){
			user.setExtensionPeople(Integer.parseInt(CookieManager.retrieve("ePeopleId", request, false)));
		}*/
		ApiResult result = userFacade.updateUser(user);
		if(1!=result.getCode()){
			return webUtil.resMsg(0, "", "绑定用户信息失败", null);
		}
		else{
		ApiGoodLibrary apiGoodLibrary = goodLibraryFacade.selectById(lirbraryId);
		if(apiGoodLibrary==null){
			return webUtil.resMsg(4, "", "善库信息有误", null);
		}
		//查询善库成员是否存在
		ApiGoodLibraryProple param = new ApiGoodLibraryProple();
		param.setLibraryId(lirbraryId);
		param.setUserId(hUser.getId());
		ApiGoodLibraryProple apiGoodLibraryProple = goodLibraryFacade.selectByParam(param);
		if(apiGoodLibraryProple!=null){//用户已是此善库成员
			return webUtil.resMsg(3, "", "用户已是此善库成员", null);
		}
		param.setCreatetime(new Date());
		param.setDonateNum(0);
		param.setState(201);
		param.setBalance(apiGoodLibrary.getDefaultMoney());
		param.setUsedmoney(0d);
		param.setRechargebalance(0d);
		int flag = goodLibraryFacade.save(param);
		if(flag==1){
			//更新善库家庭成员数
			ApiGoodLibrary apiGoodLibrary2 = goodLibraryFacade.selectById(lirbraryId);
			apiGoodLibrary2.setPeopleNum(apiGoodLibrary2.getPeopleNum()+1);
			goodLibraryFacade.update(apiGoodLibrary2);
			return webUtil.resMsg(1, "", "成功", null);
		}
		return webUtil.resMsg(5, "", "添加善库成员失败", null);
		/*if (1 != result.getCode()) {
			return webUtil.resMsg(0, "0007", "失败", null);
		}else{
			return webUtil.resMsg(result.getCode(), "0000", "成功", null);
		}*/
		}
	}
	
	@RequestMapping("gotoBind")
	public ModelAndView gotoBind(ApiFrontUser user,Integer lirbraryId){
		ModelAndView view = new ModelAndView("h5/goodlibrary/library_bind");
		view.addObject("user", user);
		view.addObject("lirbraryId", lirbraryId);
		return view;
	}
	private boolean isPhoneUser(PUser hUser) {
		return StringUtils.isBlank(hUser.getName());
	}
	
	@RequestMapping(value="getGoodLibraryView")
	public ModelAndView getGoodLibraryView(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "lirbraryId", required = true)Integer lirbraryId){
		ModelAndView view = new ModelAndView("goodlibrary/goodlibrary");
		//Integer userId = UserUtil.getUserId(request, response);
		ApiGoodLibrary goodLibrary = new ApiGoodLibrary();
		goodLibrary.setId(lirbraryId);
		//暂时考虑一个用户只能创建一个善库
		ApiPage<ApiGoodLibrary> list = goodLibraryFacade.queryByParam(goodLibrary,1,2);
		if(list != null && list.getResultData().size() > 0){
			
			//项目个数
			Integer goodlibraryProNum = donateRecordFacade.
					countProjectNumByUserId(list.getResultData().get(0).getUserId());
			//用户的普通捐款统计排除红包和善库（金额、次数、项目数）
			ApiDonateRecord apidr = donateRecordFacade.countDonateByUser(list.getResultData().get(0).getUserId());
			//善库的捐款金额、次数、号召人数
			ApiGoodLibraryProple apigp = goodLibraryFacade.
					countByGoodLibraryId(list.getResultData().get(0).getId());
			//用户红包的使用个数和金额
			ApiRedpackets apiRed = redPacketsFacade.countUserdNumAndUserdMoney(list.getResultData().get(0).getUserId());
			//总的捐赠项目
			int totalProjectNum = goodlibraryProNum;
			//号召人数
			int totalPeople = apigp.getTotalPeople();
			//行善次数
			int usednum =0;
			double usedamount=0.00;
			double totalAmount=0.00;
			if(apiRed!=null){
				usednum = apiRed.getUsednum()== null?0:apiRed.getUsednum();
				usedamount = apiRed.getUsedamount()== null?0.00:apiRed.getUsedamount();
			}
			if(apidr.getTotalAmount()!=null){
				totalAmount=apidr.getTotalAmount();
				}
			int totalDonateNum = (apidr.getDonateNum()==null?0:apidr.getDonateNum()) + 
					(apigp.getTotalDonateNum()==null?0: apigp.getTotalDonateNum())+ usednum;
			//捐款金额
			double totalMoney = totalAmount + (apigp.getTotalDonateMoney()==null?0.00: apigp.getTotalDonateMoney()) + usedamount;
			view.addObject("totalProjectNum", totalProjectNum);
			view.addObject("totalPeople", totalPeople);
			view.addObject("totalDonateNum", totalDonateNum);
			view.addObject("totalMoney", totalMoney);
			view.addObject("goodLibrary", list.getResultData().get(0));
			
			List<ApiBFile> bFiles = null;
			ApiBFile apiBFile = new ApiBFile();
			apiBFile.setCategory("goodLibrary"+list.getResultData().get(0).getId());
			bFiles = CommonFacade.queryApiBfile(apiBFile);
			view.addObject("gLibrary", bFiles);
			view.addObject("userId", list.getResultData().get(0).getUserId());
			//加载相关公益项目的图片
			ApiProject apiProject = new ApiProject();
			List<String> tags=new ArrayList<String>();
			if(!list.getResultData().get(0).getTag().equals("")){
			String[] tagString =  list.getResultData().get(0).getTag().split(",");
			for(int i=0;i<tagString.length;i++){
				tags.add(tagString[i]);
			}}
			tags.add("无标签");
			apiProject.setTaglist(tags);
			apiProject.setOrderBy("lastUpdateTime");
			apiProject.setOrderDirection("desc");
			ApiPage<ApiProject> project =projectFacade.queryProjectListNew(apiProject, 1, 100);
			if(project!=null&&project.getResultData()!=null){
			view.addObject("newUrl", project.getResultData());
			}
		}else {
			view = new ModelAndView("redirect:/index/newindex.do");
		}
		return view;
	}
	
	/**
	 * 行善记录
	 * @param from
	 * @return
	 */
    @ResponseBody
    @RequestMapping("goodLibraryList")
    public Map<String, Object> goodLibraryList(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, 
    		@RequestParam(value = "pageNum", required = false, defaultValue = "10") Integer pageNum,
    		@RequestParam(value = "userId", required = false) Integer userId,
    		@RequestParam(value = "donateTime", required = false) String donateTime){
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
        
        ApiDonateRecord apidr = new ApiDonateRecord();
        apidr.setUserId(userId);
        apidr.setDonatTimeStr(donateTime);
        ApiPage<ApiDonateRecord> list = donateRecordFacade.queryDonateRecordByParam(apidr, p.getPage(), p.getPageNum());
        if (list != null)
        {
        	for(ApiDonateRecord adr : list.getResultData()){
        		List<String> taglist = new ArrayList<String>();
        		if(StringUtils.isNotEmpty(adr.getTag())){
        			String[] str = adr.getTag().split(",");
            		if(str.length > 0){
            			for(String s:str){
            				if(StringUtils.isNotEmpty(s)){
            					taglist.add(s);
            				}
            			}
            		}
        		}
        		DateFormat format = new SimpleDateFormat("MM-dd");
        		//DateFormat longFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG); 
        		String dateString=format.format(adr.getDonatTime());
        		String[] date =dateString.split("-");
        		String dates=date[0]+"月"+date[1]+"日";
        		adr.setDonatTimeStr(dates);
        		adr.setTagList(taglist);
        	}
        	p.setData(list.getResultData());
            p.setNums(list.getTotal());
        }
        else
        {
            p.setNums(0);
        }
        if (p.getTotal() == 0)
        {
            return webUtil.resMsg(2, "0002", "行善记录列表为空", p);
        }
        else
        {
            return webUtil.resMsg(1, "0000", "成功", p);
        }
    }
    
    /**
     * 跳转到加入善库成功页
     * @param userId
     * @param libraryId
     * @return
     */
    @RequestMapping("gotoLibraryJoin")
    public ModelAndView gotoLibraryJoin(Integer userId,Integer lirbraryId){
    	ModelAndView view = new ModelAndView("h5/goodlibrary/library_join");
    	ApiGoodLibrary apiGoodLibrary = goodLibraryFacade.selectById(lirbraryId);
    	view.addObject("goodlibrary", apiGoodLibrary);
    	return view;
    }
    
    @ResponseBody
    @RequestMapping("isOrgoodLibrary")
    public Map<String, Object> isOrgoodLibrary(HttpServletRequest request, HttpServletResponse response,ApiGoodLibraryProple model){
    	if(model==null){
    		return webUtil.resMsg(2, "", "userId为空", null);
    	}
    	ApiGoodLibraryProple apiGoodLibraryProple = goodLibraryFacade.selectByParam(model);
    	if(apiGoodLibraryProple!=null){
    		return webUtil.resMsg(3, "", "你已经是此善库成员！", null);
    	}
    	return webUtil.resMsg(1, "", "你不是此善库成员！", null);
    	
    }
    /**
     * 善库记录
     * @return
     */
    @RequestMapping(value="getGoodLibraryList")
    public ModelAndView getGoodLibraryList(@RequestParam(value="type",required=false)Integer type){
    	ModelAndView view = new ModelAndView("goodlibrary/goodlibrary_list");
    	ApiGoodLibrary gl = new ApiGoodLibrary();
    	gl.setState(201);
    	gl.setOrderBy("usedmoney");
    	gl.setOrderDirection("DESC");
    	ApiPage<ApiGoodLibrary> list = goodLibraryFacade.queryByParam(gl,1,10000);
    	view.addObject("goodlibrary", list.getResultData());
    	if(list.getTotal()%12==0){
    		view.addObject("pageNum", list.getTotal()/12);
    	}else {
    		view.addObject("pageNum", list.getTotal()/12+1);
		}
    	// 善管家列表
		ApiFrontUser user = new ApiFrontUser();
		//user.setUserType("individualUsers");// 个人
		user.setLoveGroupMent(1);
		user.setLoveState(203);
		ApiPage<ApiFrontUser> apfronUsers = userFacade.queryUserList(user,1, 10000);
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
		view.addObject("goodPeople",users);
		if(apfronUsers.getTotal()%12==0){
    		view.addObject("pageNumGP", apfronUsers.getTotal()/12);
    	}else {
    		view.addObject("pageNumGP", apfronUsers.getTotal()/12+1);
		}
		view.addObject("type", type);
    	return view;
    }
    
    /**
     * 跳转到开通善库页面
     * @param view
     * @return
     */
    @RequestMapping("gotoOpenGoodLibrary")
    public ModelAndView gotoOpenGoodLibrary(ModelAndView view,HttpServletRequest request,HttpServletResponse response){
    	Integer userId=UserUtil.getUserId(request, response);
    	if(userId==null){//未登录
    		view.setViewName("redirect:/user/sso/login.do");
			view.addObject("entrance", "http://www.17xs.org/goodlibrary/gotoOpenGoodLibrary.do");
			return view;
    	}
    	//判断用户是否开通过善库1.已开通，回显信息，修改；2.没开通，添加
    	ApiGoodLibrary goodLibrary = new ApiGoodLibrary();
    	goodLibrary.setUserId(userId);
    	ApiPage<ApiGoodLibrary> page =goodLibraryFacade.queryByParam(goodLibrary, 1, 1);
    	if(page.getTotal()==1){
    		//view.addObject("goodlibrary", page.getResultData().get(0));
    		view.setViewName("redirect:/myGoodLibrary/gotoMyGoodLibraryDetails.do?id="+page.getResultData().get(0).getId());
    		return view;
    	}
    	ApiFrontUser user = userFacade.queryById(userId);
    	view.setViewName("goodlibrary/openGoodLibrary");
    	view.addObject("userType", user.getUserType());
    	return view;
    }
    
    /**
     * 添加善库
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("addGoodLibrary")
    @ResponseBody
    public Map<String, Object> addGoodLibrary(HttpServletRequest request,HttpServletResponse response,
    		ApiGoodLibrary model){
    	Integer userId=UserUtil.getUserId(request, response);
    	if(userId==null){//未登录
    		return webUtil.loginFailedRes(null);
    	}
    	ApiFrontUser user=userFacade.queryById(userId);
    	Integer result=0;
    	if(model.getId()==null){//添加
    		if(user.getBalance()>=model.getBalance()){//将用户余额冲到善库，生成资金明细
    			ApiCapitalinout cap =new ApiCapitalinout();
				cap.setUserId(user.getId());
				cap.setType(1);
				cap.setInType(12);
				cap.setMoney(model.getBalance());
				cap.setBalance(MathUtil.sub(user.getBalance(), model.getBalance()));
				cap.setSource("PC");
				cap.setPayState(302);
				cap.setPayType("善库充值");
				cap.setTranNum(StringUtil.uniqueCode());
				cap.setCreateTime(new Date());
				ApiResult flagCap = companyFacade.reCharge(cap);
				if(flagCap.getCode()== 1){
					user.setBalance(MathUtil.sub(user.getBalance(),model.getBalance()));
					user.setAvailableBalance(user.getBalance());
					userFacade.updateUser(user);
				}
    		}
    		else{//提醒充值
    			return webUtil.resMsg(webUtil.FLAG__FAILED, webUtil.ERROR_CODE_ADDFAILED, "余额不足，请先充值！", null);
    		}
    		model.setUserId(userId);
    		model.setUsedmoney(0.0);
    		model.setDonateNum(0);
    		model.setState(201);
    		model.setCreatetime(new Date());
    		model.setPeopleNum(0);
    		model.setHelpNum(0);
    		model.setContentImageId(model.getLogoId().toString());
    		result =goodLibraryFacade.save(model);
    		if(result>0){
    			ApiGoodLibrary param2 = new ApiGoodLibrary();
    			param2.setUserId(userId);
    			ApiPage<ApiGoodLibrary> page2 = goodLibraryFacade.queryByParam(param2, 1, 1);
    			result=page2.getResultData().get(0).getId();
    			ApiGoodLibraryProple people=new ApiGoodLibraryProple();
    			people.setLibraryId(result);
    			people.setUserId(userId);
    			people.setFamilyAddress(page2.getResultData().get(0).getFamilyAddress());
    			people.setState(201);
    			people.setDonateNum(0);
    			people.setUsedmoney(0.0);
    			people.setRechargebalance(page2.getResultData().get(0).getBalance());
    			people.setBalance(-1.0);
    			people.setCreatetime(new Date());
    			goodLibraryFacade.save(people);
    		}
    			
    		
    	}
    	else{//更新
        	ApiGoodLibrary goodLibrary = new ApiGoodLibrary();
        	goodLibrary.setId(model.getId());
        	goodLibrary.setState(201);
        	ApiPage<ApiGoodLibrary> param = goodLibraryFacade.queryByParam(goodLibrary, 1, 1);
    		if(param.getTotal()>0){//已经审核通过的善库
    			if(param.getResultData().get(0).getBalance()>model.getBalance()){//不符合，弹出提醒
        			return webUtil.resMsg(webUtil.FLAG__FAILED, webUtil.ERROR_CODE_ADDFAILED, "行善金额小于善库余额！", null);
        		}
        		else if(param.getResultData().get(0).getBalance()<model.getBalance()){//大于
        			Double balance = MathUtil.sub(model.getBalance(),param.getResultData().get(0).getBalance());
        			if(user.getBalance()>=balance){//将用户余额冲到善库，生成资金明细
        				ApiCapitalinout cap =new ApiCapitalinout();
    					cap.setUserId(user.getId());
    					cap.setType(1);
    					cap.setInType(12);
    					cap.setMoney(balance);
    					cap.setBalance(MathUtil.sub(user.getBalance(), balance));
    					cap.setSource("PC");
    					cap.setPayState(302);
    					cap.setPayType("善库充值");
    					cap.setTranNum(StringUtil.uniqueCode());
    					cap.setCreateTime(new Date());
    					ApiResult flagCap = companyFacade.reCharge(cap);
    					if(flagCap.getCode()== 1){
    						user.setBalance(MathUtil.sub(user.getBalance(), balance));
    						user.setAvailableBalance(user.getBalance());
    						userFacade.updateUser(user);
    					}
        			}
        			else{//提醒去充值
        				return webUtil.resMsg(webUtil.FLAG__FAILED, webUtil.ERROR_CODE_ADDFAILED, "余额不足，请先充值！", null);
        			}
        		}
        		else{//等于
        		}
    		}
    		else{//没通过的善库
    			
    		}
        	//ApiGoodLibrary param = goodLibraryFacade.queryById(model.getId());
    		//判断当前善库余额和所填余额：1.若小于善库的，不符合；2.若大于，则判断用户余额与差值，大则余额扣，小则提醒去充值；3.若等则不作处理
    		
    		if(param.getResultData().get(0).getContentImageId()!=null&&model.getLogoId()!=null&&!param.getResultData().get(0).getContentImageId().contains(model.getLogoId().toString())){
    			model.setContentImageId(param.getResultData().get(0).getContentImageId()+","+model.getLogoId());
    		}
    		else if((param.getResultData().get(0).getContentImageId()==null||param.getResultData().get(0).getContentImageId()=="")&&model.getLogoId()!=null){
    			model.setContentImageId(model.getLogoId().toString());
    		}
    		goodLibraryFacade.update(model);
    		result=model.getId();
    	}
    	if(result>0){
    		return webUtil.resMsg(webUtil.FLAG_SUCCESS, webUtil.ERROR_CODE_SUCCESS, "添加或修改善库成功！", result);
    	}
    	else{
    		return webUtil.resMsg(webUtil.FLAG__FAILED, webUtil.ERROR_CODE_ADDFAILED, "添加善库失败，请联系客服！", result);
    	}
    }
    
    /**
     * 跳转到善库成功页面
     * @param view
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("gotoGoodLibrarySuccess")
    public ModelAndView gotoGoodLibrarySuccess(ModelAndView view,@RequestParam(value="libraryId",required=true)Integer libraryId,
    		HttpServletRequest request,HttpServletResponse response){
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId==null){
    		view.setViewName("redirect:/user/sso/login.do");
			view.addObject("entrance", "http://www.17xs.org/goodlibrary/gotoGoodLibrarySuccess.do?libraryId="+libraryId);
    		return view;
    	}
    	view.setViewName("goodlibrary/opendGoodLibrary_success");
    	view.addObject("libraryId", libraryId);
    	return view;
    }
    
    /**
     * 跳转到善库设置页面
     * @param view
     * @param libraryId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("gotoSetGoodLibrary")
    public ModelAndView gotoSetGoodLibrary(ModelAndView view,@RequestParam(value="libraryId",required=true)Integer libraryId,
    		HttpServletRequest request,HttpServletResponse response){
    	Integer userId = UserUtil.getUserId(request, response);
    	if(userId==null){
    		view.setViewName("redirect:/user/sso/login.do");
			view.addObject("entrance", "http://www.17xs.org/goodlibrary/gotoSetGoodLibrary.do?libraryId="+libraryId);
    		return view;
    	}
    	ApiConfig config = new ApiConfig();
        config.setConfigKey("projectTag");
        List<ApiConfig> list = CommonFacade.queryList(config);
        if(list != null && list.size() > 0 ) {
        	view.addObject("tag", list.get(0).getConfigValue());
        }
    	view.setViewName("goodlibrary/goodLibrarySetup");
    	view.addObject("libraryId", libraryId);
    	return view;
    }
    
    /**
     * 设置善库信息
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("setGoodLibrary")
    @ResponseBody
    public Map<String, Object> setGoodLibrary(HttpServletRequest request,HttpServletResponse response,
    		ApiGoodLibrary model){
    	if(model.getId()==null){
    		return webUtil.resMsg(webUtil.FLAG__FAILED, webUtil.ERROR_CODE_ADDFAILED,"参数错误！" , null);
    	}
    	Integer userId=UserUtil.getUserId(request, response);
    	if(userId==null){//未登录
    		return webUtil.loginFailedRes(model.getId());
    	}
    	if(model.getTag()!=null){
    		model.setTag(model.getTag().replace("，", ","));
    	}
    	if(model.getAppointArea()!=null){
    		model.setAppointArea(model.getAppointArea().replace("，", ","));
    	}
    	if(model.getAppointPersonArea()!=null){
    		model.setAppointPersonArea(model.getAppointPersonArea().replace("，", ","));
    	}
    	if(model.getAppointMobile()!=null){
    		model.setAppointMobile(model.getAppointMobile().replace("，", ","));
    	}
    	Integer result = goodLibraryFacade.update(model);
    	if(result>0){
    		return webUtil.resMsg(webUtil.FLAG_SUCCESS, webUtil.ERROR_CODE_SUCCESS, "设置善库信息成功！", model.getId());
    	}
    	else{
    		return webUtil.resMsg(webUtil.FLAG__FAILED, webUtil.ERROR_CODE_ADDFAILED,"设置善库信息失败！" , null);
    	}
    }
    
    private String getImgCode(HttpServletRequest request) {
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
