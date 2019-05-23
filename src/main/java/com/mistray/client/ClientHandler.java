package com.mistray.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author MistRay
 * @Project netty-study
 * @Package com.mistray.client
 * @create 2019年05月23日 10:24
 * @Desc
 */
@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<String> {


    //异常数据捕获
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.err.println(msg);
    }
}
