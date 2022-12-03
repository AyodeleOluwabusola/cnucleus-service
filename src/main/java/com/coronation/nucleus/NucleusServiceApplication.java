package com.coronation.nucleus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootApplication
@RefreshScope
public class NucleusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NucleusServiceApplication.class, args);
    }

}
