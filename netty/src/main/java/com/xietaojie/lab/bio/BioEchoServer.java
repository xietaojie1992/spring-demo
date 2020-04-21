package com.xietaojie.lab.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞 IO，
 *
 * @author xietaojie
 * @date 2020-02-05 12:38:40
 * @version $ Id: FAioEchoServer.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class BioEchoServer implements AutoCloseable {

    private          String       host     = "localhost";
    private          Integer      port     = 8080;
    private final    ServerSocket serverSocket;
    private volatile boolean      isClosed = false;

    public BioEchoServer() throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public BioEchoServer(Integer port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
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

                    // 当监听到有 Client 发起连接，则新建一个线程来处理
                    new Thread(new BioTimerServerHandler(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class BioTimerServerHandler implements Runnable {

        private final Socket socket;

        public BioTimerServerHandler(Socket socket) {
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
