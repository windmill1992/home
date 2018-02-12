package com.guangde.home.controller.deposit;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit_wap;
import com.alipay.util.UtilDate;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.api.user.ICompanyFacade;
import com.guangde.api.user.IDonateRecordFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiCapitalinout;
import com.guangde.entry.ApiProject;
import com.guangde.home.utils.DateUtil;
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
@RequestMapping("rechargeWapAlipay")
public class RechargeAlipayByWapAction extends DepositBaseAction
{
    
    // 服务器异步通知页面路径
    public final String notify_url = "http://www.17xs.org/rechargeWapAlipay/async/";
    
	// 页面跳转同步通知页面路径
    public final String call_back_url = "http://www.17xs.org/rechargeWapAlipay/return_url/";

  	//操作中断返回地址
    public final String merchant_url = "http://www.17xs.org/rechargeWapAlipay/return_url/";
    
    //支付宝网关地址
    public static final	String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?";
    //项目的缓存key
    public static final	String PROJECT_PAY_SUCCESS_KEY= "PROJECT_PAY_SUCCESS";
    // 批量捐项目id
    public static final int PROJECTID = 285;
    
    Logger logger = LoggerFactory.getLogger(RechargeAlipayByWapAction.class);
    
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
    	
        ModelAndView view = new ModelAndView("deposit/alipay");
        int payType = from.getPayType();//支付错误返回方式
        //用户的类型判断，是游客还是注册用户
        Integer userId = UserUtil.getUserId(request, response);
		if (userId == null) {
			view = new ModelAndView("redirect:/user/sso/login.do");
			return view;
		}
            
        String total_fee = String.valueOf(from.getAmount());
        Map<String, String> sParaTemp = new HashMap<String,String>();
        sParaTemp.put("out_trade_no", StringUtil.uniqueCode());// 商品订单编号
        sParaTemp.put("total_fee", total_fee);// 价格
        sParaTemp.put("projectId", String.valueOf(PROJECTID));// 项目Id

        sParaTemp.put("subject", "善基金充值");
        
		ApiCapitalinout capitalinout = new ApiCapitalinout();
		capitalinout.setTranNum(sParaTemp.get("out_trade_no"));
		capitalinout.setSource("H5");
		capitalinout.setMoney(from.getAmount());

		capitalinout.setPayType("alipay");
	
		capitalinout.setUserId(userId);
		capitalinout.setBankType(from.getBank());
		ApiResult rt = companyFacade.companyReCharge(capitalinout);
      
        
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
            view.addObject("pName", "善基金充值");
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
        if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS"))
        {
            if (companyFacade.checkAlipayResult(tradeNo, notifyId))
            {
            	companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
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
        	companyFacade.companyReChargeResult(tradeNo, notifyId, false, "");
        	logger.debug("rechargeWapAlipay>>>支付失败");
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
            	companyFacade.companyReChargeResult(tradeNo, notifyId, true, "");
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
        	companyFacade.companyReChargeResult(tradeNo, notifyId, false, "");
        	logger.debug("rechargeWapAlipay>>>支付失败");
        	return "/deposit/error";
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
				+ "</pay_expire><merchant_url>" + merchant_url+param
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
