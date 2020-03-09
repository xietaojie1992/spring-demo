/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package com.xietaojie.lab.nio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 同步阻塞 IO，
 *
 * @author xietaojie
 * @date 2020-02-05 12:38:40
 * @version $ Id: FAioEchoServer.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class NioTimerServer implements AutoCloseable {

    private          String              host     = "localhost";
    private          Integer             port     = 8080;
    private          Selector            selector;
    private          ServerSocketChannel serverSocketChannel;
    private volatile boolean             isClosed = false;

    /**
     * 初始化 Selector，并绑定监听端口
     *
     * @param host
     * @param port
     * @throws IOException
     */
    public NioTimerServer(String host, Integer port) throws IOException {
        this.host = host;
        this.port = port;

        // 打开 ServerSocketChannel，监听客户端的连接。它是所有客户端连接的父管道
        // 绑定监听的地址和端口，设置连接为非阻塞模式
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(host, port), 1024);
        this.serverSocketChannel.configureBlocking(false);

        // 创建多路复用器
        this.selector = Selector.open();

        // 把 ServerSocketChannel 注册到 Selector 上，监听 Accept 事件
        this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    public void start() {
        // 启动 Reactor 线程
        new Thread(new Reactor()).start();
    }

    @Override
    public void close() throws Exception {

    }

    class Reactor implements Runnable {

        @Override
        public void run() {
            while (!isClosed) {
                try {
                    // 轮询准备就绪的 Key
                    selector.select(1000);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        try {
                            handleSelectKey(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Server 停止后，关闭 Selector，所有注册在上面的 Channel 和 Pipe 等资源都会被自动关闭
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleSelectKey(SelectionKey selectionKey) throws IOException {
            if (selectionKey.isValid()) {
                // 处理新接入的请求消息
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                    // Selector 监听到有新的客户端接入，处理新的接入请求，完成 TCP 三次握手，建立物理连接
                    SocketChannel sc = ssc.accept();
                    // 设备 Client 连接为非阻塞模式
                    sc.configureBlocking(false);
                    // 将 Client 连接注册到 Selector，监听读操作，读取客户端的网络消息
                    sc.register(selector, SelectionKey.OP_READ);
                }

                if (selectionKey.isReadable()) {
                    SocketChannel sc = (SocketChannel) selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    // 异步读取 Client 请求的消息到缓冲区
                    int readBytes = sc.read(readBuffer);
                    if (readBytes > 0) {
                        readBuffer.flip();
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);

                        String body = new String(bytes, "UTF-8");
                        log.info("Time server receives request: " + body);
                        write(sc, "Server current time : " + System.currentTimeMillis());
                    } else if (readBytes < 0) {
                        //对端链路关闭
                        selectionKey.cancel();
                        sc.close();
                    } else {
                        // 读到 0 字节，忽略
                    }
                }
            }
        }

        private void write(SocketChannel sc, String resp) throws IOException {
            if (StringUtils.isNoneEmpty(resp)) {
                byte[] bytes = resp.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(resp.getBytes().length);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                sc.write(writeBuffer);
            }
        }

    }

}
