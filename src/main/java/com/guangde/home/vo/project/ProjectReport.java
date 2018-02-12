package com.guangde.home.vo.project;

import java.util.List;

public class ProjectReport {

	private int id;//报告id
	private String content;//报告内容
	private String imgIds;//图片id集合，格式：id1，id2,id3,id4
	private List<String> imgUrls;//图片urls
	private int type;
	private int pid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgIds() {
		return imgIds;
	}
	public void setImgIds(String imgIds) {
		this.imgIds = imgIds;
	}
	public List<String> getImgUrls() {
		return imgUrls;
	}
	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
}
