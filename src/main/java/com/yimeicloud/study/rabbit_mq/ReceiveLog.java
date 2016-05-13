package com.yimeicloud.study.rabbit_mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class ReceiveLog {
	private final static String EXCHANGE_NAME = "logs";
	private final static String IP = "localhost";
	
	public void receive(String type) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		// create connection
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection();
		// create channel
		final Channel channel = connection.createChannel();
		
		// declare exchange, type=fanout -->broadcasting
		//channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		// declare exchange, type=direct -->delivered by routing key
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, type);
		System.out.println("waiting for " + type + " log...");

		// create consumer
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		channel.basicConsume(queueName, true, consumer);
		
		while(true) {
			// consume message
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
	        String msg = new String(delivery.getBody());
	        System.out.println(msg);
		}
	}
	
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		// args[0]: INFO, DEBUG, ERROR
		new ReceiveLog().receive(args[0]);
	}
}
