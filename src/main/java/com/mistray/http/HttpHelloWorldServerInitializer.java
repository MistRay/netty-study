package com.mistray.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

/**
 * @author MistRay
 * @Project netty-study
 * @Package com.mistray.http
 * @create 2019年05月24日 16:35
 * @Desc 初始化
 */
public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /**
         * 或者使用 HttpRequestDecoder & HttpResponseEncoder
         */
        pipeline.addLast(new HttpServerCodec());
        /**
         * 在处理post消息体时需要加上.Aggregator:聚合
         */
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new HttpServerExpectContinueHandler());
        pipeline.addLast(new HttpHelloWorldServerHandler());

    }

}
