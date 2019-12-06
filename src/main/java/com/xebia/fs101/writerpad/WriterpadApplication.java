package com.xebia.fs101.writerpad;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;

@SpringBootApplication
@EnableAsync
public class WriterpadApplication {
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Value("${spring.datasource.username}")
    private String dbUser;

    @Bean
    public DataSource dataSource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        return new HikariDataSource(config);
    }

    public static void main(String[] args) {

        SpringApplication.run(WriterpadApplication.class, args);
    }
}
