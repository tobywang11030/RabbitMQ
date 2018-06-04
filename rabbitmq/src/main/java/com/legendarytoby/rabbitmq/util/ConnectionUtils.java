package com.legendarytoby.rabbitmq.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {

	private ConnectionUtils() {
	}

	public static Connection getConnection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);// Overview->Listening ports : amqp
		factory.setVirtualHost("/admin");
		factory.setUsername("admin");
		factory.setPassword("admin");
		return factory.newConnection();
	}
}
