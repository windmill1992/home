package com.guangde.home.vo.project;

import java.util.Date;
import java.util.List;

import com.guangde.entry.ApiLeaveWord;

public class PFeedBack {

	private int id;//反馈id
	private long cTime;//反馈时间
	private String uName;//反馈人姓名
	private String content;//反馈内容
	private List<String> imgs;//图片地址数组
	private String imgIds;//图片id   格式 id1,id2,id3
	private int pid;//项目id
	private List<ApiLeaveWord> leaveWordList;
	private Date cDate;//创建时间
	private String showTime;
	/**
	 * 用户类型
	 */
	private Integer userType ; 
	/**
	 * 反馈人头像地址
	 */
	private String userImageUrl ; 
	/**
	 * 项目标题
	 */
	private String title ; 
	/**
	 * 项目领域
	 */
	private String field;
	
	/**
	 * 反馈来源
	 */
	private String source ;
	
	
	
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUserImageUrl() {
		return userImageUrl;
	}
	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public List<ApiLeaveWord> getLeaveWordList() {
		return leaveWordList;
	}
	public void setLeaveWordList(List<ApiLeaveWord> leaveWordList) {
		this.leaveWordList = leaveWordList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getcTime() {
		return cTime;
	}
	public void setcTime(long cTime) {
		this.cTime = cTime;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getImgIds() {
		return imgIds;
	}
	public void setImgIds(String imgIds) {
		this.imgIds = imgIds;
	}
	public Date getcDate() {
		return cDate;
	}
	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}
}
