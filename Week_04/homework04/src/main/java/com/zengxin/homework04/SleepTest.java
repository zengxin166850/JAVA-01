package com.zengxin.homework04;

/**
 * sleep
 */
public class SleepTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        SleepTest joinTest = new SleepTest();
        new Thread(() -> {
            try {
                Thread.sleep(200);
                joinTest.hello(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(1000);
        System.out.println(joinTest.hello);
    }
}
