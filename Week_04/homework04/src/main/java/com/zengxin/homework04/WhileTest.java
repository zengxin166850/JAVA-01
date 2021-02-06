package com.zengxin.homework04;

/**
 * 循环
 */
public class WhileTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        WhileTest joinTest = new WhileTest();
        new Thread(() -> {
            try {
                Thread.sleep(200);
                joinTest.hello(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        while (joinTest.hello == null) {
            Thread.sleep(10);
        }
        System.out.println(joinTest.hello);
    }
}
