package com.zengxin.homework04;

import java.util.concurrent.Semaphore;

/**
 * semaphore
 */
public class SemaphoreTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreTest semaphoreTest = new SemaphoreTest();
        //设置为0，只需一次acquire，1需要两次
        Semaphore semaphore = new Semaphore(0);
//        semaphore.acquire();
        new Thread(() -> {
            try {
                Thread.sleep(200);
                semaphoreTest.hello(1);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        semaphore.acquire();
        System.out.println(semaphoreTest.hello);

    }
}
