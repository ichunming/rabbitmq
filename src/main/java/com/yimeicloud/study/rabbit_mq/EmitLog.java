package com.yimeicloud.study.rabbit_mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {
	private final static String EXCHANGE_NAME = "logs";
	private final static String IP = "localhost";
	
	public void sendLog(List<Log> logs) throws IOException, TimeoutException {
		// create connection
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection();
		// create channel
		Channel channel = connection.createChannel();
		
		// declare exchange, type=fanout -->broadcasting
		//channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		// declare exchange, type=direct -->delivered by routing key
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		System.out.println("sended logs:");
		for(Log log : logs) {
			// publish message, without routingKey
			//channel.basicPublish(EXCHANGE_NAME, "", null, log.toString().getBytes());
			
			// publish message, with routingKey
			channel.basicPublish(EXCHANGE_NAME, log.getType(), null, log.toString().getBytes());
			
			System.out.println(log);
		}
		
		// close channel and connection
		channel.close();
		connection.close();
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		List<Log> logs = new ArrayList<Log>();
		Log log = new Log("INFO", "this is an INFO message!");
		logs.add(log);
		log = new Log("DEBUG", "this is an DEBUG message!");
		logs.add(log);
		log = new Log("ERROR", "this is an ERROR message!");
		logs.add(log);
		new EmitLog().sendLog(logs);
	}
}
