package com.xietaojie.lab.springdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author xietaojie1992
 */
@Configuration
@ConfigurationProperties(prefix = "project")
@Data
public class ProjectProperties {
    private String name;
    private String version;
}
