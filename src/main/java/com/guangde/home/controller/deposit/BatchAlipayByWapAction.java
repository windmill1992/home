package com.guangde.home.controller.deposit;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit_wap;
import com.alipay.util.UtilDate;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiDonateRecord;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiProject;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.home.utils.UserUtil;
import com.guangde.home.vo.deposit.DepositForm;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;

/**
 * 
 * 对于支付宝无线端的，支付渠道
 * 支付宝成功处理. 要确定你的收款支付宝账号，合作身份者ID，商户的私钥是正确的
 * 同时要把AlipaySubmit.buildRequest的输出文本显示在jsp上
 */
@Controller
@RequestMapping("batchWapAlipay")
public class BatchAlipayByWapAction extends DepositBaseAction
{
    
    // 服务器异步通知页面路径
    public final String notify_url = "http://www.17xs.org/batchWapAlipay/async/";
    
	// 页面跳转同步通知页面路径
    public final String call_back_url = "http://www.17xs.org/batchWapAlipay/return_url/";

  	//操作中断返回地址
    public final String merchant_url = "http://www.17xs.org/";
    
    //支付宝网关地址
    public static final	String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?";
    //项目的缓存key
    public static final	String PROJECT_PAY_SUCCESS_KEY= "PROJECT_PAY_SUCCESS";
    // 批量捐项目id
    public static final int PROJECTID = 285;
    
    Logger logger = LoggerFactory.getLogger(BatchAlipayByWapAction.class);
    
    @Autowired
    private IDonateRecordFacade donateRecordFacade;
    
    @Autowired
    private IProjectFacade projectFacade;
    
    @Autowired
    private IUserFacade userFacade;
    
    @Autowired
    private ICompanyFacade companyFacade;
    
    @Autowired
    private RedisService redisService;
    
    @RequestMapping(value = "/deposit", produces = "application/json")
    public ModelAndView deposit(DepositForm from, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
    	String touristName = from.getTouristName() == null?"":from.getTouristName();
    	String touristMobile = from.getTouristMobile() == null?"":from.getTouristMobile();
    	
        ModelAndView view = new ModelAndView("deposit/alipay");
        int payType = from.getPayType();//支付错误返回方式
        //用户的类型判断，是游客还是注册用户
        Integer userId = UserUtil.getUserId(request, response);
        ApiFrontUser user = new ApiFrontUser();
        if(userId == null){
        	//根据IP创建用户
        	//判段这个IP是否已经注册过
            String adressIp = SSOUtil.getUserIP(request);
            user = new ApiFrontUser();
            user.setRegisterIP(adressIp);
            user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
            user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
            user = userFacade.queryUserByParam(user);
            if (user != null)
            {
                userId = user.getId();
                String nickName = user.getNickName() == null ?"":user.getNickName();
                String mobileNum = user.getMobileNum() == null ?"":user.getMobileNum();
                if(!touristName.equals(nickName) || !touristMobile.equals(mobileNum))
				 {
					 ApiFrontUser  fur = new ApiFrontUser();
					 fur.setNickName(StringUtil.filterEmoji(touristName));
					 fur.setMobileNum(touristMobile);
					 fur.setId(user.getId());
					 userFacade.updateUser(fur);
				 }
            }
            else
            {
                user = new ApiFrontUser();
                try {
                	long ip = IPAddressUtil.ipToLong(adressIp);
                	ApiIptable iptable = new ApiIptable();
                	iptable.setEndIPNum(String.valueOf(ip));
                	iptable.setStartIPNum(String.valueOf(ip));
                	List<ApiIptable> ipList = userFacade.queryIptableList(iptable);
                	if(ipList!=null && ipList.size()>0){
                		String[] contents = ipList.get(ipList.size()-1).getCountry().replace("省", ",").replace("市", ",").replace("区", ",").split(",");
                		if(contents.length==1){
                			user.setProvince(contents[0]);
                		}
                		else if(contents.length==2){
                			user.setProvince(contents[0]);
                			user.setCity(contents[1]);
                		}
                		else if(contents.length==3){
                			user.setProvince(contents[0]);
                			user.setCity(contents[1]);
                			user.setArea(contents[2]);
                		}
                	}
				
			} catch (Exception e) {
			}
                if(!StringUtils.isEmpty(from.getTouristName()))
                {
                	user.setUserName(StringUtil.filterEmoji(touristName));
                }
                else
                {
                	user.setUserName(PengPengConstants.VISITOR + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
                }
                
                if(!StringUtils.isEmpty(touristMobile))
                {
                	user.setMobileNum(touristMobile);
                }
                user.setUserPass(StringUtil.randonNums(8));
                user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
                user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
                user.setRegisterIP(adressIp);
                ApiResult apr = userFacade.registered(user);
                if (apr == null || apr.getCode() != 1)
                {
                    view.addObject("alipay", "没有这个项目");
                    return view;
                }
                user = new ApiFrontUser();
                user.setRegisterIP(adressIp);
                user.setUserType(PengPengConstants.PERSON_TOURIST_USER);
                user.setRegUsersType(PengPengConstants.DONOR_TYPE_OUT);
                user = userFacade.queryUserByParam(user);
                userId = user.getId();
            }
            
			 try
			 {
				 // 自动登录
				 SSOUtil.login(user, request, response);
			 }
			 catch(Exception e)
			 {
				 logger.error("WapAlipay >> SSOUtil.login : "+e);
			 }
        }else{
        	 user = userFacade.queryById(userId);
             String nickName = user.getNickName() == null ?"":user.getNickName();
             String mobileNum = user.getMobileNum() == null ?"":user.getMobileNum();
             if(!touristName.equals(nickName) || !touristMobile.equals(mobileNum))
			{
				 ApiFrontUser  fur = new ApiFrontUser();
				 fur.setNickName(StringUtil.filterEmoji(touristName));
				 fur.setMobileNum(touristMobile);
				 fur.setId(user.getId());
				 userFacade.updateUser(fur);
			 }
        }
        String total_fee = String.valueOf(from.getAmount());
        Map<String, String> sParaTemp = new HashMap<String,String>();
        sParaTemp.put("out_trade_no", StringUtil.uniqueCode());// 商品订单编号
        sParaTemp.put("total_fee", total_fee);// 价格
        sParaTemp.put("projectId", String.valueOf(PROJECTID));// 项目Id
        ApiDonateRecord dRecord = new ApiDonateRecord();
        ApiProject project = projectFacade.queryProjectDetail(PROJECTID);
        if (project == null)
        {
            view.addObject("error", "支付出现异常，请联系客服。");
            return view;
        }
        sParaTemp.put("subject", "批量捐款项目分别为"+from.getPlistId());
        dRecord.setLeaveWord(from.getPlistId());
        dRecord.setUserId(userId);
        dRecord.setProjectId(project.getId());
        //dRecord.setProjectTitle(project.getTitle());
        dRecord.setProjectTitle("批量捐款");
        dRecord.setMonthDonatId(123);
        dRecord.setDonorType(PengPengConstants.PERSON_TOURIST_USER);
        dRecord.setDonatType(user.getUserType());
        dRecord.setDonatAmount(from.getAmount());
        dRecord.setDonateCopies(from.getCopies());
        dRecord.setTranNum(sParaTemp.get("out_trade_no"));
        dRecord.setSource("H5");
        dRecord.setCompanyId(user.getCompanyId());
        if(from.getExtensionPeople() != null){
        	dRecord.setExtensionPeople(from.getExtensionPeople());
        	ApiFrontUser auser = userFacade.queryById(from.getExtensionPeople());
        	if(auser != null && auser.getExtensionPeople() != null 
    				&& auser.getExtensionPeople()==1) {
        		dRecord.setExtensionEnabled(1);//推广员的有效推广
        	}
        }
        //新增游客信息
        if (!StringUtils.isEmpty(from.getTouristName()) || !StringUtils.isEmpty(from.getTouristMobile()))
        {
            
        	String touristMessage = "{\"name\":\"" + from.getTouristName().replaceAll("\"", "\'") + "\",\"mobile\":\"" + from.getTouristMobile().replaceAll("\"", "\'") + "\"}";
            dRecord.setTouristMessage(touristMessage);
        }
        /* 
        if (payType == 1)
        {
        	
            if (StringUtils.isNotEmpty(from.getDonateName()) || StringUtils.isEmpty(from.getDonateWord()))
            {
                if (from.getDonateName().length() > 20 || from.getDonateWord().length() > 20)
                {
                    view = new ModelAndView("/deposit/pay");
                    view.addObject("error", "请控制在20个字内");
                    return view;
                }
            }
            dRecord.setLeaveWord(from.getDonateName() + "|" + from.getDonateWord());
          
        }
          */
        ApiResult rt = null;
        rt = donateRecordFacade.batchBuyDonate(dRecord, null, "alipay");
        
        // 成功:1
        if (rt != null && rt.getCode() == 1)
        {
            // 建立请求
            try
            {
            	//建立请求
        		String sHtmlText = AlipaySubmit_wap.buildRequest(ALIPAY_GATEWAY_NEW, alipayMap(request, response,sParaTemp), "get", "确认");
                view.addObject("alipay", sHtmlText);
                return view;
            }
            catch (Exception e)
            {
                view = getview(payType);
                view.addObject("error", "支付出现异常，请联系客服。");
                e.printStackTrace();
                return view;
            }
        }
        else if (rt.getCode() == 90001)
        {
            view = getview(payType);
            view.addObject("tokenCode", from.getTokenCode());
            view.addObject("projectId", from.getProjectId());
            view.addObject("pName", project.getTitle());
            view.addObject("copies", from.getCopies());
            view.addObject("amount", from.getAmount());
            view.addObject("error", rt.getMessage());
            return view;
        }
        else
        {
            view = getview(payType);
            view.addObject("error", "支付出现异常，请联系客服。");
            return view;
        }
    }
    
    /**
     * 同步通知的页面的Controller
     * 
     */
    @RequestMapping(value = "/return_url")
    public ModelAndView Return_url( DepositForm from,HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView view = null;
        String tradeStatus = request.getParameter("result");
        String tradeNo = request.getParameter("out_trade_no");
        String notifyId = request.getParameter("trade_no");
        if (tradeStatus.equals("success"))
        {
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
                donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "alipay");
                logger.debug("支付宝渠道充值成功:: 对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }
            
            
        }
        else
        {
            // 查看错误alipay文档
            donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, false, tradeStatus, "");
        }
        
        view = new ModelAndView("redirect:http://www.17xs.org/ucenter/userCenter_h5.do");
        String key = PROJECT_PAY_SUCCESS_KEY+PROJECTID;
        Object obj = redisService.queryObjectData(key);
        if (obj == null) {
        	ApiProject apiProject = projectFacade.queryProjectDetail(from.getProjectId());
        	if(apiProject != null){
        		redisService.saveObjectData(key, apiProject.getTitle(), DateUtil.DURATION_TEN_S);
        		 view.addObject("pName", apiProject.getTitle());
        	}else {
        		 view.addObject("pName", "爱心支持");
			}
		}else {
			view.addObject("pName", obj);
		}
		view.addObject("amount", from.getAmount());
		view.addObject("projectId", PROJECTID);
		view.addObject("tradeNo", tradeNo);
        return view;
    }
    
    /**
     * 异步通知付款状态的Controller
     * 
     */
    @RequestMapping(value = "/async")
    public String async(HttpServletRequest request, HttpServletResponse response)
    {
        String tradeNo = request.getParameter("out_trade_no");
        String tradeStatus = request.getParameter("trade_status");
        String notifyId = request.getParameter("trade_no");
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            //检查是否已经充值
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
               // donateRecordFacade.buyDonateResult(tradeNo, notifyId, true, "", "");
                donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, true, "", "alipay");
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId);
            }
            else
            {
                logger.debug("支付宝渠道充值成功::对交易号" + tradeNo + "：：支付宝ID" + notifyId + "重复回调的屏蔽");
            }

        }
        else
        {
            // 查看错误alipay文档
            donateRecordFacade.batchBuyDonateResult(tradeNo, notifyId, false, tradeStatus, "");
        }
        return "/deposit/success";
        
    }
    
    private Map<String, String> alipayMap(HttpServletRequest request, HttpServletResponse response,Map<String, String> sParaTemp)
        throws Exception
    {
        // 支付类型
        String payment_type = "1";
        // 订单名称
        String subject = sParaTemp.get("subject");
        String out_trade_no = sParaTemp.get("out_trade_no");
        String total_fee = sParaTemp.get("total_fee");
        //返回格式
  		String format = "xml";
  		String v = "2.0";
  		String pay_expire = "15";//订单有效15分钟
  		//请求号
  		String req_id = UtilDate.getOrderNum();
  		String param= sParaTemp.get("projectId")+"_"+total_fee+"/"; 
        //请求业务参数详细
		String req_dataToken = "<direct_trade_create_req><notify_url>"
				+ notify_url+param + "</notify_url><call_back_url>" + call_back_url+param
				+ "</call_back_url><seller_account_name>"
				+ AlipayConfig.seller_email
				+ "</seller_account_name><out_trade_no>" + out_trade_no
				+ "</out_trade_no><subject>" + subject
				+ "</subject><total_fee>" + total_fee
				+ "</total_fee><pay_expire>" + pay_expire
				+ "</pay_expire><merchant_url>" + merchant_url
				+ "</merchant_url></direct_trade_create_req>";

		// 把请求参数打包成数组
		Map<String, String> sParaTempToken = new HashMap<String, String>();
		sParaTempToken.put("service", "alipay.wap.trade.create.direct");
		sParaTempToken.put("partner", AlipayConfig.partner);
		sParaTempToken.put("_input_charset", AlipayConfig.input_charset);
		sParaTempToken.put("sec_id", AlipayConfig.sign_type);
		sParaTempToken.put("format", format);
		sParaTempToken.put("v", v);
		sParaTempToken.put("req_id", req_id);
		sParaTempToken.put("req_data", req_dataToken);

		// 建立请求
		String sHtmlTextToken = AlipaySubmit_wap.buildRequest(ALIPAY_GATEWAY_NEW,
				"", "", sParaTempToken);
		// URLDECODE返回的信息
		sHtmlTextToken = URLDecoder.decode(sHtmlTextToken,
				AlipayConfig.input_charset);
		// 获取token
		String request_token = AlipaySubmit_wap.getRequestToken(sHtmlTextToken);
		// out.println(request_token);

		// //////////////////////////////////根据授权码token调用交易接口alipay.wap.auth.authAndExecute//////////////////////////////////////

		// 业务详细
		String req_data = "<auth_and_execute_req><request_token>"
				+ request_token + "</request_token></auth_and_execute_req>";
		// 必填

		// 把请求参数打包成数组
		sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("sec_id", AlipayConfig.sign_type);
		sParaTemp.put("format", format);
		sParaTemp.put("v", v);
		sParaTemp.put("req_data", req_data);
      		
        return sParaTemp;
    }
    
    public ModelAndView getview(int type)
    {
        ModelAndView view = null;
        if (type == 1)
        {
            view = new ModelAndView("/deposit/pay");
        }
        else
        {
            view = new ModelAndView("/deposit/common_pay");
        }
        return view;
    }
    
}
