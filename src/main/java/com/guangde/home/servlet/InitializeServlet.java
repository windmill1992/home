package com.guangde.home.servlet;

import com.guangde.home.utils.BGConfig;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.StringUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitializeServlet extends HttpServlet {

	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InitializeServlet.class);
	
	public InitializeServlet() {
		super();
	}

	
	public void destroy() {
		super.destroy(); 
		
	}

	protected void service(HttpServletRequest request, HttpServletResponse response){
		 //初始化时，生成一个唯一的会话标识吗（在多服务器没有用户id的情况下，做唯一标识）
	    //将这个回话id存入cookie
	    //todo,将生成会话标识的代码，移到合适的位置（在service里会做不必要的重复判断，sessionlistener里又不能加到cookie，目前没找到合适的地方）
	    
		String prex = BGConfig.get("serverId");
	    if(StringUtils.isBlank(prex)){
	    	prex = "server"+StringUtil.randonNums(4);
	    }
	    if(CookieManager.retrieve(CookieManager.COOKIE_UUID_NAME, request,true)==null){
	    	String code = prex+System.currentTimeMillis()+StringUtil.randonNums(6);
	 	    CookieManager.create(CookieManager.COOKIE_UUID_NAME,code, CookieManager.EXPIRED_30MINUTE, response,true);
	        
	    }
	    //todo 是否有更合适的处理方式

	}
	
	public void init() throws ServletException {
	}

}
