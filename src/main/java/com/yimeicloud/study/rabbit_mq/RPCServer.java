package com.yimeicloud.study.rabbit_mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RPCServer {
	private final static String RPC_QUEUE_NAME = "rpc_queue";
	private final static String IP = "localhost";
	
	public void process() throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		System.out.println("waiting for rpc message...");
		
		// create consumer
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		channel.basicConsume(RPC_QUEUE_NAME, true, consumer);
		
		while(true) {
			// consume message
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println(msg);
			
			// return result to client
			String result = "this is result!";
			BasicProperties props = delivery.getProperties();
			BasicProperties responseProps = new BasicProperties.Builder().correlationId(props.getCorrelationId()).build();
	        channel.basicPublish("", props.getReplyTo(), responseProps, result.getBytes());
	        
	        System.out.println("result returned!");
		}
	}
	
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		new RPCServer().process();
	}
}
