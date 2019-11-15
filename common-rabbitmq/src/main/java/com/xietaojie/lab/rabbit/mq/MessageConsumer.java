package com.xietaojie.lab.rabbit.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.xietaojie.lab.State;
import com.xietaojie.lab.rabbit.model.Msg;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xietaojie1992
 */
@Slf4j
public class MessageConsumer implements Closeable {

    private final ObjectMapper           objectMapper     = new ObjectMapper();
    private final AtomicReference<State> state            = new AtomicReference(State.LATENT);
    private final List<MessageListener>  messageListeners = new ArrayList<>(1);
    private       boolean                autoAck          = true;

    private       Connection connection;
    private       Channel    channel;
    private final String     queueName;

    public MessageConsumer(Connection connection, String queueName) {
        this.connection = connection;
        this.queueName = queueName;
    }

    public MessageConsumer(Channel channel, String queueName) {
        this.channel = channel;
        this.queueName = queueName;
    }

    public void start() throws IOException {
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "Cannot be started more than once");

        // 获取信道
        if (channel == null) {
            channel = connection.createChannel();
        }

        // 声明要订阅的队列
        channel.queueDeclare(queueName, true, false, false, null);
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                //log.info("{}, {}", envelope, properties);
                //log.info("RoutingKey={}， type={}", envelope.getRoutingKey(), properties.getContentType());
                //String bodyStr = new String(body, "UTF-8");
                //log.info("Msg received in Queue[{}] -> {}", queueName, bodyStr);

                //Msg msg = JSON.parseObject(bodyStr, new TypeReference<Msg>() {
                //}.getType());

                Msg msg = objectMapper.readValue(body, Msg.class);
                log.info("Msg received in Queue[{}] -> {}", queueName, msg);
                messageListeners.forEach(l -> l.onMessage(msg));

                // 确认消息（如果开启了自动确认，则不需要重复确认了）
                if (!autoAck) {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }

    public void addListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeListener(MessageListener listener) {
        messageListeners.add(listener);
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
