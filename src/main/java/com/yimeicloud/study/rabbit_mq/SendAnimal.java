package com.yimeicloud.study.rabbit_mq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendAnimal {
	private final static String EXCHANGE_NAME = "animals";
	private final static String IP = "localhost";
	
	public void send(List<Animal> animals) throws IOException, TimeoutException {
		// create connection
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(IP);
		Connection connection = factory.newConnection();
		// create channel
		Channel channel = connection.createChannel();
		
		// declare exchange, type=topic -->delivered by routing key,more complex and flexible
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		
		System.out.println("sended animals:");
		for(Animal animal : animals) {
			// publish message, with routingKey
			channel.basicPublish(EXCHANGE_NAME, animal.getTopic(), null, animal.toString().getBytes());
			
			System.out.println(animal.toString());
		}
		
		// close channel and connection
		channel.close();
		connection.close();
	}
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		List<Animal> animals = new ArrayList<Animal>();
		Animal animal = new Animal("quick.white.rabbit", "this is a white rabbit's information!");
		animals.add(animal);
		animal = new Animal("quick.orange.rabbit", "this is an orange rabbit's information!");
		animals.add(animal);
		animal = new Animal("quick.orange.fox", "this is an fox's information!");
		animals.add(animal);
		animal = new Animal("lazy.pink.pig", "this is an pig's information!");
		animals.add(animal);
		new SendAnimal().send(animals);
	}
}
