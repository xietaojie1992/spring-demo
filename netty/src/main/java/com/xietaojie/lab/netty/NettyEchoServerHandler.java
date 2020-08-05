package com.xietaojie.lab.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xietaojie
 * @date 2020-02-16 20:18:31
 * @version $ Id: NettyEchoServerHandler.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        String requestContent = new String(data, CharsetUtil.UTF_8);
        log.info("Server received: {}", requestContent);

        // 把消息 Echo 回 Client
        String responseContent = requestContent;
        //ctx.writeAndFlush(Unpooled.copiedBuffer(responseContent.getBytes()))
        // 写回数据后断开客户端的链接
        //.addListener(ChannelFutureListener.CLOSE)
        //;

        // 处理完消息后，继续传递
        //ctx.fireChannelRead(Unpooled.copiedBuffer(responseContent.getBytes()));

        ctx.write(Unpooled.copiedBuffer(responseContent.getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决的消息冲刷到远程节点，并且关闭该 Channel
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭 Channel
        ctx.close();
    }
}
