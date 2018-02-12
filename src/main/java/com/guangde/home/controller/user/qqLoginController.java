package com.guangde.home.controller.user;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiThirdUser;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.SpringContextUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.pojo.ApiResult;
import com.tenpay.utils.TenpayUtil;

@Controller
@RequestMapping(value="sso")
public class qqLoginController {

	private final Logger logger = LoggerFactory.getLogger(qqLoginController.class);
	public static final String queryToken = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	public static final String appid = "wxe4a86dee2b974b99";
	public static final String secret = "45d51c0f1f9aebf3152f2c3d6bdfa897";
	@Autowired
	private IUserFacade userFacade;
	/**
	 * 微信登录
	 * @param code
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("wxLoad")
	public ModelAndView wxLoad(@RequestParam(value = "code", required = true ) String code,
			@RequestParam(value = "state", required = false ) String redirect_url,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		//redirect_url=java.net.URLEncoder.encode(redirect_url, "UTF-8");
		redirect_url = redirect_url.replace("!", "&");
		logger.info("PC 扫码 login redirect_url>>>>>>"+redirect_url);
		String adressIp = SSOUtil.getUserIP(request);
		String url = queryToken+"appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		JSONObject json = TenpayUtil.httpRequest(url, "get", "");
		String unionid = (String)json.get("unionid");
		String openid = (String)json.get("openid");
		String access_token = (String)json.get("access_token");
		ApiFrontUser user =null;
		if (StringUtils.isNotEmpty(unionid)) {
			ApiThirdUser tuser = new ApiThirdUser();
			tuser.setUnionid(unionid);
			tuser.setType("weixin");
			tuser = userFacade.queryThirdUserByParam(tuser);
			if (tuser != null) {
				user = userFacade.queryById(tuser.getUserId());
			}else{
				user = new ApiFrontUser();
				url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
				json = TenpayUtil.httpRequest(url, "get", "");
				//新增游客信息
				 String tName = (String)json.get("nickname");
				 String headimgurl = (String)json.get("headimgurl");
				 tName = StringUtil.filterEmoji(tName);
				//临时保存一下图片的路径
				user.setPersition(headimgurl);
				user.setUserType(PengPengConstants.PERSON_USER);
				user.setNickName(tName);
				user.setUserPass(StringUtil.randonNums(8));
				user.setUserName(PengPengConstants.WENXIN + DateUtil.parseToFormatDateString(new Date(), DateUtil.LOG_DATE_TIME));
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
				tuser = new ApiThirdUser();
				tuser.setAccountNum("weixin");
				tuser.setUnionid(unionid);
				tuser.setType("weixin");
				ApiResult result = userFacade.thirdUserRegistered(user, tuser);
				Integer userId = Integer.valueOf(result.getMessage());
				user = userFacade.queryById(userId);
				
			}
			try
			{
				// 自动登录
				SSOUtil.login(user, request, response);
			}
			catch(Exception e)
			{
				logger.error("apiThirdUser >> SSOUtil.login : "+e);
			}
			
			
		};
		ModelAndView view = new ModelAndView();
		if(redirect_url==null || "".equals(redirect_url)){
			view.setViewName("redirect:/index/newindex.do");
		}
		else{
			view.setViewName("redirect:"+redirect_url);
		}
		return view;
	}
	public static void main(String[] args) {
		String access_token ="V6veq_Q_oNldAiW68PooFXq3jTjOjnwJ4cDEMWhSxqF8zhylat3JvjZ9rL8TdrqQFkN4CXDB2TA1BkgSlm2CrCiFmuCTF4oN8-2ipTfRO9rttAS-r_i9AhilSMt9pDGoVPXdABACWE";
		String openId = "oxhfYwcexgpZGVQ-L92kTaSkEN5E";
		String unionid = "o3gIDuHeBz_UpH2EqcWKGJqLNUpY";
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId;
		System.out.println(TenpayUtil.httpRequest(url, "get", ""));
	}
}
