package com.legendarytoby.rabbitmq.workqueues;

import java.io.IOException;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv2 {
	private static final  String QUEUE_NAME = "test_queue_work";

	public static void main(String[] args) throws Exception {
		Connection connection = ConnectionUtils.getConnection();
		final Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body);
				System.out.println(" [2] Received '" + message + "'");
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					System.out.println(" [2] Done");
				}
			}
		};
		boolean autoAck = true; //
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}
}