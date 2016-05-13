package com.yimeicloud.study.rabbit_mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Task {
	private final static String QUEUE_NAME = "work";
	private final static String IP = "localhost";
	
	public void sendTask(String task) throws IOException, TimeoutException {
		// create connection
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection();
		// create channel
		Channel channel = connection.createChannel();
		
		// declare queue, durable=true to make sure queue is durable
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		// publish message, props=MessageProperties.PERSISTENT_TEXT_PLAIN to make sure message is durable
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, task.getBytes());
		
		System.out.println("sended task:" + task);
		
		// close channel and connection
		channel.close();
		connection.close();
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		new Task().sendTask(args[0]);
	}
}
