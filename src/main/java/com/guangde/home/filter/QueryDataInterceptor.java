package com.guangde.home.filter;

import com.alibaba.fastjson.JSONObject;
import com.guangde.home.utils.SSOUtil;
import com.guangde.home.utils.webUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;



public class QueryDataInterceptor  extends HandlerInterceptorAdapter{

	private static Logger logger = LoggerFactory.getLogger(QueryDataInterceptor.class);



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uid = SSOUtil.verifyAuth(request, response);
        StringBuffer urlParam = new StringBuffer("?entrance= ");
        //如果未登录登录，则跳转至登录页面
        if (uid == null) {
        	String entranceEncode = SSOUtil.extractEntrance(request);
        	urlParam.append(entranceEncode);
        	Map<String,Object> map = webUtil.resMsg(-1, "0001", "未登入", urlParam.toString());
    		JSONObject result = new JSONObject();
    		result.putAll(map);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("expires", "0");
            response.setContentType("text/json; charset=UTF-8");
            if (logger.isDebugEnabled()) {
                logger.debug("jsonValue = " + result);
            }
            response.getWriter().write(result.toJSONString());
            return false;
        }
        //check user id exist
        //todo
        return true;
    }
}
