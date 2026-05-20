package com.kefu.administrator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.kefu.administrator.mapper")
@SpringBootApplication
public class AdministratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdministratorApplication.class, args);
    }

}
