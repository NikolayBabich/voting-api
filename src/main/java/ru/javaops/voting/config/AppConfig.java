package ru.javaops.voting.config;

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
import java.util.concurrent.TimeUnit;

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
        Hibernate5Module module = new Hibernate5Module();
        module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        return module;
    }

    // https://stackoverflow.com/a/54571018
    @Bean
    CaffeineCache usersCache() {
        return new CaffeineCache("users",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterAccess(10, TimeUnit.MINUTES)
                        .build());
    }

    @Bean
    CaffeineCache menusCache() {
        return new CaffeineCache("menus",
                Caffeine.newBuilder()
                        .maximumSize(20)
                        .expireAfterWrite(12, TimeUnit.HOURS)
                        .build());
    }
}