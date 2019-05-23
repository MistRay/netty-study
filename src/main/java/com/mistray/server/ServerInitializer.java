package com.mistray.server;

import io.netty.channel.ChannelInitializer;

import java.nio.channels.SocketChannel;

/**
 * @author MistRay
 * @Project netty-study
 * @Package com.mistray.server
 * @create 2019年05月23日 10:25
 * @Desc
 */
public class ServerInitializer implements ChannelInitializer<SocketChannel> {
    /**
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch the {@link Channel} which was registered.
     * @throws Exception is thrown if an error occurs. In that case it will be handled by
     *                   {@link #exceptionCaught(ChannelHandlerContext, Throwable)} which will by default close
     *                   the {@link Channel}.
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

    }
}
