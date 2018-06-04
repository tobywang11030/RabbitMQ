package com.legendarytoby.rabbitmq.routing;

import java.io.IOException;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv1 {
	private static final String QUEUE_NAME = "test_queue_direct_1";
	private static final String EXCHANGE_NAME = "test_exchange_direct";

	public static void main(String[] argv) throws Exception {
		Connection connection = ConnectionUtils.getConnection();
		final Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		String[] routingKeys = new String[] { "update", "delete" };
		for (String routingKey : routingKeys) {
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey); // queue Bind routingKey
		}

		channel.basicQos(1);
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body);
				System.out.println("[1] Recv msg:" + msg);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("[1] done ");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}
}