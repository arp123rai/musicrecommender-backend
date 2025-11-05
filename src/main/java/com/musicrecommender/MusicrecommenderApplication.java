package com.musicrecommender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.musicrecommender.repository")
@EntityScan(basePackages = "com.musicrecommender.model")
public class MusicrecommenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicrecommenderApplication.class, args);
    }

}
