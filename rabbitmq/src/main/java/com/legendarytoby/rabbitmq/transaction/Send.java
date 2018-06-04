package com.legendarytoby.rabbitmq.transaction;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	private static final String QUEUE_NAME = "QUEUE_transaction";

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String msg = "transaction msg !";

		/*
		 * RabbitMQ 中与事务机制有关的方法有三个：txSelect(), txCommit()以及 txRollback(),
		 * txSelect 用于将当前 channel 设置成 transaction 模式 txCommit 用于提交事务, txRollback
		 * 用于回滚事务，在通过 txSelect 开启事务之后，我们便可以发布消息给 broker 代理服务器了，如果 txCommit
		 * 提交成功了，则消息一定到达了 broker 了 如果在 txCommit执行之前 broker
		 * 异常崩溃或者由于其他原因抛出异常，这个时候我们便可以捕获异常通过 txRollback 回滚事务了。
		 *
		 * *********此种模式还是很耗时的,采用这种方式 降低了 Rabbitmq 的消息吞吐量
		 */

		try {
			channel.txSelect();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			// int result = 1 / 0;
			channel.txCommit();
		} catch (Exception e) {
			channel.txRollback();
			System.out.println("----msg rollabck ");
		} finally {
			System.out.println("---------send msg :" + msg);
		}
		channel.close();
		connection.close();
	}

}