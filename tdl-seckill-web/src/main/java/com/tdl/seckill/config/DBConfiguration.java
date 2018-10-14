package com.tdl.seckill.config;

import com.alibaba.druid.pool.DruidDataSource;

import com.tdl.seckill.config.druid.AbstractDruidDBConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;

/**
 * 描述: 核心配置，配置数据源 事物 sqlSessionFactory
 */
@Configuration
@EnableTransactionManagement
public class DBConfiguration extends AbstractDruidDBConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    // 注册 datasourceOne
    @Bean(name = "druidDataSource", initMethod = "init", destroyMethod = "close")
    public DruidDataSource dataSource() {
        return super.createDataSource(url, username, password);
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return super.sqlSessionFactory(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }
}
