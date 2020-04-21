package com.xietaojie.lab;

import com.xietaojie.lab.nio.NioTimerClient;
import com.xietaojie.lab.nio.NioTimerServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author xietaojie
 * @date 2020-02-05 12:48:49
 * @version $ Id: BioTest.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class NioTest {

    public static void main(String[] args) {
        String host = "localhost";
        Integer port = 8888;

        try {

            NioTimerServer server = new NioTimerServer(host, port);
            server.start();

            NioTimerClient client = new NioTimerClient(host, port);
            //client.start();

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
