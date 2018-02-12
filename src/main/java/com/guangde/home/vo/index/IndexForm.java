package com.guangde.home.vo.index;

import java.io.Serializable;
import java.util.List;

import com.guangde.pojo.ApiFocusMap;

public class IndexForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5380076884477906952L;
	protected List<ApiFocusMap> focusList;	//焦点图的列表

	public List<ApiFocusMap> getFocusList() {
		return focusList;
	}

	public void setFocusList(List<ApiFocusMap> focusList) {
		this.focusList = focusList;
	}
	
//	protected List<ApiFocusMap> announcementList;	//公告列表
}
