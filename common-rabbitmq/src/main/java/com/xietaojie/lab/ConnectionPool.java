package com.xietaojie.lab;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RabbitMQ Connection Pool，Connection 对象创建的是 TCP 连接，创建和销毁本身都是很消耗资源的。
 * 解决办法就是把 Connection 缓存起来，一直不关闭，在传输数据时共享同一个 Connection 即可，即在一个 Connection 对象上创建多个 Channel 来实现数据传输。
 *
 * @author xietaojie1992
 */
public class ConnectionPool {

    private static final int DEFAULT_CONNECTION_CACHE_SIZE = 10;

    private final AtomicInteger    counter     = new AtomicInteger(0);
    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());

    private final ConnectionFactory connectionFactory;

    private int connectionCacheSize = DEFAULT_CONNECTION_CACHE_SIZE;

    public ConnectionPool(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;

    }

    public int getConnectionCacheSize() {
        return connectionCacheSize;
    }

    public void setConnectionCacheSize(int connectionCacheSize) {
        this.connectionCacheSize = connectionCacheSize;
    }

    public Connection createConnection() {
        if (connections.size() < connectionCacheSize) {
            Connection connection;
            try {
                connection = connectionFactory.newConnection();
                connections.add(connection);
                return connection;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return connections.get(counter.getAndIncrement() % connectionCacheSize);
    }
}
