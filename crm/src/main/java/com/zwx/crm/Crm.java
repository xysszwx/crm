package com.zwx.crm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zwx.crm.dao")
public class Crm {
    public static void main(String[] args) {
        SpringApplication.run(Crm.class, args);
    }
}
