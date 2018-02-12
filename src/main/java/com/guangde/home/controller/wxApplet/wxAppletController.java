package com.guangde.home.controller.wxApplet;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiFrontUser;
import com.guangde.entry.ApiIptable;
import com.guangde.entry.ApiThirdUser;
import com.guangde.home.constant.PengPengConstants;
import com.guangde.home.utils.AES;
import com.guangde.home.utils.DateUtil;
import com.guangde.home.utils.IPAddressUtil;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.StringUtil;
import com.guangde.pojo.ApiResult;
import com.redis.utils.RedisService;
import com.tenpay.demo.H5Demo;

@Controller
@RequestMapping("wx")
public class wxAppletController {
	Logger logger = LoggerFactory.getLogger(wxAppletController.class);
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private IUserFacade userFacade;

	@RequestMapping(value="getSessionKey",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getSessionKey(@RequestParam("js_code")String js_code){
		JSONObject result= new JSONObject();
		JSONObject result2= new JSONObject();
		JSONObject item= new JSONObject();
		String session_key="";
		String openId="";
		String unionId="";
		try {
			 Object objAtc = redisService.queryObjectData(js_code);
			 if(objAtc != null){
				 session_key=(String)objAtc;
				 String[] session_keys = session_key.split("_");
				 result2.put("openId", session_keys[1]);
				 result2.put("session_key", session_keys[0]);
				 result2.put("unionId", session_keys[2]);
				 item.put("code", 1);
				 item.put("msg", "success！");
				}else{
					result = H5Demo.getSessionKeyRequest(js_code);
					session_key = result.getString("session_key");
					openId = result.getString("openid");
					unionId = result.getString("unionid");
					redisService.saveObjectData(js_code, session_key+"_"+openId+"_"+unionId, DateUtil.DURATION_MIN_S*2);
					result2.put("openId", openId);
					result2.put("session_key", session_key);
					result2.put("unionId", unionId);
					item.put("code", 1);
					item.put("msg", "success！");
				}
			} catch (UnsupportedEncodingException e) {
				item.put("code", -1);
				item.put("msg", "error！");
				e.printStackTrace();
			}
		item.put("result", result2);
		return item;
	}
	
	/**
	 * 解密用户敏感数据
	 * @param encryptedData 明文
	 * @param iv            加密算法的初始向量
	 * @param sessionId     会话ID
	 * @return
	 */
	@RequestMapping(value = "/decodeInfo", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public JSONObject decodeUserInfo(@RequestParam(required = true,value = "encryptedData")String encryptedData,
	        @RequestParam(required = true,value = "iv")String iv,
	        String js_code) throws InvalidParameterSpecException{
		JSONObject result= new JSONObject();
		JSONObject item = new JSONObject();
	    //从缓存中获取session_key
		String session_key="";
		try {
			 Object objAtc = redisService.queryObjectData(js_code);
			 if(objAtc != null){
				 session_key=objAtc.toString();
				 String[] session_keys = session_key.split("_");
				 session_key = session_keys[0];
			}else{
				result = H5Demo.getSessionKeyRequest(js_code);
				session_key = result.getString("session_key");
				redisService.saveObjectData(js_code, session_key, DateUtil.DURATION_MIN_S*2);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if("".equals(session_key) || session_key==null){
			item.put("code", -1);
			item.put("msg", "js_code is invalid");
		}else{
			try {
		        AES aes = new AES();
		        byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(session_key), Base64.decodeBase64(iv));
		        if(null != resultByte && resultByte.length > 0){
		            String userInfo = new String(resultByte, "UTF-8");
		            
		            /*Integer step =0;
		            JSONObject data = JSONObject.parseObject(userInfo);
		            String s = data.get("stepInfoList").toString();
		            JSONArray json = (JSONArray) JSONArray.parse(s);
		            if(json.size()>0 && json.size()==30){
		              //for(int i=0;i<json.size();i++){
		                JSONObject job = json.getJSONObject(29);//获取今天的步数
		                step=Integer.valueOf(job.get("step").toString());
		              //}
		            }*/
		            
		            
		            item.put("code", 1);
		            item.put("msg", "success");
		            item.put("result", JSONObject.parseObject(userInfo));
		            return item;
		        }
		    } catch (InvalidAlgorithmParameterException e) {
		        e.printStackTrace();
		        item.put("code", -1);
	            item.put("msg", "error");
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace(); 
		        item.put("code", -1);
	            item.put("msg", "error");
		    }
			if(item.get("code")==null ){
				item = new JSONObject();
				item.put("code", -1);
	            item.put("msg", "error");
			}
		}
	    return item;
	}
	
	
	@RequestMapping(value="login",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject login(ApiFrontUser user,HttpServletRequest request,HttpServletResponse response,
			@RequestParam("unionId")String unionId, @RequestParam("openId")String openId){
		response.setHeader("Access-Control-Allow-Origin", "*");
		Integer userId=null;
		JSONObject item = new JSONObject();
		JSONObject data = new JSONObject();
		//根据unionId判断用户是否注册过
		ApiThirdUser thirdUser = new ApiThirdUser();
		thirdUser.setUnionid(unionId);
		thirdUser = userFacade.queryThirdUserByParam(thirdUser);
		if(thirdUser==null){//未注册
			String adressIp = SSOUtil.getUserIP(request);
			String nickName = StringUtil.filterEmoji(user.getNickName());
			user.setRegisterIP(adressIp);
			user.setNickName(nickName);
			user.setPersition(user.getCoverImageUrl());
			user.setUserType(PengPengConstants.PERSON_USER);
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
		thirdUser = new ApiThirdUser();
		thirdUser.setAccountNum(openId);
		thirdUser.setType("weixin");
		thirdUser.setUnionid(unionId);
		ApiResult result = userFacade.thirdUserRegistered(user, thirdUser);
		userId = Integer.valueOf(result.getMessage());
		user = userFacade.queryById(userId);
		try{
			// 自动登录
			SSOUtil.login(user, request, response);
		}
		catch(Exception e)
		{
			logger.error("",e);
		}
		}
		else{
			userId=thirdUser.getUserId();
		}
		logger.info("WeRunData--login--userId>>>>>>>>>"+userId);
		data.put("userId", userId);
		data.put("realName", user.getRealName());
		data.put("mobile", user.getMobileNum());
		item.put("code", 1);
		item.put("msg", "success");
		item.put("result", data);
		
		return item;
	}
	
	@RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getAccessToken(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject item = new JSONObject();
		JSONObject result = new JSONObject();
		String res = H5Demo.getAppletAccessTokenRequest();
		if(res != null && !"".equals(res)){
			result.put("accessToken", res);
			item.put("result", result);
			item.put("code", 1);
			item.put("msg", "success");
			return item;
		}
		item.put("code", 0);
		item.put("msg", "error");
		return item;
	}
	
}
