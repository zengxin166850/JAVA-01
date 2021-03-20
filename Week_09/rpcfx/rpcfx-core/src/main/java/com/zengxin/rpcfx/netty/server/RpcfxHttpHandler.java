package com.zengxin.rpcfx.netty.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zengxin.rpcfx.api.RpcfxException;
import com.zengxin.rpcfx.api.RpcfxRequest;
import com.zengxin.rpcfx.api.RpcfxResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * netty处理器，获取bean实例后，调用方法并返回
 *
 * @param <T> T
 * @see ChannelInboundHandlerAdapter 与
 */
public class RpcfxHttpHandler<T> extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RpcfxHttpHandler.class);

    private final ApplicationContext applicationContext;

    RpcfxHttpHandler(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            String uri = fullRequest.uri();
            logger.info("接收到的请求url为{}", uri);
            RpcfxResponse response = invoke(fullRequest);
            //处理响应
            handleResponse(fullRequest, ctx, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 处理响应结果
     * @param fullRequest request
     * @param ctx netty上下文
     * @param response 响应结果
     */
    private void handleResponse(FullHttpRequest fullRequest, ChannelHandlerContext ctx, RpcfxResponse response) {
        FullHttpResponse fullHttpResponse = null;
        try {
            String res = JSON.toJSONString(response, SerializerFeature.WriteClassName);
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(res.getBytes(StandardCharsets.UTF_8)));

            fullHttpResponse.headers().set("Content-Type", "application/json");
            fullHttpResponse.headers().setInt("Content-Length", res.getBytes().length);
            //add response Filter
        } catch (Exception e) {
            e.printStackTrace();
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(fullHttpResponse);
                }
            }
            ctx.flush();
        }
    }

    private RpcfxResponse invoke(FullHttpRequest fullRequest) {
        RpcfxResponse response = new RpcfxResponse();
        String reqJson = fullRequest.content().toString(CharsetUtil.UTF_8);
        logger.info("receive client reqJson: {}", reqJson);
        //获取请求内容
        RpcfxRequest request = JSON.parseObject(reqJson, RpcfxRequest.class);
        String serviceClass = request.getServiceClass();
        try {
            // 作业1：改成泛型和反射
            Class<T> serviceClz = (Class<T>) Class.forName(serviceClass);
            T service = applicationContext.getBean(serviceClz);
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (Exception e) {
            RpcfxException rpcfxException = new RpcfxException(e);
            e.printStackTrace();
            response.setException(rpcfxException);
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}