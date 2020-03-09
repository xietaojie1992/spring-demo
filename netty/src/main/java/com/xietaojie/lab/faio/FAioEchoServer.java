/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package com.xietaojie.lab.faio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xietaojie
 * @date 2020-02-05 12:38:40
 * @version $ Id: FAioEchoServer.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class FAioEchoServer implements AutoCloseable {

    private final    ServerSocket serverSocket;
    private volatile boolean      isClosed = false;

    private final ExecutorService executorService;

    public FAioEchoServer(Integer port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(), 120, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    public void start() {
        new Thread(new Acceptor()).start();
    }

    @Override
    public void close() throws Exception {
        this.isClosed = true;
        if (this.serverSocket != null && !this.serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    class Acceptor implements Runnable {

        @Override
        public void run() {
            while (!isClosed) {
                try {
                    // 阻塞等待 Client 发起连接
                    Socket socket = serverSocket.accept();

                    // 当监听到有 Client 发起连接，则生成一个 Task，放入线程池处理
                    executorService.execute(new FAioTimerServerTask(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class FAioTimerServerTask implements Runnable {

        private final Socket socket;

        public FAioTimerServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;

            try {
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);
                while (true) {
                    String body = in.readLine();
                    if (body == null) {
                        break;
                    }
                    log.info(body);
                    out.println(body);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }
    }
}
