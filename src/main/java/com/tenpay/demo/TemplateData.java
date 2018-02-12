package com.tenpay.demo;

import java.io.Serializable;

public class TemplateData implements Serializable{
	private static final long serialVersionUID = -8305888188055589072L;
	private Object value;
	private String color;
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
