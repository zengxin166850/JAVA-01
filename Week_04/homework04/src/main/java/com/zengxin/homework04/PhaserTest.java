package com.zengxin.homework04;

import java.util.concurrent.Phaser;

/**
 * Phaser
 */
public class PhaserTest extends Phaser {
    private String hello = null;

    public String hello(int i) {
        hello = "hello" + i;
        return hello;
    }

    public static void main(String[] args) {
        PhaserTest test = new PhaserTest();
        Phaser phaser = new Phaser(2);
        phaser.register();
        new Thread(() -> {
            try {
                phaser.register();
                Thread.sleep(200);
                test.hello(1);
                phaser.arrive();
                phaser.forceTermination();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        phaser.arriveAndAwaitAdvance();

        System.out.println(test.hello);
    }
}
