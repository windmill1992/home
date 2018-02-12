package com.guangde.home.vo.message;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.guangde.home.utils.BGConfig;


public class PNews {

	private int id;//新闻id
	private String title;//新闻标题
	private String content;//新闻内容
	private Date cDate;//新闻发布时间
	private long cTime;//新闻发布时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getcDate() {
		return cDate;
	}
	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}
	public long getcTime() {
		return cTime;
	}
	public void setcTime(long cTime) {
		this.cTime = cTime;
	}
	public static String dealContent(String content){
		//新闻图片在admin上
		if(!StringUtils.isBlank(content)){
			String adminhost = BGConfig.get("adminhost");
			content = content.replaceAll("src=\"ui", "src=\""+adminhost+"ui");
			
		}
		return content;
	}
}
