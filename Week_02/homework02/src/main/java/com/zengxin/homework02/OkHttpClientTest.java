package com.zengxin.homework02;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 测试okhttp
 */
public class OkHttpClientTest {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8801")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            System.out.println(result);
        }
    }
}
