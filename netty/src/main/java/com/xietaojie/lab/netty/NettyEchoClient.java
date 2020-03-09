/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package com.xietaojie.lab.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author xietaojie
 * @date 2020-02-10 17:03:23
 * @version $ Id: NettyEchoServer.java, v 0.1  xietaojie Exp $
 */
public class NettyEchoClient {

    private String host;
    private int    port;

    public NettyEchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建 Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            bootstrap
                    // 指定 EventLoopGroup 以处理客户端事件
                    .group(group)
                    // 适用于 NIO 传输的 Channel 类型
                    .channel(NioSocketChannel.class)
                    // 设置 Server 的 InetSocketAddress
                    .remoteAddress(new InetSocketAddress(host, port))
                    //
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyEchoClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
