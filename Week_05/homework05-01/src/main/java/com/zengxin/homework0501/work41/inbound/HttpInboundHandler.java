package com.zengxin.homework0501.work41.inbound;


import com.zengxin.homework0501.work41.config.FilterAop;
import com.zengxin.homework0501.work41.outbound.HttpOutboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
@ChannelHandler.Sharable
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ExecutorService inboundThreadPool;
    @Autowired
    private HttpOutboundHandler handler;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @FilterAop
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {

            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            //线程池处理inbound
            inboundThreadPool.submit(() -> handler.handle(fullRequest, ctx, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
