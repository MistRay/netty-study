package com.mistray.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author MistRay
 * @Project netty-study
 * @Package com.mistray.server
 * @create 2019年05月23日 10:25
 * @Desc
 */
public final class Server {
    public static void main(String[] args) {
        // 创建两个EventLoopGroup对象
        // 创建boss线程组 用于服务端接收客户端的连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 创建worker线程组 用于进行SocketChannel数据的读写
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 设置使用的EventLoopGroup
            serverBootstrap.group(bossGroup,workerGroup)
                    // 设置要被实例化的 NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    // 设置 NioServerSocketChannel 的处理器
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 设置连入服务端的 Client 的 SocketChannel的处理器
                    .childHandler(new ServerInitializer());
            // 绑定端口,并同步等待成功,即启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(8888);
            // 鉴定服务端关闭,并阻塞等待
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 优雅关闭两个EventLoopGroup对象
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
