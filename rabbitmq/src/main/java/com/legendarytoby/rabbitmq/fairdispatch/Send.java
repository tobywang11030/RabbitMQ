package com.legendarytoby.rabbitmq.fairdispatch;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

//Fair dispatch（公平分发）
public class Send {
	private static final String QUEUE_NAME = "test_queue_work";

	public static void main(String[] argv) throws Exception {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();

		boolean durable = false; //change to true later : Exception
		boolean exclusive = false;
		boolean autoDelete = false;
		channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, null);

		int prefetchCount = 1;
		// 每个消费者发送确认信号之前，消息队列不发送下一个消息过来，一次只处理一个消息
		// 限制发给同一个消费者不得超过1条消息
		channel.basicQos(prefetchCount);
		for (int i = 0; i < 50; i++) {
			String message = "Fair dispatch " + i;
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			Thread.sleep(i * 10);
		}
		channel.close();
		connection.close();
	}
}