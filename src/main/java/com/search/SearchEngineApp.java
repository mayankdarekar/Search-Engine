package com.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SearchEngineApp {
    public static void main(String[] args) {
        SpringApplication.run(SearchEngineApp.class, args);
        System.out.println("\n🔍 Modern Search Engine Running at: http://localhost:8080\n");
    }
}
