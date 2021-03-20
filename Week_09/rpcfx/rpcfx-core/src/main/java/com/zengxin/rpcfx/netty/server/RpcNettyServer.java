package com.zengxin.rpcfx.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import static io.netty.channel.unix.UnixChannelOption.SO_REUSEPORT;

/**
 * RpcNettyServer,
 * 通过@Bean初始化 port 等
 */
public class RpcNettyServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcNettyServer.class);
    private final Integer port;
    private Integer bossGroupNum = 1;
    private Integer workerGroupNum = 16;
    private final ApplicationContext applicationContext;

    public RpcNettyServer(Integer port, ApplicationContext context) {
        this.port = port;
        this.applicationContext = context;
    }

    public RpcNettyServer(Integer port, Integer bossGroupNum, Integer workerGroupNum, ApplicationContext applicationContext) {
        this.port = port;
        this.bossGroupNum = bossGroupNum;
        this.workerGroupNum = workerGroupNum;
        this.applicationContext = applicationContext;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossGroupNum);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerGroupNum);
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .option(SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(1024 * 1024))
                                    .addLast(new RpcfxHttpHandler<>(applicationContext));
                        }
                    });
            Channel ch = b.bind(port).sync().channel();
            logger.info("开启netty http服务器，监听地址和端口为 http://127.0.0.1:{}/", port);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
