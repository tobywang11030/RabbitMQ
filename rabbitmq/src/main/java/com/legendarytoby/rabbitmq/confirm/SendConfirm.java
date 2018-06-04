package com.legendarytoby.rabbitmq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SendConfirm {
	private static final String QUEUE_NAME = "QUEUE_simple_confirm";

	/*
	 * producer 端 confirm 模式的实现原理 生产者将信道设置成 confirm 模式，一旦信道进入 confirm
	 * 模式，所有在该信道上面发布的消息都会被指派一个唯一的 ID(从 1 开始)
	 *
	 * 一旦消息被投递到所有匹配的队列之后，broker 就会发送一个确认给生产者（包含消息的唯一ID）,这就使得生产者知道消息已经正确到达目的队列了
	 *
	 * 如果消息和队列是可持久化的，那么确认消息会将消息写入磁盘之后发出，broker 回传给生产者的确认消息中 deliver-tag
	 * 域包含了确认消息的序列号
	 *
	 * 此外 broker 也可以设置 basic.ack 的 multiple 域，表示到这个序列号之前的所有消息都已经得到了处理。
	 *
	 * confirm
	 * 模式最大的好处在于他是异步的，一旦发布一条消息，生产者应用程序就可以在等信道返回确认的同时继续发送下一条消息，当消息最终得到确认之后，
	 * 生产者应用便可以通过回调方法来处理该确认消息，
	 *
	 * 如果RabbitMQ 因为自身内部错误导致消息丢失，就会发送一条 nack 消息，生产者应用程序同样可以在回调方法中处理该 nack 消息。
	 */

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 普通 confirm 模式：每发送一条消息后，调用 waitForConfirms()方法，等待服务器端confirm。实际上是一种串行
		// confirm 了。
		channel.confirmSelect();

		String msg = "Simple confirm !";
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

		if (!channel.waitForConfirms()) {

			System.out.println("send message failed.");
		} else {
			System.out.println(" send messgae ok ...");
		}
		channel.close();
		connection.close();
	}
}