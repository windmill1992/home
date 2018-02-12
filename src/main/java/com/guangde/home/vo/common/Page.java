package com.guangde.home.vo.common;

public class Page {

	private Object data;//数据
	private long total;//分页总数
	private int page=1;//当前页
	private int pageNum=10;//每条页数
	
	private int index;//数据查询起始
	private long nums;//数据总数
	
	private Object info;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public long getTotal() {
		if(nums==0){
			return 0;
		}
		total = (nums - 1) / pageNum + 1;
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	public int getIndex() {
		index = (page-1)*pageNum;
		return index;
	}
	public long getNums() {
		return nums;
	}
	public void setNums(long nums) {
		this.nums = nums;
	}
	public Object getInfo() {
		return info;
	}
	public void setInfo(Object info) {
		this.info = info;
	}
}
