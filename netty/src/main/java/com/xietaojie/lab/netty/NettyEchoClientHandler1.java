package com.xietaojie.lab.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xietaojie
 * @date 2020-02-16 20:18:31
 * @version $ Id: NettyEchoServerHandler.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class NettyEchoClientHandler1 extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知 Channel 活动的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取msg中的数据
        ByteBuf result = (ByteBuf) msg;
        byte[] bytes = new byte[result.readableBytes()];
        result.readBytes(bytes);
        String resultStr = "second handled msg: " + new String(bytes);
        log.info("Server Received {} : Inbound 1 Is OK", resultStr);

        // 处理完msg中的数据后往msg中重新存放新的数据
        result.writeBytes(resultStr.getBytes());

        // 把消息从当前节点继续往下传递
        ctx.fireChannelRead(result);

        // 把消息从头节点开始传递
        //ctx.pipeline().fireChannelRead(result);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws Exception {
        //log.info("Client received: {}", in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭 Channel
        ctx.close();
    }
}
