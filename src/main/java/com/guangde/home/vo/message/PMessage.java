package com.guangde.home.vo.message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Administrator
 *
 */
public class PMessage {

	private int id;  //消息id
	private String title;//消息标题
	private String content;//消息内容
	private String type;//消息类别
	private int from;//消息发送方
	private String sender;//消息放送方
	private int to;//消息接收方
	private Date creatTime;//消息创建时间
	private String cTimeFormat;
	private int status;//是否已阅读
	private boolean pre = true;
	private boolean next = true;
  
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getcTimeFormat() {
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 cTimeFormat = format.format(creatTime);
		return cTimeFormat;
	}
	public boolean isPre() {
		return pre;
	}
	public void setPre(boolean pre) {
		this.pre = pre;
	}
	public boolean isNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
}
