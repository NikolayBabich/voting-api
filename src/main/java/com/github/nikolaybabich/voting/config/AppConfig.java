package com.github.nikolaybabich.voting.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;
import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class AppConfig {

    @Profile("!test")
    @Bean(initMethod = "start", destroyMethod = "stop")
    Server h2Server() throws SQLException {
        log.info("Start H2 TCP server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    // https://stackoverflow.com/a/46947975
    @Bean
    Module module() {
        return new Hibernate5Module();
    }

    // https://stackoverflow.com/a/54571018
    @Bean
    CaffeineCache usersCache() {
        return new CaffeineCache("users",
                Caffeine.newBuilder()
                        .maximumSize(5000)
                        .expireAfterAccess(Duration.ofSeconds(60))
                        .build());
    }

    @Bean
    CaffeineCache menusCache() {
        return new CaffeineCache("menus",
                Caffeine.newBuilder()
                        .maximumSize(100)
                        .expireAfterWrite(Duration.ofHours(6))
                        .build());
    }
}
