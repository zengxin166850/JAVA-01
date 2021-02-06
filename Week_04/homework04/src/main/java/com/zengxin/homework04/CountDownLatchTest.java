package com.zengxin.homework04;

import java.util.concurrent.CountDownLatch;

/**
 * countdown
 */
public class CountDownLatchTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchTest test = new CountDownLatchTest();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                Thread.sleep(200);
                test.hello(1);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        countDownLatch.await();
        System.out.println(test.hello);
    }
}
