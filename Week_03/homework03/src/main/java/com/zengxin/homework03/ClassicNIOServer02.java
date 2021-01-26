package com.zengxin.homework03;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用java自带的api实现线程池server
 */
public class ClassicNIOServer02 {

    public static void main(String[] args) throws IOException {
        new ClassicNIOServer02(8805).listen();
    }

    private ServerSocketChannel server;
    private Selector selector;


    ClassicNIOServer02(int port) throws IOException {
        server = ServerSocketChannel.open();
        selector = Selector.open();
        server.bind(new InetSocketAddress(port)).configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    void listen() throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                threadPool.execute(() -> {
                    try {
                        process(iterator.next());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            selectionKeys.clear();
        }
    }

    private void process(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
            SocketChannel client = serverChannel.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
        if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("HTTP/1.1 200 OK\r\n".getBytes());
            buffer.put("Content-Type:text/htmL;charset=utf-8\r\n".getBytes());
            buffer.put("\r\n".getBytes());
            buffer.put("hello nio".getBytes());
            buffer.flip();
            client.write(buffer);
            client.close();
//            client.shutdownOutput()
        }
    }

}
