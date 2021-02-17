package com.zengxin.homework0501.work41.jms;

import com.zengxin.homework0501.work41.outbound.HttpOutboundHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.IOException;

@Component(value = "requestListener")
public class RequestListener implements MessageListener {

//    private final ExecutorService outboundThreadPool = Executors.newFixedThreadPool(8);

    //    @Autowired
    HttpOutboundHandler outboundHandler;

    //收到信息时的动作
    @Override
    public void onMessage(Message m) {
        ObjectMessage message = (ObjectMessage) m;
        try {
            String url = (String) message.getObject();
            System.out.println("接收到的url为：" + url);
            //收到消息后处理
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String result = response.body().string();
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            outboundThreadPool.submit(() -> outboundHandler.fetchGet(fullRequest, ctx, url));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}