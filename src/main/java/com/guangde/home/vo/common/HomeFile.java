package com.guangde.home.vo.common;

public class HomeFile {
    /**
     * 文件id
     */
	private int id;
	/**
	 * 文件url
	 */
	private String url;
	/**
	 * 文件类型
	 */
	private String type;
	/**
	 * 文件描述
	 */
	private String description;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
