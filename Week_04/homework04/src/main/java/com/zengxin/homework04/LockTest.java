package com.zengxin.homework04;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock
 */
public class LockTest {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) throws InterruptedException {
        LockTest test = new LockTest();
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        lock.lock();
        new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(200);
                test.hello(1);
                condition.signal();
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //主线程阻塞，等通知
        condition.await();
        lock.unlock();
        System.out.println(test.hello);
    }
}
