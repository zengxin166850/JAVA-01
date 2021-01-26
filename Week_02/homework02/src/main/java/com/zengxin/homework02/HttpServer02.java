package com.zengxin.homework02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer02 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8802);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        service(socket);
                    }
                }).start();
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
            printwriter.println();
            printwriter.write("hello,nio");
            printwriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
