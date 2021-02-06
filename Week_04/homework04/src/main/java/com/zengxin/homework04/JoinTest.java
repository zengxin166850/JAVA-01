package com.zengxin.homework04;

/**
 * join
 */
public class JoinTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        JoinTest joinTest = new JoinTest();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(200);
                joinTest.hello(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
        System.out.println(joinTest.hello);
    }
}
