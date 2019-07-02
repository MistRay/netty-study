package com.mistray.http;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MistRay
 * @Project netty-study
 * @Package com.mistray.http
 * @create 2019年05月24日 16:34
 * @Desc
 */
public class HttpHelloWorldServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelloWorldServer.class);

    private static final Integer PORT = 9688;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            //然后配置NioServerSocketChannel的TCP参数，此处将它的backlog设置为1024，
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.childOption(ChannelOption.TCP_NODELAY, true);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            //调用ServerBootstrap的group方法，将两个NIO线程组当作入参传递到ServerBootstrap中。
            b.group(bossGroup, workerGroup)
                    //接着设置创建的Channel为NioServerSocketChannel，它的功能对应于JDK NIO类库中的ServerSocketChannel类。
                    .channel(NioServerSocketChannel.class)
                    //主要用于处理网络I/O事件，例如记录日志、对消息进行编解码等。
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //最后绑定I/O事件的处理类ChildChannelHandler，它的作用类似于Reactor模式中的handler类，
                    .childHandler(new HttpHelloWorldServerInitializer());
            //绑定端口，同步等待成功
            //服务端启动辅助类配置完成之后，调用它的bind方法绑定监听端口
            //随后，调用它的同步阻塞方法sync等待绑定操作完成。
            //完成之后Netty会返回一个ChannelFuture，它的功能类似于JDK的java.util.concurrent.Future，主要用于异步操作的通知回调。
            Channel ch = b.bind(PORT).sync().channel();
            LOGGER.info("Netty http server listening on port " + PORT);
            //等待服务端监听端口关闭
            //使用f.channel().closeFuture().sync()方法进行阻塞,等待服务端链路关闭之后main函数才退出。
            ch.closeFuture().sync();
        } finally {
            //优雅退出，释放线程池资源
            //调用NIO线程组的shutdownGracefully进行优雅退出，它会释放跟shutdownGracefully相关联的资源。
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
