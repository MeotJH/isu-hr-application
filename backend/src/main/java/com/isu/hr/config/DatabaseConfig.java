package com.isu.hr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Profile("local")
    @Primary
    public DataSource h2DataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:hrdb")
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    @Profile({"dev", "prod"})
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }
}
