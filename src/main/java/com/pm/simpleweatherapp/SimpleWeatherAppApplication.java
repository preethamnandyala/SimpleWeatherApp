package com.pm.simpleweatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class SimpleWeatherAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleWeatherAppApplication.class, args);
    }

}
