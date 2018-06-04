package com.legendarytoby.rabbitmq.routing;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String EXCHANGE_NAME = "test_exchange_direct";

	public static void main(String[] argv) throws Exception {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();

		// Direct Exchange 处理路由键。
		// 需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键
		// “dog”，则只有被标记为“dog”的消息才被转发，不会转发 dog.puppy，也不会转发dog.guard，只会转发 dog。

		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

		String message = "{id:'001',name:'book'}";

		String routingKey = "delete";

		channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
		System.out.println(" [x] Sent : '" + routingKey + " --> " + message + "'");

		channel.close();
		connection.close();
	}
}
