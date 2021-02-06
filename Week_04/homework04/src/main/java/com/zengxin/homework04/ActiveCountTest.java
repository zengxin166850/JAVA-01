package com.zengxin.homework04;

/**
 * 计算存活数量
 */
public class ActiveCountTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) {
        ActiveCountTest test = new ActiveCountTest();
        new Thread(() -> {
            try {
                Thread.sleep(200);
                test.hello(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        while(Thread.activeCount()>2){
            Thread.yield();
        }
        System.out.println(test.hello);
    }
}
