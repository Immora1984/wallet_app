package ru.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class WalletTzApplication {

    static void main(String[] args) {
        SpringApplication.run(WalletTzApplication.class, args);
    }

}