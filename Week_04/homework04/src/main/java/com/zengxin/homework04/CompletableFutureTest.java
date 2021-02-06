package com.zengxin.homework04;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture
 */
public class CompletableFutureTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFutureTest test = new CompletableFutureTest();
        String str = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
                return test.hello(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }).get();

        System.out.println(str);
    }
}
