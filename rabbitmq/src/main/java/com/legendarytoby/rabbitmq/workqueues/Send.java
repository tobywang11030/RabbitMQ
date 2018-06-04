package com.legendarytoby.rabbitmq.workqueues;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

//Round-robin（轮询分发）
public class Send {
	private static final String QUEUE_NAME = "test_queue_work";

	public static void main(String[] argv) throws Exception {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		for (int i = 0; i < 50; i++) {
			String message = "work queues " + i;
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			Thread.sleep(i * 10);
		}
		channel.close();
		connection.close();
	}
}