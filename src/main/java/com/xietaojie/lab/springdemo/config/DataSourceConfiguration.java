
package com.xietaojie.lab.springdemo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * 使用 mybatis-spring-boot-autoconfigure，不需要自己创建DataSource，SqlSessionFactory
 *
 * SpringBoot集成Mybatis的入口：创建数据源，创建SqlSessionFactory
 * @author xietaojie1992
 */
@Configuration
@MapperScan(basePackages = "com.xietaojie.lab.springdemo.common.dal.mapper")
public class DataSourceConfiguration {

    //@Bean
    //@Primary
    //@ConfigurationProperties(prefix = "spring.datasource")
    //public DataSource primaryDataSource() {
    //    return DataSourceBuilder.create().build();
    //}

    //@Bean
    //public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {
    //    System.err.println("sqlSessionFactory");
    //    System.err.println(dataSource.getConnection().isValid(1));
    //    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    //    sessionFactory.setDataSource(dataSource);
    //    //sessionFactory.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
    //    //sessionFactory.setMapperLocations(applicationContext.getResources("classpath:mapper/*Mapper.xml"));
    //    return sessionFactory.getObject();
    //}
    //

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
