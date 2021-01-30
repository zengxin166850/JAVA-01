package com.zengxin.homework03.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer03 {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        final ServerSocket serverSocket = new ServerSocket(8803);
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            PrintWriter printwriter = new PrintWriter(socket.getOutputStream(), true);
            printwriter.println("HTTP/1.1 200 OK");
            printwriter.println("Content-Type:text/htmL;charset=utf-8");
            String body = "hello,nio1";
            printwriter.println("Content-Length:" + body.getBytes().length);
            printwriter.println();
            printwriter.write(body);
            printwriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
