package com.dev.kamran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TranslationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TranslationServiceApplication.class, args);
    }
}
