package com.xietaojie.lab.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyEchoServerOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        String requestContent = new String(data, CharsetUtil.UTF_8);
        log.info("Server response: {}", requestContent);
        ctx.writeAndFlush(msg, promise);
    }
}
