package com.zengxin.homework04;

import java.util.concurrent.locks.LockSupport;

/**
 * lockSupport
 */
public class LockSupportTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) {
        LockSupportTest test = new LockSupportTest();
        Thread main = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(200);
                test.hello(1);
                LockSupport.unpark(main);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        LockSupport.park();
        System.out.println(test.hello);
    }
}
