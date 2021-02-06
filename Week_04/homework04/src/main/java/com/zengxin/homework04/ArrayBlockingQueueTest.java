package com.zengxin.homework04;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * ArrayBlockingQueue，并发包下的其他队列均可以，不重复写
 */
public class ArrayBlockingQueueTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueueTest test = new ArrayBlockingQueueTest();
        ArrayBlockingQueue queue = new ArrayBlockingQueue(1);
        new Thread(() -> {
            try {
                Thread.sleep(200);
                String str = test.hello(1);
                queue.add(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        String str = String.valueOf(queue.take());
        System.out.println(str);
    }
}
