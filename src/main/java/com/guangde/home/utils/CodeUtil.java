package com.guangde.home.utils;

import java.awt.image.BufferedImage;
import java.util.Date;

import com.guangde.api.commons.ICommonFacade;
import com.guangde.api.homepage.IProjectFacade;
import com.guangde.entry.ApiAnnounce;
import com.guangde.home.utils.storemanage.StoreManage;
import com.guangde.pojo.ApiResult;

//验证统一处理类
public class CodeUtil {
    
	//页面验证码业务代码
		public static final String icode_type_r = "register";//注册业务
		public static final String certificationprex="certification";//个人认证
		public static final String enterprise_validate="enterprise_validate";
		public static final String login = "login";
	
	//图片验证码
    public static BufferedImage imgCode(String key,StoreManage storeManage) throws Exception{
    	String code = StringUtil.randonNums(4);
    	BufferedImage img = ImageUtil.codeImg(code);
    	storeManage.put(key, code);
    	return img;
    }
    //校验验证码
    /**
     * 
     * @param key
     * @param storeManage
     * @param code
     * @param delete
     * @return -1 校验码不存在或过期 ; 1 校验成功 ; 0 校验失败
     */
    public static int VerifiCode(String key,StoreManage storeManage,String code,boolean delete){
    	String tcode = (String)storeManage.get(key);
    	if(tcode==null){
    		return -1;
    	}
    	if(tcode.equals(code)){
    		if(delete){
    		  storeManage.delete(key);
    		}
    		return 1;
    	}else{
    		return 0;
    	}
    }
    /**
     * 删除验证码
     * @param key
     * @param storeManage
     */
    public static void deleteCode(String key,StoreManage storeManage){
    	storeManage.delete(key);
    }
    //token验证吗
    public static String tokenCode(String key,StoreManage storeManage){
    	String code = StringUtil.randonNums(12);
    	storeManage.put(key, code);
    	return code;
    }
    
    //手机验证码
    public static String phoneCode(String key,String mobile,StoreManage storeManage){
    	String code = StringUtil.randonNums(6);
    	//todo调用短信接口
    	ICommonFacade commonFacade = SpringContextUtil.getBean("commonFacade", ICommonFacade.class);
    	ApiAnnounce apiAnnounce = new ApiAnnounce();
    	apiAnnounce.setContent("欢迎使用手机验证码: "+code+" ,请在10分钟内完成验证。如非本人操作，请致电客服");
    	apiAnnounce.setCause("手机验证码");
    	apiAnnounce.setDestination(mobile);
    	apiAnnounce.setType(1);
    	apiAnnounce.setPriority(1);
    	ApiResult result = commonFacade.sendSms(apiAnnounce, false);
    	if(result.getCode()==1){
    		storeManage.put(key, code);
        	return code;
    	}else{
    		return null;
    	}
    }
    
    public static String getBuyKey(Integer uid,Integer pid){
    	return "buy_oper_"+uid+"_"+pid;
    }
}
