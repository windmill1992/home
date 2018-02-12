package com.guangde.home.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.guangde.home.utils.storemanage.StoreManage;

public class webUtil {
	 
	 public static final int FLAG_SUCCESS = 1;
	 public static final int FLAG__FAILED = 0;
	 public static final int LOGIN_FAILED = -1;
	 //error_code
	 public static final String ERROR_CODE_SUCCESS = "0000";//成功
	 public static final String ERROR_CODE_LOGINFAILED = "0001";//未登入的错误
	 public static final String ERROR_CODE_PARAMWRONG = "0002";//参数错误
	 public static final String ERROR_CODE_VALIDATEWRONG = "0003";//验证错误
	 public static final String ERROR_CODE_ADDFAILED = "0010";//添加失败
	 public static final String ERROR_CODE_UPDATEFAILED = "0030";//修改失败
	 public static final String ERROR_CODE_DATAWRONG = "0040";//数据错误
	 public static final String ERROR_CODE_ILLEGAL = "0080";//非法操作
	 
     private static final Object visit = new Object();
	
     //成功返回
     public static Map<String,Object> successRes(Object obj){
    	 return resMsg(webUtil.FLAG_SUCCESS,webUtil.ERROR_CODE_SUCCESS,"成功",obj);
     }
     //成功返回两个object对象
     public static Map<String , Object> successResDoubleObject(Object obj,Object obj1){
    	 return resMsgTwoObject(webUtil.FLAG_SUCCESS,webUtil.ERROR_CODE_SUCCESS,"成功",obj,obj1);
     }
     //失败返回
     public static Map<String,Object> failedRes(String errorCode,String errorMsg,Object obj){
    	 return resMsg(webUtil.FLAG__FAILED,errorCode,errorMsg,obj);
     }
     //未登入
     public static Map<String,Object> loginFailedRes(Object obj){
    	 return resMsg(webUtil.LOGIN_FAILED,webUtil.ERROR_CODE_LOGINFAILED,"未登入",obj);
     }
     public static void failedView(ModelAndView view,String errorCode,String errorMsg){
    	 if(view!=null){
    		 view.addObject("errorCode", errorCode);
    		 view.addObject("errorMsg", errorMsg);
    	 }
     }
     /*
      * @param flag 0:失败，1:成功，-1:未登录
      * @param 成功：0000
      */
	public static Map<String,Object> resMsg(int flag,String errorCode,String errorMsg,Object obj){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("errorCode", errorCode);
		result.put("errorMsg", errorMsg);
		result.put("obj", obj);
	    return result;
	}
	
	public static Map<String, Object> resMsgTwoObject(int flag,String errorCode,String errorMsg,Object obj,Object obj1){
		
		Map<String, Object> result=new HashMap<String ,Object>();
		result.put("flag", flag);
		result.put("errorCode", errorCode);
		result.put("errorMsg", errorMsg);
		result.put("obj", obj);
		result.put("obj1", obj1);
		return result;
	}
	
	//记录访问次数
	public static int visitNums(String key ,StoreManage storeManage,Object lock){
		if(lock==null){
			lock = visit;
		}
		Integer nums = 0;
		synchronized (lock) {
			nums = (Integer)storeManage.get(key);
			if(nums==null){
				nums = 0;
			}else{
				nums++;
			}
			storeManage.put(key, nums);
		}
		return nums;
	}
	
	public static String encodeTodecode(String value) throws UnsupportedEncodingException{
		return URLDecoder.decode(value, "UTF-8");
	}
}
