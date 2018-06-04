package com.legendarytoby.rabbitmq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SendbatchConfirm {
	private static final String QUEUE_NAME = "QUEUE_simple_confirm";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.confirmSelect();

		for (int i = 0; i < 10; i++) {
			String msg = "Send batch Confirm !" + i;
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		}
		if (!channel.waitForConfirms()) {
			System.out.println("send message failed.");
		} else {
			System.out.println(" send messgae ok ...");
		}
		channel.close();
		connection.close();

	}

}