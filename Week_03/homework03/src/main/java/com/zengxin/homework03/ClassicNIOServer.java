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

/**
 * 使用java自带的api实现单线程server
 */
@SuppressWarnings("Duplicates")
public class ClassicNIOServer {

    public static void main(String[] args) throws IOException {
        new ClassicNIOServer(8804).listen();
    }

    private ServerSocketChannel server;
    private Selector selector;


    ClassicNIOServer(int port) throws IOException {
        server = ServerSocketChannel.open();
        selector = Selector.open();
        server.bind(new InetSocketAddress(port)).configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    void listen() throws IOException {
        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                process(iterator.next());
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
        }
    }

}
