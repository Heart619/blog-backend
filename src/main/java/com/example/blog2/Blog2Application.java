package com.example.blog2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * @author mxp
 */

@EnableTransactionManagement
@SpringBootApplication
public class Blog2Application {
    public static void main(String[] args) {
        SpringApplication.run(Blog2Application.class, args);
    }
}
