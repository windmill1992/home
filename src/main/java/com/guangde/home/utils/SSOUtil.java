package com.guangde.home.utils;

import com.guangde.entry.ApiFrontUser;
import com.guangde.home.constant.PengPengConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class SSOUtil {

	private static Logger logger = LoggerFactory.getLogger(SSOUtil.class);
	public static final String SESSION_USER = "c_user_id";

	public static void login(ApiFrontUser user, HttpServletRequest request,
			HttpServletResponse response) {
		// 写入session
		writeLoginSession(user, request);
		// 写入cookie
		writeLoginCookie(user, response);
	}

	public static void loginOut(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().invalidate();
		CookieManager.remove(CookieManager.LOGIN_ID, response, true);
		CookieManager.remove(CookieManager.LOGIN_NAME, response, true);
		CookieManager.remove(CookieManager.LOGIN_LASTTIME, response, false);
		CookieManager.remove(CookieManager.LOGIN_NAME, response, false);
		CookieManager.remove(CookieManager.USER_TYPE, response, false);
		CookieManager.remove(CookieManager.HUZHU_TOKEN, response, false);
		CookieManager.remove(CookieManager.USER_HEAD, response, false);
		request.getSession().removeAttribute(SESSION_USER);
	}

	public static void loginOut_test(HttpServletRequest request,
								HttpServletResponse response) {
		//request.getSession().invalidate();
		CookieManager.remove(CookieManager.LOGIN_ID, response, true);
		CookieManager.remove(CookieManager.LOGIN_NAME, response, true);
		CookieManager.remove(CookieManager.LOGIN_LASTTIME, response, false);
		CookieManager.remove(CookieManager.LOGIN_NAME, response, false);
		CookieManager.remove(CookieManager.USER_TYPE, response, false);
		CookieManager.remove(CookieManager.HUZHU_TOKEN, response, false);
		CookieManager.remove(CookieManager.USER_HEAD, response, false);
		request.getSession().removeAttribute(SESSION_USER);
	}

	public static void writeLoginSession(ApiFrontUser user,
			HttpServletRequest request) {
		request.getSession()
				.setAttribute(SESSION_USER, user.getId().toString());
	}

	public static void writeLoginCookie(ApiFrontUser user,
			HttpServletResponse response) {
		// 保存登录cookie信息
		try {
			CookieManager
					.create(CookieManager.LOGIN_ID, user.getId().toString(),
							CookieManager.EXPIRED_DEFAULT_MINUTE, response, true);
			CookieManager.create(CookieManager.LOGIN_NAME, user.getUserName(),
					CookieManager.EXPIRED_DEFAULT_MINUTE, response, true);
			CookieManager.create(CookieManager.LOGIN_LASTTIME,
					String.valueOf(System.currentTimeMillis()),
					CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			// 明码记录用户名，便于前端使用
			CookieManager.create(CookieManager.LOGIN_NAME, URLEncoder.encode(user.getUserName(),"utf-8"),
					CookieManager.EXPIRED_REMEBER, response, false);
			CookieManager.create(CookieManager.USER_TYPE, user.getUserType(),
					CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			CookieManager.create(CookieManager.HUZHU_TOKEN, user.getId().toString()+"_TOKEN", CookieManager.EXPIRED_30MINUTE, response, false);
			writeUserHead(response,user.getCoverImageUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeUserHead(HttpServletResponse response,String url){
		try{
			if(!StringUtils.isBlank(url)){
				CookieManager.create(CookieManager.USER_HEAD,URLEncoder.encode(url,"utf-8"),
						CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			}else{
				//good 代表没有头像
				CookieManager.create(CookieManager.USER_HEAD,URLEncoder.encode("good","utf-8"),
						CookieManager.EXPIRED_DEFAULT_MINUTE, response, false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String verifyAuth(HttpServletRequest request,
			HttpServletResponse response) {
		String uid = null;
		try {
			uid = CookieManager.retrieve(CookieManager.LOGIN_ID, request, true);
		} catch (Exception e) {
			e.printStackTrace();
			uid = null;
		}
		return uid;
	}
	
	public static String extensionPeople(HttpServletRequest request,
			HttpServletResponse response) {
		String uid = null;
		try {
			uid = CookieManager.retrieve(CookieManager.PROJECTID_EXTENSIONPEOPLE, request, true);
		} catch (Exception e) {
			e.printStackTrace();
			uid = null;
		}
		return uid;
	}
	
	public static String getCurrentUserName(HttpServletRequest request,
			HttpServletResponse response){
		String userName = CookieManager.retrieve(CookieManager.LOGIN_NAME, request, false);
		if(userName==null){
			return null;
		}else{
			try {
				return URLDecoder.decode(userName,"utf-8");
			} catch (UnsupportedEncodingException e) {
				return userName;
			}
		}
	}
	
	public static String getCurrentUserType(HttpServletRequest request,
			HttpServletResponse response){
		String userType = CookieManager.retrieve(CookieManager.USER_TYPE, request, false);
		if(userType==null){
			return null;
		}else{
			return userType;
		}
	}
	
	/**
	 * 取得用户的 IP 地址.
	 * 
	 * @param request
	 *            WEB 请求
	 * @return
	 */
	public static String getUserIP(HttpServletRequest request) {
		String ipaddress = request.getHeader("X-Real-IP");
		if (ipaddress == null) {
			ipaddress = request.getHeader("X-Forwarded-For");
		}
		if (ipaddress == null) {
			ipaddress = request.getRemoteAddr();
		} else {
			int index = ipaddress.indexOf(',');
			if (index != -1) {
				ipaddress = ipaddress.substring(0, index);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("ipaddress = " + ipaddress);
		}

		return ipaddress;
	}
	
	/**
     * 取得该请求的路径和参数，参数和路径之间的 ? 用 | 代替，并 URL Encode ，以便这个路径作为一个参数被传递到另一个地址.
     * <p>
     * 例如一个请求地址为 >> http://www.cp2y.com/my/viewMember.m5?memberId=1&sex=0
     * ，转换后 >>>
     * http%3A%2F%2Fwww.cp2y.com%2Fmy%2FviewMember.m5%7CmemberId%3D1%26sex%3D0
     * </p>
     *
     * @param request Http 请求
     * @return 字符串
     * @throws java.io.UnsupportedEncodingException
     *          字符集异常
     */
    public static String extractEntrance(HttpServletRequest request) throws UnsupportedEncodingException {
        StringBuffer entrance = new StringBuffer(request.getRequestURL());
        if (request.getQueryString() != null) {
            entrance.append("?");
            entrance.append(new String(request.getQueryString().getBytes("8859_1"), PengPengConstants.ENCODING));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("entrance before encode = " + entrance.toString());
        }
        return URLEncoder.encode(entrance.toString(), "GBK");
    }
}
