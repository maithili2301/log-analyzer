package com.maithili.loganalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LogAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogAnalyzerApplication.class, args);
    }
}