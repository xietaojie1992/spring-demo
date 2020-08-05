package com.xietaojie.lab.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xietaojie
 */
@Slf4j
public class NettyEchoServer {

    private static final String DEFAULT_HOST = "localhost";
    private static final int    DEFAULT_PORT = 8080;

    /**
     * boss 线程组，用于接收 Client 端的连接
     */
    private final EventLoopGroup bossGroup;

    /**
     * worker 线程组，用于实际处理 Client 来的业务请求
     */
    private final EventLoopGroup workerGroup;

    /**
     * Server 辅助类，用于进行一系列的配置
     */
    private final ServerBootstrap serverBootstrap;

    private ChannelFuture channelFuture;

    private final String host;
    private final int    port;

    public NettyEchoServer() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public NettyEchoServer(int port) {
        this(DEFAULT_HOST, port);
    }

    public NettyEchoServer(String host, int port) {
        this.host = host;
        this.port = port;

        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
    }

    public void start() {
        try {

            serverBootstrap
                    // 放入两个工作组
                    .group(bossGroup, workerGroup)
                    // 指定使用 NioServerSocketChannel 这种类型的通道
                    .channel(NioServerSocketChannel.class)
                    //
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 设置发送缓冲大小
                    .childOption(ChannelOption.SO_SNDBUF, 50 * 1024)
                    // 是指接受缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 50 * 1024)
                    //
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyEchoServerHandler());
                            socketChannel.pipeline().addLast(new NettyEchoServerOutboundHandler());
                        }
                    });
            // 绑定IP 和端口，并同步等待结果
            channelFuture = serverBootstrap.bind(new InetSocketAddress(host, port)).sync();
            log.info("{} started and listening for connections on {}", NettyEchoServer.class.getName(),
                    channelFuture.channel().localAddress());

            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 退出，释放线程池资源
            log.info("shutdown all event loop groups");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void shutdown() {
        log.info("shutdown");
        channelFuture.cancel(true);
        channelFuture.channel().close();

    }
}
