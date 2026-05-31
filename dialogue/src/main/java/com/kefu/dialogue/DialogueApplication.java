package com.kefu.dialogue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.kefu.dialogue.mapper")
@EnableFeignClients
@SpringBootApplication
public class DialogueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DialogueApplication.class, args);
    }

}
