package com.xietaojie.lab.rabbit.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.xietaojie.lab.State;
import com.xietaojie.lab.rabbit.model.RpcReplyMsg;
import com.xietaojie.lab.rabbit.model.RpcRequestMsg;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xietaojie1992
 */
@Slf4j
public class RpcServer implements Closeable {

    private final AtomicReference<State> state  = new AtomicReference(State.LATENT);
    private final ObjectMapper           mapper = new ObjectMapper();

    private final RpcRequestHandler requestHandler;
    private final String            queueName;
    private final Connection        connection;

    public RpcServer(Connection connection, String queueName, RpcRequestHandler requestHandler) {
        this.connection = connection;
        this.queueName = queueName;
        this.requestHandler = requestHandler;
    }

    public void start() throws IOException {
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "Cannot be started more than once");

        Channel channel = connection.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicQos(1);

        channel.basicConsume(queueName, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                RpcRequestMsg rpcRequest = mapper.readValue(body, RpcRequestMsg.class);
                log.info("RpcRequest message received. [{}] ", rpcRequest);

                BasicProperties replyProps = new BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
                RpcReplyMsg rpcReply = requestHandler.handle(rpcRequest);
                getChannel().basicPublish("", properties.getReplyTo(), replyProps, mapper.writeValueAsBytes(rpcReply));
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            }
        });
        log.info("awaiting RPC requests");
    }

    @Override
    public void close() throws IOException {
        state.set(State.CLOSED);
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}