package com.xietaojie.lab.influxdb;

import org.influxdb.InfluxDB;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author xietaojie1992
 */
public class InfluxDbServiceImpl {

    @Value("${influxdb.host}")
    private String  host;
    @Value("${influxdb.port}")
    private Integer port;
    @Value("${influxdb.user}")
    private String  user;
    @Value("${influxdb.password}")
    private String  password;

    private InfluxDB influxDB;
}
