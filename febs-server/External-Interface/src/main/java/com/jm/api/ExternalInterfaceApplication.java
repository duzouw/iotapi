package com.jm.api;

import cc.mrbird.febs.common.security.starter.annotation.EnableFebsCloudResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableFebsCloudResourceServer
@SpringBootApplication
@MapperScan("com.jm.api.*.mapper")
public class ExternalInterfaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExternalInterfaceApplication.class, args);
    }
}
