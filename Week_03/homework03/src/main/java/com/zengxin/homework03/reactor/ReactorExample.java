package com.zengxin.homework03.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * doug lea nio 单reactor例子
 */
public class ReactorExample implements Runnable {
    public static void main(String[] args) throws IOException {
        new Thread(new ReactorExample(8805)).start();
    }

    Selector selector;
    ServerSocketChannel server;

    ReactorExample(int port) throws IOException {
        selector = Selector.open();
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        SelectionKey selectionKey = server.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {
        while (true) {
            try {
                int num = selector.select();
                if (num == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    //转发
                    dispatch(iterator.next());
                }
                selectionKeys.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void dispatch(SelectionKey key) {
        Runnable attachment = (Runnable) key.attachment();
        if (attachment != null) {
            attachment.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel client = server.accept();
                if (client != null) {
//                    new Handler(selector, client);
//                    new Handler02(selector, client);
                    new Handler03(selector, client);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
