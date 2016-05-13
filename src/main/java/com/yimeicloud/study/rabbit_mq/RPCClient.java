package com.yimeicloud.study.rabbit_mq;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RPCClient {
	private final static String RPC_QUEUE_NAME = "rpc_queue";
	private final static String IP = "localhost";
	
	public void process() throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection();
		
		Channel channel = connection.createChannel();
		String responseQueue = channel.queueDeclare().getQueue();
		System.out.println("send message from client...");
		
		String corrId = UUID.randomUUID().toString();
		// build props
		BasicProperties props = new BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(responseQueue)
                .build();

		// send message
		String msg = "this is send message";
		channel.basicPublish("", RPC_QUEUE_NAME, props, msg.getBytes());

		
		// receive result
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(responseQueue, true, consumer);
		
		while(true) {
			// consume message
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();

			System.out.println("received message from server!");
			// return result to client
			if (delivery.getProperties().getCorrelationId().equals(corrId)) {
	            String result = new String(delivery.getBody());
	            System.out.println(result);
	            System.exit(0);
	        }
		}
	}
	
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		new RPCClient().process();
	}
}
