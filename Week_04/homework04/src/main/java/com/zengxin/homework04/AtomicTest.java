package com.zengxin.homework04;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Atomic包下的其它类均类似，不再重复写
 */
public class AtomicTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicTest test  = new AtomicTest();
        AtomicInteger atomic = new AtomicInteger(0);
        new Thread(() -> {
            try {
                Thread.sleep(200);
                String str = test.hello(1);
                atomic.addAndGet(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        while (!atomic.compareAndSet(1,1)){
            Thread.sleep(5);
        }
        System.out.println(test.hello);
    }
}
