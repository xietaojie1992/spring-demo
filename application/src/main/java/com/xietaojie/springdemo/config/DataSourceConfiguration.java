package com.xietaojie.springdemo.config;

import com.xietaojie.springdemo.dao.tools.MybatisExecutorInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * 使用 mybatis-spring-boot-autoconfigure，不需要自己创建DataSource，SqlSessionFactory
 *
 * SpringBoot集成Mybatis的入口：创建数据源，创建SqlSessionFactory
 *
 * @author xietaojie1992
 */
@Configuration
@MapperScan(basePackages = "com.xietaojie.springdemo.dao.mapper")
// MapperScan 应该使用 tk.mybatis.spring.annotation.MapperScan;
public class DataSourceConfiguration {

    @Bean
    public MybatisExecutorInterceptor mybatisExecutorInterceptor() {
        // mybatis would auto add it to interceptors
        return new MybatisExecutorInterceptor();
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public TransactionTemplate transactionTemplate(DataSourceTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

}
