package com.capit.capitschedule;

import com.capit.exceptions.handling.RestExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.capit.capitschedule", "com.capit.exceptions.handling"})
public class CapitScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(CapitScheduleApplication.class, args);
    }
}
