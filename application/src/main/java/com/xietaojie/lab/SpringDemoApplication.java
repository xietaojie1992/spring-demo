package com.xietaojie.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringDemoApplication {

    /**
     * 程序启动入口.
     * 启动方式 1. 直接执行该方法
     *         2. mvn spring-boot:run
     *         3. java -jar ***.jar --spring.profiles.active=prod
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }
}
