package com.zengxin.homework04;

/**
 * Synchronized+wait/notify
 */
public class SynchronizedTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest test = new SynchronizedTest();
        Object oo = new Object();
        synchronized (oo) {
            new Thread(() -> {
                try {
                    synchronized (oo) {
                        Thread.sleep(200);
                        test.hello(1);
                        oo.notify();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            oo.wait();
        }
        System.out.println(test.hello);
    }
}
