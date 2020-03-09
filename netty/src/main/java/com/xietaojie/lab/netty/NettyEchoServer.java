/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package com.xietaojie.lab.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xietaojie
 * @date 2020-02-10 17:03:23
 * @version $ Id: NettyEchoServer.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class NettyEchoServer {

    private String host;
    private int    port = 8080;

    public NettyEchoServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        NettyEchoServerHandler nettyEchoServerHandler = new NettyEchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    //
                    .group(group)
                    //
                    .channel(NioServerSocketChannel.class)
                    //
                    .localAddress(new InetSocketAddress(host, port))
                    //
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(nettyEchoServerHandler);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            log.info("{} started and listening for connections on {}", NettyEchoServer.class.getName(),
                    channelFuture.channel().localAddress());
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
