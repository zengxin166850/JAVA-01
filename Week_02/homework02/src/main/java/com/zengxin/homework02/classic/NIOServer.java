package com.zengxin.homework02.classic;

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
 * 使用jdk自带的api
 */
public class NIOServer {
    ServerSocketChannel serverSocket;
    Selector selector;
    NIOServer(int port) throws IOException {
        //开门
        serverSocket = ServerSocketChannel.open();
        selector = Selector.open();
        //绑定端口
        serverSocket.bind(new InetSocketAddress(port)).configureBlocking(false);
        //可以接客了
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void listener() throws IOException {
        while (true){
            int num = selector.select();
            if(num==0){
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                process(key);
//                iterator.remove();
            }
            selectionKeys.clear();
        }
    }

    private void process(SelectionKey key) throws IOException {
        if(key.isAcceptable()){
            SocketChannel channel = (SocketChannel) key.channel();
            channel.configureBlocking(false);
            System.out.println("收到来自"+channel.getRemoteAddress()+"的连接");
            channel.register(this.selector,SelectionKey.OP_READ);
        }
        if(key.isReadable()){
            SocketChannel channel = (SocketChannel) key.channel();
            channel.register(this.selector,SelectionKey.OP_WRITE);
//            key.interestOps()
        }
        if(key.isWritable()){
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] bytes = "hello,nio".getBytes();
            buffer.put(bytes);
            channel.write(buffer);
            //是否需要关闭
        }
    }


    public static void main(String[] args) throws IOException {
        new NIOServer(8081).listener();
    }
}
