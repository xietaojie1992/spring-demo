/*
 * Fabric4cloud.com Inc.
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package com.xietaojie.lab.springdemo.service.impl;

import org.influxdb.InfluxDB;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author taojie
 * @version $Id: InfluxDbServiceImpl.java, v 0.1 2018-08-14 15:31:40 taojie Exp $
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
