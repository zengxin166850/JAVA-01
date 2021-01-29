package com.zengxin.homework03.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 单线程handler
 */
public class Handler implements Runnable {
    final SocketChannel client;
    final SelectionKey key;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0;
    static final int SENDING = 1;
    int state = READING;

    public Handler(Selector selector, SocketChannel client) throws IOException {
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
            if (state == READING) {
                read();
            }
            if (state == SENDING) {
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void read() throws IOException {
        client.read(input);
        if (inputIsComplete()) {
            process();
//如果是双端通信，需取消注释，process之后不关闭socket
//模拟http服务，则只需要accept或read即可
//            client.close();
//            state = SENDING;
//            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void send() throws IOException {
        client.write(output);
        if (outputIsComplete()) {
            key.cancel();
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
