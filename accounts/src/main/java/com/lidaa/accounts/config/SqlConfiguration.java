package com.lidaa.accounts.config;


import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SqlConfiguration {

    @Value("${spring.liquibase.change-log:}")
    private String liquibaseChangeLog;



    @Bean
    public SpringLiquibase liquibase(final DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDropFirst(false);
        springLiquibase.setDefaultSchema("public");
        springLiquibase.setChangeLog(liquibaseChangeLog);
        springLiquibase.setDataSource(dataSource);
        return springLiquibase;
    }

}
