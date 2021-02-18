package com.zengxin.homework0501.work41.jms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.protocol.HTTP;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component(value = "requestListener")
public class RequestListener implements MessageListener {


    //收到信息时的动作,只模拟了请求。
    // request 无法序列化后存入消息中，应该如何与发送方的request进行关联？
    @Override
    @JmsListener(destination = "test.queue")
    public void onMessage(Message m) {
        ObjectMessage message = (ObjectMessage) m;
        try {
            JSONObject jsonObject = (JSONObject) JSON.parse((String) message.getObject());
//            ChannelPipeline ctx = JSON.toJavaObject((JSONObject) jsonObject.get("ctx"), ChannelPipeline.class);
//            Boolean isKeepAlive = (Boolean) jsonObject.get("isKeepAlive");
            String mao = (String) jsonObject.get("headers-mao");
            String url = (String) jsonObject.get("url");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url)
                    .addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE)
                    .addHeader("mao", mao)
                    .build();

            try (Response endpointResponse = client.newCall(request).execute()) {
                String result = endpointResponse.toString();
                System.out.println("请求结果为：" + result);
//                byte[] body = endpointResponse.body().bytes();
//                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
                //增强
//                handleResponse(isKeepAlive, ctx, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

//    private void handleResponse(Boolean isKeepAlive, final ChannelPipeline ctx, FullHttpResponse response) {
//        try {
//
//            response.headers().set("Content-Type", "application/json");
//            response.headers().setInt("Content-Length", response.content().readableBytes());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
//        } finally {
//            if (isKeepAlive) {
//                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
//            } else {
//                ctx.write(response);
//            }
//        }
//        ctx.flush();
//    }

}
