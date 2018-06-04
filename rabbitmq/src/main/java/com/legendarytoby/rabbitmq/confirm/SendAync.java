package com.legendarytoby.rabbitmq.confirm;

import java.io.IOException;

import com.legendarytoby.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class SendAync {
	private static final String QUEUE_NAME = "QUEUE_simple_confirm_aync";

	/*
	 * Channel 对象提供的 ConfirmListener()回调方法只包含 deliveryTag（当前 Chanel
	 * 发出的消息序号），我们需要自己为每一个 Channel 维护一个 unconfirm 的消息序号集合，每 publish 一条数据，集合中元素加
	 * 1，每回调一次 handleAck方法，unconfirm
	 * 集合删掉相应的一条（multiple=false）或多条（multiple=true）记录。从程序运行效率上看，这个unconfirm
	 * 集合最好采用有序集合 SortedSet 存储结构。 实际上，SDK 中的 waitForConfirms()方法也是通过
	 * SortedSet维护消息序号的。
	 */

	public static void main(String[] args) throws IOException, TimeoutException {
		Connection connection = ConnectionUtils.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		channel.confirmSelect();

		final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

		channel.addConfirmListener(new ConfirmListener() {
			// 每回调一次handleAck方法，unconfirm集合删掉相应的一条（multiple=false）或多条（multiple=true）记录。
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				if (multiple) {
					System.out.println("--multiple--");
					confirmSet.headSet(deliveryTag + 1).clear();// 用一个SortedSet,
																// 返回此有序集合中小于end的所有元素。
				} else {
					System.out.println("--multiple false--");
					confirmSet.remove(deliveryTag);
				}
			}

			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("Nack, SeqNo: " + deliveryTag + ", multiple: " + multiple);
				if (multiple) {
					confirmSet.headSet(deliveryTag + 1).clear();
				} else {
					confirmSet.remove(deliveryTag);
				}

			}

		});
		for (int i = 0; i < 10; i++) {
			String msg = "Send Aync !" + i;
			long nextSeqNo = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			confirmSet.add(nextSeqNo);
		}
		channel.close();
		connection.close();
	}
}