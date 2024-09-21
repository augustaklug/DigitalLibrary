package com.klug.loansystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoansystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoansystemApplication.class, args);
    }

}
