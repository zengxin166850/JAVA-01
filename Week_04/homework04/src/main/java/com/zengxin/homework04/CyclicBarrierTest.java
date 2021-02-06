package com.zengxin.homework04;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier
 */
public class CyclicBarrierTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrierTest test = new CyclicBarrierTest();
        CyclicBarrier barrier = new CyclicBarrier(2);
        new Thread(() -> {
            try {
                Thread.sleep(200);
                test.hello(1);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
        barrier.await();
        System.out.println(test.hello);
    }
}
