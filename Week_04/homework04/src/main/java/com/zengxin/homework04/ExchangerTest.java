package com.zengxin.homework04;

import java.util.concurrent.Exchanger;

/**
 * Exchanger
 */
public class ExchangerTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        ExchangerTest test = new ExchangerTest();
        final Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            try {
                Thread.sleep(200);
                String str = test.hello(1);
                exchanger.exchange(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //交换得到另一个线程的结果
        String result = exchanger.exchange("");
        System.out.println(result);
    }
}
