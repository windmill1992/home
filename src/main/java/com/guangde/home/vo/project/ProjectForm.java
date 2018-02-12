package com.guangde.home.vo.project;

import java.io.Serializable;



public class ProjectForm implements Serializable{
	/**
	 * 发起项目的参数
	 */
	private static final long serialVersionUID = 5218358284746182869L;
	//每次显示条数
	public int len=9;
	//显示第几页
	public int page=1;
	//项目类型 领域
	public int type;
	//项目类型 领域别名
	public String typeName;
	//项目标签
	public String tag;
	//状态
	public Integer status;
	//标题
	public String title;
	//项目Id
	public int itemId;
	//介绍
	public String information;
	//善管家 领域
	public String field;
	//是否通过
	public String checkRst;
	//理由
	public String reason;
	//图片ID
	public String imagesId;
	//区域
	public String location ;
	
	//发起人userID
	public int userID;
	public int key;
	
	/**
	 * 排序类型
	 * sortType
	 * 0： 最新发布（时间倒序）
	 * 1： 关注最多（捐款人最多）
	 * 2： 最早发布（时间顺序）
	 * 3： 最新反馈（根据求助人反馈的时间，最新的反馈排在最前）
	 * 4： 受捐金额
	 */
	public Integer sortType;
	
	// 关键字
	public String keyWords;
	/**项目捐助状态(0:未捐，1:已捐，2:全部)*/
	private Integer state;
	
	//项目反馈信息列表
	
	public String getKeyWords() {
		return keyWords;
	}
	
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getImagesId() {
		return imagesId;
	}

	public void setImagesId(String imagesId) {
		this.imagesId = imagesId;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCheckRst() {
		return checkRst;
	}

	public void setCheckRst(String checkRst) {
		this.checkRst = checkRst;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	@Override
	public String toString() {
		return "ProjectForm [checkRst=" + checkRst + ", field=" + field
				+ ", imagesId=" + imagesId + ", information=" + information
				+ ", itemId=" + itemId + ", len=" + len + ", page=" + page
				+ ", reason=" + reason + ", sortType=" + sortType + ", status="
				+ status + ", title=" + title + ", type=" + type + ", userID=" + userID
				+ ", typeName=" + typeName + "]";
	}


}
