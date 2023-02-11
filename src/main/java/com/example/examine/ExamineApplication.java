package com.example.examine;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("com.example.examine.dao")
public class ExamineApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamineApplication.class, args);
    }
}

