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
import java.net.Socket;

/**
 * @author xietaojie
 * @date 2020-02-05 18:17:14
 * @version $ Id: FAioEchoClient.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class FAioEchoClient implements AutoCloseable {

    private final Socket socket;

    public FAioEchoClient(String host, Integer port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public void start() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);

            out.println("hello");
            log.info(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

    @Override
    public void close() throws Exception {

    }
}
