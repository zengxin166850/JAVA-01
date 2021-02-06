package com.zengxin.homework04;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * futureTask
 */
public class FutureTaskTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTaskTest test = new FutureTaskTest();
        FutureTask<String> future = new FutureTask<>(() -> {
            Thread.sleep(200);
            return test.hello(1);
        });
        future.run();
        System.out.println(future.get());
    }
}
