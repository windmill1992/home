package com.guangde.home.vo.common;

import com.guangde.home.constant.ResultEnum;


public class Result<T> {

	private int code;

	private String msg;
	
	private T result;

	public Result(ResultEnum resultEnum) {
		super();
		this.code = resultEnum.getState();
		this.msg = resultEnum.getStateInfo();
	}

	public Result(ResultEnum resultEnum, T result) {
		this.code = resultEnum.getState();
		this.msg = resultEnum.getStateInfo();
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Result [code=" + code + ", msg=" + msg + ", result=" + result
				+ "]";
	}
}
