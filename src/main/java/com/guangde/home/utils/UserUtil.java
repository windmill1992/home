package com.guangde.home.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


/**
 * user help class.
 * Date: 11-8-31 上午9:30
 *
 * @author dongxf2006@gmail.com
 *         since 2.0
 */
public class UserUtil {

	 /**
     * 检查用户登录名
     *
     * @param userName
     * @return null :正确,其他错误信息
     */
    public static String verifyUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return "请输入用户名";
        }
//        if (IllegalKeywordUtils.isUsernameIllegal(userName)) {
//            return "用户名中含有禁止使用的字符";
//        }
        int len;
		try {
			len = userName.getBytes("gbk").length;
		} catch (UnsupportedEncodingException e) {
			 return "验证用户名失败";
		}
        if (len < 4 || len > 36) {
            return "用户名不能超过18个字符";
        }
        String reg = "^([a-z|A-Z]+|[ \\u4e00-\\u9fa5]+|[0-9]+)+$";
        if (!userName.matches(reg)) {
            return "用户名只能由汉字、数字、字母组成";
        }
        return null;
    }
    
    /**
     * 检查密码
     *
     * @param password
     * @return null :正确,其他错误信息
     */
    public static String verifyPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return "请输入登录密码";
        }
        int lent;
		try {
			lent = password.getBytes("gbk").length;
		} catch (UnsupportedEncodingException e) {
			 return "验证密码失败";
		}
        if (lent < 6 || lent > 12) {
            return "登录密码必须在6-12个字符之间";
        }
        String reg = "^([^\\u4e00-\\u9fa5]+)+$";
        if (!password.matches(reg)) {
            return "密码只能由数字‘字母组成";
        }
        return null;
    }
    
    public static Integer getUserId(HttpServletRequest request,HttpServletResponse response){
    	String userId = SSOUtil.verifyAuth(request,response);
    	if(userId == null){
    		return null;
    	}
    	boolean isNum = userId.matches("[0-9]+"); 
    	if(!isNum){
    		return null;
    	}
    	return Integer.valueOf(userId);
    }
    /**
     * 获取推广人id
     * @param request
     * @param response
     * @return
     */
    public static String getExtensionPeople(HttpServletRequest request,HttpServletResponse response){
    
    	String extensionPeople = SSOUtil.extensionPeople(request,response);
    	
    	return extensionPeople;
    }
    
    public static String getUserHead(HttpServletRequest request,HttpServletResponse response){
    	String url = CookieManager.retrieve(CookieManager.USER_HEAD, request, false);
    	if(StringUtils.isBlank(url))
    		return null;
    	try {
			url = URLDecoder.decode(url, "utf-8");
			if("good".equals(url))
				return null;
			return url;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    public static String Browser (HttpServletRequest request)
	{
    	//判断是浏览器访问  
    	String agent = request.getHeader("User-Agent");
    	//微信浏览器
    	if(agent.toLowerCase().matches("(.)*(micromessenger)(.)*")){
    		return "wx";
    	}else {
    		//未知
    		return "wz";
		}
	}

    public static void main(String[]args){
    	System.out.println(verifyUserName("吴"));
    }
}
