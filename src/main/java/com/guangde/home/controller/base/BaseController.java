//package com.guangde.home.controller.base;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.log4j.Logger;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//import com.guangde.home.utils.UserUtil;
//
///**
// *
// */
//public class BaseController {
//
//    protected Logger logger = Logger.getLogger(getClass());
//    protected String promptCode;
//
//    //SEO info
//    protected String pageTitle;
//    protected String pageKeywords;
//    protected String pageDescription;
//
//    private String redirectUrl;
//
//    protected HttpServletRequest request;  
//    protected HttpServletResponse response;  
//    protected HttpSession session;  
//      
//    @ModelAttribute  
//    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
//        this.request = request;  
//        this.response = response;  
//        this.session = request.getSession();  
//    }
//
//
//    public Integer getCurrentUserId() {
//        return UserUtil.getUserId(request, response);
//    }
//
//    protected void put(String key, Object o) {
//    	request.setAttribute(key, o);
//    }
//
//    public String getPromptCode() {
//        return promptCode;
//    }
//
//    public void setPromptCode(String promptCode) {
//        this.promptCode = promptCode;
//    }
//
//
//    private void setResponse() {
//        response.reset();
//    }
//
//    private void setHeader() {
//    	response.setHeader("Cache-Control", "no-cache");
//    	response.setHeader("Pragma", "no-cache");
//    	response.setHeader("expires", "0");
//    }
//}
