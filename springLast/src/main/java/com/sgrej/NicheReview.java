package com.sgrej;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.sgrej.mapper")
@EnableScheduling
public class NicheReview {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(NicheReview.class,args);
    }
}
  