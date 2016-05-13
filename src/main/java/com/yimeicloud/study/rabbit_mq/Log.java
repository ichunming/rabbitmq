package com.yimeicloud.study.rabbit_mq;

public class Log {
	// info, debug, error
	private String type;
	
	private String msg;

	public Log() {}
	
	public Log(String type, String msg) {
		this.type = type;
		this.msg = msg;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "[" + type + "]" + ":" + msg;
	}
}
