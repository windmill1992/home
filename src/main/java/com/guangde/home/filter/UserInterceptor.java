package com.guangde.home.filter;

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.user.IUserFacade;
import com.guangde.entry.ApiSystemLog;
import com.guangde.home.annotation.ActionLog;
import com.guangde.home.utils.CookieManager;
import com.guangde.home.utils.SSOUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class UserInterceptor  extends HandlerInterceptorAdapter{

	private static Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Resource
    private ICommonFacade commonFacade;

    @Resource
    private IUserFacade userFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uid = SSOUtil.verifyAuth(request, response);
        StringBuffer urlParam = new StringBuffer("?entrance= ");
        //如果未登录登录，则跳转至登录页面
        if (uid == null) {
        	String entranceEncode = SSOUtil.extractEntrance(request);
        	if (logger.isDebugEnabled()) {
                logger.debug("signon entrance after encode = " + entranceEncode);
            }
        	urlParam.append(entranceEncode);
        	response.sendRedirect("/user/sso/login.do"+urlParam.toString());
            return false;
        }

        //check user id exist
        //todo
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception
    {

        String uid = (String) request.getSession().getAttribute(SSOUtil.SESSION_USER);

        String userName = CookieManager.retrieve(CookieManager.LOGIN_NAME, request, false);

       if (StringUtils.isBlank(uid))
        {
            return;
        }

        HandlerMethod handler2=(HandlerMethod) handler;
        ActionLog log = handler2.getMethod().getAnnotation(ActionLog.class);

       /*Method[] methods = handler.getClass().getMethods();
        for (Method method : methods)
        {
            RequestMapping anno = method.getAnnotation(RequestMapping.class);
            ActionLog actionLog = method.getAnnotation(ActionLog.class);

            if (null != anno && null != actionLog)
            {
                String[] values = anno.value();
                if (request.getRequestURI().contains(values[0]))
                {
                    saveSystemLog(request, actionLog, userName, values[0]);
                    return;
                }
            }
        }*/

        if(null != log && null != log.content()){
            saveSystemLog(request, log, userName);
        }
    }

    private void saveSystemLog(HttpServletRequest request, ActionLog actionLog, String userName)
    {
        String content = actionLog.content();

        ApiSystemLog log = new ApiSystemLog();
        log.setUsername(userName);
        //log.setRealName(user.getRealName());
        log.setOperateTime(new Date());
        log.setIP(SSOUtil.getUserIP(request));
        log.setUrl(request.getRequestURI());
        log.setContent(content);
        log.setOperateType(request.getMethod());

        try
        {
            commonFacade.saveSystemLog(log);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
