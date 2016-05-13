package com.yimeicloud.study.rabbit_mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Worker {
	private final static String QUEUE_NAME = "work";
	private final static String IP = "localhost";
	
	public void work() throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		// create connection
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection();
		// create channel
		final Channel channel = connection.createChannel();
		// declare channel, durable=true to make sure queue is durable
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		System.out.println("waiting for task...");
		// prefetchCount
		channel.basicQos(1);

		// create consumer
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		// autoAck = false
		channel.basicConsume(QUEUE_NAME, false, consumer);
		
		while(true) {
			// consume message
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
	        String task = new String(delivery.getBody());
	        System.out.println(task + " start...");
	        Thread.sleep(1000 * 5);
	        System.out.println(task + " end.");
	        // acknowledge
	        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}
	
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		new Worker().work();
	}
}
