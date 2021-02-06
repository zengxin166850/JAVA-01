package com.zengxin.homework04;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Execute
 */
public class ExecutorExecuteTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) {
        ExecutorExecuteTest test = new ExecutorExecuteTest();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            try {
                Thread.sleep(200);
                test.hello(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        while (test.hello == null) {
            Thread.yield();
        }
        executorService.shutdown();
        System.out.println(test.hello);

    }
}
