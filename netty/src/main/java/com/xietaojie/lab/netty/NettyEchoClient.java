package com.xietaojie.lab.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author xietaojie
 * @date 2020-02-10 17:03:23
 * @version $ Id: NettyEchoServer.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class NettyEchoClient {

    private final EventLoopGroup group;
    private final Bootstrap      bootstrap;
    private final String         host;
    private final int            port;
    private       ChannelFuture  channelFuture;

    public NettyEchoClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }

    public void start() {
        try {
            // 创建 Bootstrap
            bootstrap
                    // 指定 EventLoopGroup 以处理客户端事件
                    .group(group)
                    // 适用于 NIO 传输的 Channel 类型
                    .channel(NioSocketChannel.class)
                    //
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyEchoClientHandler());
                            socketChannel.pipeline().addLast(new NettyEchoClientHandler2());
                        }
                    });
            channelFuture = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("shutdown event loop group");
            group.shutdownGracefully();
        }
    }

    public void send(String content) {
        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
    }

    public void shutdown() {
        log.info("shutdown");
        channelFuture.cancel(true);
        channelFuture.channel().close();
    }
}
