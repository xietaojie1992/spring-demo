package com.xietaojie.lab;

import com.xietaojie.lab.faio.FAioEchoClient;
import com.xietaojie.lab.faio.FAioEchoServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author xietaojie
 * @date 2020-02-05 12:48:49
 * @version $ Id: BioTest.java, v 0.1  xietaojie Exp $
 */
@Slf4j
public class FAioTest {

    public static void main(String[] args) {
        String host = "localhost";
        Integer port = 8888;

        try {

            FAioEchoServer server = new FAioEchoServer(port);
            server.start();

            FAioEchoClient client = new FAioEchoClient(host, port);
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
