package com.zengxin.homework0501.work41.outbound;


import com.alibaba.fastjson.JSONObject;
import com.zengxin.homework0501.work41.config.FilterAop;
import com.zengxin.homework0501.work41.config.RouterAop;
import com.zengxin.homework0501.work41.jms.SendService;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Component
public class HttpOutboundHandler {

    private CloseableHttpAsyncClient httpclient;

    @Autowired
    ExecutorService outboundThreadPool;

    @Autowired
    SendService sendService;

    public HttpOutboundHandler() {
        int cores = Runtime.getRuntime().availableProcessors();

        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();

        httpclient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response, context) -> 6000)
                .build();
        httpclient.start();
    }

    @RouterAop
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        //将路由之后的 url 发送出去
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("headers-mao", fullRequest.headers().get("mao"));
        jsonObject.put("url", url);
        //fullRequest 及 ChannelHandlerContext 不能序列化，内容会丢失
//        jsonObject.put("ctx", ctx);
//        jsonObject.put("isKeepAlive", HttpUtil.isKeepAlive(fullRequest));
        sendService.send(jsonObject.toString());
        System.out.println("发送成功");

        //线程池处理outbound
        outboundThreadPool.submit(() -> fetchGet(fullRequest, ctx, url));
    }

    public void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        final HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        httpGet.setHeader("mao", inbound.headers().get("mao"));

        httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(final HttpResponse endpointResponse) {
                try {
                    byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());
                    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
                    //outbound filter
                    handleResponse(inbound, ctx, response);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }

            @Override
            public void failed(final Exception ex) {
                httpGet.abort();
                ex.printStackTrace();
            }

            @Override
            public void cancelled() {
                httpGet.abort();
            }
        });
    }

    @FilterAop
    public void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
        try {

            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
            response.headers().set("kk", "java-1-nio");
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
