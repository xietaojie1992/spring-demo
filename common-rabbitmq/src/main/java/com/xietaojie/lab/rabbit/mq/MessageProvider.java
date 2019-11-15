package com.xietaojie.lab.rabbit.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xietaojie.lab.State;
import com.xietaojie.lab.rabbit.model.Msg;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xietaojie1992
 */
@Slf4j
public class MessageProvider implements Closeable {

    private       Connection             connection;
    private       Channel                channel;
    private final AtomicReference<State> state  = new AtomicReference(State.LATENT);
    private final ObjectMapper           mapper = new ObjectMapper();

    private String queueName;
    private String exchange;
    private String routingKey;

    public MessageProvider(Connection connection, String queueName, String exchange, String routingKey) throws IOException {
        Preconditions.checkNotNull(connection, "Connection Cannot be null");
        this.connection = connection;
        this.queueName = queueName;
        this.exchange = exchange;
        this.routingKey = routingKey;

        init();
    }

    public MessageProvider(Channel channel, String queueName, String exchange, String routingKey) throws IOException {
        Preconditions.checkNotNull(channel, "Channel Cannot be null");
        this.channel = channel;
        this.queueName = queueName;
        this.exchange = exchange;
        this.routingKey = routingKey;

        init();
    }

    private void init() throws IOException {
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "Cannot be initialed more than once");

        // 获得信道
        if (channel == null) {
            channel = connection.createChannel();
        }

        // 声明交换器
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true);

        /**
         * 声明一个队列。
         *
         * durable, 是否持久化（true表示是，队列将在服务器重启时生存)
         * exclusive, 是否是独占队列（创建者可以使用的私有队列，断开后自动删除）
         * autoDelete, 当所有消费者客户端连接断开时是否自动删除队列
         */
        channel.queueDeclare(queueName, true, false, false, null);

        // 进行绑定
        channel.queueBind(queueName, exchange, routingKey);
    }

    public boolean publish(Msg msg) throws IOException {
        if (channel.isOpen()) {
            channel.basicPublish(exchange, routingKey, null, mapper.writeValueAsBytes(msg));
            log.info("message[{}] published for {}.", msg, queueName);
            return true;
        } else {
            log.error("change is not open");
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        state.set(State.CLOSED);
        if (channel != null && channel.isOpen()) {
            try {
                channel.close();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
