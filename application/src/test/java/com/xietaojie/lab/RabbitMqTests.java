package com.xietaojie.lab;

import com.rabbitmq.client.ConnectionFactory;
import com.xietaojie.lab.rabbit.model.Msg;
import com.xietaojie.lab.rabbit.model.RpcReplyMsg;
import com.xietaojie.lab.rabbit.model.RpcRequestMsg;
import com.xietaojie.lab.rabbit.mq.MessageConsumer;
import com.xietaojie.lab.rabbit.mq.MessageListener;
import com.xietaojie.lab.rabbit.mq.MessageProvider;
import com.xietaojie.lab.rabbit.rpc.RpcClient;
import com.xietaojie.lab.rabbit.rpc.RpcRequestHandler;
import com.xietaojie.lab.rabbit.rpc.RpcServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xietaojie1992
 */
@Slf4j
public class RabbitMqTests extends ApplicationTests {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final static String        rpcQueueName  = "rpcQueueName";

    private final static String QUEUE       = "msgQ";
    private final static String EXCHANGE    = "msgE";
    private final static String ROUTING_KEY = "msgR";

    @Test
    public void testMq() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(DEFAULT_HOST);
        factory.setPort(DEFAULT_PORT);
        factory.setUsername(DEFAULT_USERNAME);
        factory.setPassword(DEFAULT_PASSWORD);
        ConnectionPool connectionPool = new ConnectionPool(factory);

        MessageConsumer msgConsumer = new MessageConsumer(connectionPool.createConnection(), QUEUE);
        msgConsumer.addListener(new MessageListener() {
            @Override
            public void onMessage(Msg testRpcRequest) {
                log.info("onMessage, {}", testRpcRequest);
            }
        });
        msgConsumer.start();

        RpcRequestMsg rpcRequest = new RpcRequestMsg();
        rpcRequest.setMsgType("request " + System.currentTimeMillis());

        try {
            MessageProvider msgProvider = new MessageProvider(connectionPool.createConnection(), QUEUE, EXCHANGE, ROUTING_KEY);
            Msg request = new Msg();
            request.setMsgType("TOPO");
            request.setTimestamp(System.currentTimeMillis());
            request.setEntity(rpcRequest);
            msgProvider.publish(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.in.read();
    }

    @Test
    public void test() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(DEFAULT_HOST);
        factory.setPort(DEFAULT_PORT);
        factory.setUsername(DEFAULT_USERNAME);
        factory.setPassword(DEFAULT_PASSWORD);

        ConnectionPool connectionPool = new ConnectionPool(factory);

        RpcServer rpcServer = new RpcServer(connectionPool.createConnection(), rpcQueueName, new RpcRequestHandler() {
            @Override
            public RpcReplyMsg handle(RpcRequestMsg request) {
                RpcReplyMsg reply = new RpcReplyMsg();
                reply.succeed();
                return reply;
            }
        });
        rpcServer.start();

        RpcClient rpcClient = new RpcClient(connectionPool.createConnection(), rpcQueueName);

        try {
            while (atomicInteger.get() < 1000) {
                RpcRequestMsg rpcRequest = new RpcRequestMsg();
                rpcRequest.setMsgType("request " + System.currentTimeMillis());

                RpcReplyMsg rpcReply = rpcClient.call(rpcRequest);
                log.info("reply received, {}", rpcReply);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("{}", e);
        } finally {
            rpcClient.close();
            rpcServer.close();
        }

        System.in.read();
    }

}