package com.example.redcross;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RedCrossWebsiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedCrossWebsiteApplication.class, args);
    }
}