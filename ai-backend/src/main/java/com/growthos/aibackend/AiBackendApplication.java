package com.growthos.aibackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.growthos.aibackend.mapper")
public class AiBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiBackendApplication.class, args);
    }
}