package com.legendarytoby.rabbitmq.topic;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String EXCHANGE_NAME = "test_exchange_topic";

	public static void main(String[] argv) throws Exception {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();

		// Topic Exchange
		// 将路由键和某模式进行匹配。
		// # 表示0个或若干个关键字, * 表示一个关键字
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

		String message = "{id:'001',name:'java'}";

		String routingKey = "book.add";
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());

		System.out.println(" [x] Sent : '" + routingKey + " --> " + message + "'");
		channel.close();
		connection.close();
	}
}