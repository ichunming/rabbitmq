package com.yimeicloud.study.rabbit_mq;

public class Animal {
	private String topic;
	private String msg;
	
	public Animal(String topic, String msg) {
		this.topic = topic;
		this.msg = msg;
	}
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "topic: " + topic + ";msg: " + msg;
	}
}
