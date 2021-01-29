package com.zengxin.homework03.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池处理 process 过程
 */
public class Handler03 implements Runnable {
    final SocketChannel client;
    final SelectionKey key;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    ExecutorService workerPool = Executors.newFixedThreadPool(8);

    public Handler03(Selector selector, SocketChannel client) throws IOException {
        this.client = client;
        client.configureBlocking(false);
        key = client.register(selector, 0);
        key.attach(this);
        key.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

    }

    @Override
    public void run() {
        try {
            client.read(input);
            if (inputIsComplete()) {
                workerPool.execute(new Processor());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class Sender implements Runnable {
        @Override
        public void run() {
            try {
                client.write(output);
                if (outputIsComplete()) {
                    key.cancel();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean inputIsComplete() {
        return key.isReadable();
    }

    boolean outputIsComplete() {
        return key.isWritable();
    }

    void process() throws IOException {
        //逻辑处理
        System.out.println("处理接收到的信息xxx");
    }

    class Processor implements Runnable {

        @Override
        public void run() {
            try {
                process();
                key.attach(new Sender());
                key.interestOps(SelectionKey.OP_WRITE);
                System.out.println("正在处理...xxx");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
