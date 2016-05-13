package com.yimeicloud.study.rabbit_mq;

import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Sender {
	private final static String QUEUE_NAME = "hello";
	private final static String IP = "localhost";
	
	public void send(String msg) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection(); 
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		
		System.out.println("send message:" + msg);
		
		channel.close();
		connection.close();
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		new Sender().send("Hello World!");
	}
}
