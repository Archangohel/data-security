package com.dev.config;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Security database config.
 * @author Archan Gohel
 */

@Configuration
@EnableTransactionManagement
public class SecurityDataStore {

    @Bean(name = "securityDataSource")
    public DataSource getSecurityDataSource() {
        Properties props = new Properties();
        FileInputStream fis = null;
        MysqlDataSource mysqlDS = null;
        try {
            Resource resource = new ClassPathResource("global.properties");
            fis = new FileInputStream(resource.getFile());
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("spring.datasource.securitydb.url"));
            mysqlDS.setUser(props.getProperty("spring.datasource.securitydb.username"));
            mysqlDS.setPassword(props.getProperty("spring.datasource.securitydb.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }

    @Bean(name = "securityJdbcTemplate")
    public JdbcTemplate getSecurityJdbcTemplate() {
        return new JdbcTemplate(getSecurityDataSource());
    }
}
