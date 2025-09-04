package com.wx2.college;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = {
        "com.wx2.api.client"
})
@ComponentScan(basePackages = {"com.wx2"})
@SpringBootApplication
public class CollegeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollegeApplication.class, args);
    }
}
