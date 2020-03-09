/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package com.xietaojie.lab.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author xietaojie
 * @date 2020-02-05 18:17:14
 * @version $ Id: FAioEchoClient.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class NioTimerClient implements AutoCloseable {

    private String        host = "localhost";
    private Integer       port = 8080;
    private Selector      selector;
    private SocketChannel socketChannel;

    public NioTimerClient(String host, Integer port) throws IOException {
        this.host = host;
        this.port = port;

        // 打开 ServerSocketChannel，监听客户端的连接。它是所有客户端连接的父管道
        // 绑定监听的地址和端口，设置连接为非阻塞模式
        this.socketChannel = SocketChannel.open();
        this.socketChannel.configureBlocking(false);

        // 创建多路复用器
        this.selector = Selector.open();

        // 把 ServerSocketChannel 注册到 Selector 上，监听 Accept 事件
    }

    @Override
    public void close() throws Exception {

    }
}
