package com.xietaojie.lab.rabbit.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.utility.BlockingCell;
import com.xietaojie.lab.rabbit.model.RpcReplyMsg;
import com.xietaojie.lab.rabbit.model.RpcRequestMsg;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author xietaojie1992
 */
@Slf4j
public class RpcClient implements Closeable {

    private Channel channel;
    private String  replyQueueName;

    private final String                            queueName;
    private final Connection                        connection;
    private final ObjectMapper                      objectMapper     = new ObjectMapper();
    private final Map<String, BlockingCell<Object>> _continuationMap = new ConcurrentHashMap<>();

    public RpcClient(Connection connection, String queueName) throws IOException {
        this.connection = connection;
        this.queueName = queueName;
        init();
    }

    private void init() throws IOException {
        channel = connection.createChannel();

        // 声明一个 rpc reply 队列，不需要一个已知的名称，因为不会有其他 client 可以使用这个队列（独占）
        replyQueueName = channel.queueDeclare("", false, true, true, null).getQueue();

        // 创建默认 Consumer，消费返回的数据 Rpc 消息
        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleShutdownSignal(String consumerTag, ShutdownSignalException signal) {
                for (Entry<String, BlockingCell<Object>> entry : _continuationMap.entrySet()) {
                    entry.getValue().set(signal);
                }
            }

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                String replyId = properties.getCorrelationId();
                BlockingCell<Object> blocker = _continuationMap.get(replyId);
                _continuationMap.remove(replyId);
                if (null != body) {
                    blocker.set(objectMapper.readValue(body, RpcReplyMsg.class));
                } else {
                    blocker.set(body);
                }
            }
        });
        log.info("RpcClient init. Host={}, QueueName={}", connection.getAddress().getHostAddress(), queueName);
    }

    public RpcReplyMsg call(RpcRequestMsg message)
            throws IOException, ShutdownSignalException, ConsumerCancelledException, TimeoutException {
        long timeStamp = System.currentTimeMillis();
        BlockingCell<Object> k = new BlockingCell<>();
        BasicProperties props;
        synchronized (_continuationMap) {
            String corrId = UUID.randomUUID().toString();
            props = new BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName).build();
            _continuationMap.put(corrId, k);
        }

        channel.basicPublish("", queueName, props, objectMapper.writeValueAsBytes(message));

        Object reply = k.uninterruptibleGet(4000);
        log.info("RabbitMQ RPC time cost={}, msgId={}", System.currentTimeMillis() - timeStamp, message.getId());
        if (reply instanceof ShutdownSignalException) {
            ShutdownSignalException sig = (ShutdownSignalException) reply;
            ShutdownSignalException wrapper = new ShutdownSignalException(sig.isHardError(), sig.isInitiatedByApplication(),
                    sig.getReason(), sig.getReference());
            wrapper.initCause(sig);
            throw wrapper;

        } else if (reply == RpcReplyMsg.class) {
            log.info("RpcReply message received. [{}] ", reply);
            return (RpcReplyMsg) reply;
        } else {
            log.warn("response unrecognized. {}", reply);
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}