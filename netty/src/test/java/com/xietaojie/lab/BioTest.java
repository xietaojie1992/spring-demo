/*
 * Zenlayer.com Inc.
 * Copyright (c) 2014-2020 All Rights Reserved.
 */
package com.xietaojie.lab;

import com.xietaojie.lab.bio.BioEchoClient;
import com.xietaojie.lab.bio.BioEchoServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author xietaojie
 * @date 2020-02-05 12:48:49
 * @version $ Id: BioTest.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class BioTest {

    public static void main(String[] args) {
        String host = "localhost";
        Integer port = 8888;

        try {

            BioEchoServer server = new BioEchoServer(port);
            server.start();

            BioEchoClient client = new BioEchoClient(host, port);
            client.start();

            try {
                server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
