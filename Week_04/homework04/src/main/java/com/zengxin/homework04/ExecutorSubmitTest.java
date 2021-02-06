package com.zengxin.homework04;

import java.util.concurrent.*;

/**
 * Submit
 */
public class ExecutorSubmitTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorSubmitTest test = new ExecutorSubmitTest();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> future = executorService.submit(() -> {
            try {
                Thread.sleep(200);
                return test.hello(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        });
        System.out.println(future.get());
        executorService.shutdown();

    }
}
