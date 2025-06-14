package com.interview.intercorp.ms05.ms05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Ms05Application {

    public static void main(String[] args) {
        SpringApplication.run(Ms05Application.class, args);
    }

}
