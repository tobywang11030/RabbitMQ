package com.legendarytoby.rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String QUEUE_NAME = "QUEUE_simple";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel(); // queue
		boolean durable = false;
		boolean exclusive = false;
		boolean autoDelete = false;
		channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, null);

		for (int i = 0; i < 20; i++) {
			String message = "Simple QUEUE " + i;
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		}

		System.out.println("Simple QUEUE Send Done");
		channel.close();
		connection.close();
	}

}