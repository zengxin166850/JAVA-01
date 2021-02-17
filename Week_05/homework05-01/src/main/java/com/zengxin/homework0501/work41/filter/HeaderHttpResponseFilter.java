package com.zengxin.homework0501.work41.filter;

import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("kk", "java-1-nio");
    }
}
