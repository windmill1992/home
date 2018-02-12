package com.guangde.home.utils.storemanage;

import javax.servlet.http.HttpSession;

public class SessionStoreManageImpl extends StoreManage{

	private  HttpSession session;
	
	public SessionStoreManageImpl(HttpSession session){
		this.session = session;
	}
	
	public Object put(String key, Object value) {
		   session.setAttribute(key, value);
		   return value;
	}

	@Override
	public Object get(String key) {
		return session.getAttribute(key);
	}

	@Override
	public Object delete(String key) {
		session.removeAttribute(key);
		return null;
	}

	@Override
	public Object put(String key, Object value, long time) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
