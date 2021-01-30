package com.zengxin.homework03.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 工厂模式-单线程handler
 */
public class Handler02 implements Runnable {
    final SocketChannel client;
    final SelectionKey key;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);

    public Handler02(Selector selector, SocketChannel client) throws IOException {
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
                process();
                client.close();
//                key.attach(new Sender());
//                key.interestOps(SelectionKey.OP_WRITE);
//                key.selector().wakeup();
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
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("HTTP/1.1 200 OK\r\n".getBytes());
        buffer.put("Content-Type:text/htmL;charset=utf-8\r\n".getBytes());
        buffer.put("\r\n".getBytes());
        buffer.put("hello nio".getBytes());
        buffer.flip();
        client.write(buffer);
    }

}
