/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package com.xietaojie.lab;

import com.xietaojie.lab.netty.NettyEchoClient;
import com.xietaojie.lab.netty.NettyEchoServer;

/**
 * @author xietaojie
 * @date 2020-02-16 21:06:21
 * @version $ Id: NettyTests.java, v 0.1  xietaojie Exp $
 */
public class NettyTests {

    public static void main(String[] args) {
        NettyEchoServer server = new NettyEchoServer("localhost", 12345);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NettyEchoClient client = new NettyEchoClient("localhost", 12345);
            client.start();
        }).start();
        server.start();
    }
}
