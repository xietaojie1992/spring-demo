package com.xietaojie.lab;

import com.xietaojie.lab.netty.NettyEchoClient;
import com.xietaojie.lab.netty.NettyEchoServer;

import java.io.IOException;

/**
 * @author xietaojie
 */
public class NettyTests {

    public static void main(String[] args) throws InterruptedException, IOException {
        NettyEchoServer server = new NettyEchoServer("localhost", 12345);
        new Thread(() -> server.start()).start();
        Thread.sleep(1000);
        NettyEchoClient client = new NettyEchoClient("localhost", 12345);
        new Thread(() -> client.start()).start();
        Thread sendThread = new Thread(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(1000);
                    client.send("greetings in " + System.currentTimeMillis());
                } catch (InterruptedException e) {
                }
            }
        });
        sendThread.start();
        Thread.sleep(5000);
        sendThread.interrupt();
        client.shutdown();
        server.shutdown();
        Thread.sleep(200);

    }
}
