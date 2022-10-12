package com.mobon.billing.dump.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.mobon.billing.dump"})
@EnableJpaRepositories("com.mobon.billing.dump.repository") 
@EntityScan("com.mobon.billing.dump.domainmodel")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}