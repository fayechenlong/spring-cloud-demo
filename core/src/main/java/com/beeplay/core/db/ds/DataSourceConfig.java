package com.beeplay.core.db.ds;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {
    @Autowired
    private MysqlDataSourceProperties mysqlDataSourceProperties;
    @Value("${myibatis.mapper.path}")
    private String mapperPath;
    @Value("${myibatis.aliasesPackage}")
    private String aliasesPackage;

    public DruidDataSource dataSourceWrite() {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setDriverClassName(mysqlDataSourceProperties.getDriverClassName());
            dataSource.setUrl(mysqlDataSourceProperties.getUrl());
            dataSource.setUsername(mysqlDataSourceProperties.getUsername());
            dataSource.setPassword(mysqlDataSourceProperties.getPassword());
            dataSource.setValidationQuery("select 1");
            dataSource.setMaxActive(300);
            dataSource.setMaxWait(60000);
            dataSource.setFilters("stat");
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxOpenPreparedStatements(100);
            dataSource.setDefaultAutoCommit(true);
            dataSource.setMinEvictableIdleTimeMillis(30000);
            dataSource.setRemoveAbandoned(true);
            dataSource.setRemoveAbandonedTimeoutMillis(600);
            dataSource.setLogAbandoned(true);
            dataSource.setTestWhileIdle(true);
            dataSource.setUseUnfairLock(true);
           // dataSource.setConnectionInitSqls("set names utf8mb4;");
            dataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
    public DruidDataSource dataSourceRead() {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setDriverClassName(mysqlDataSourceProperties.getDriverClassNameRead());
            dataSource.setUrl(mysqlDataSourceProperties.getUrlRead());
            dataSource.setUsername(mysqlDataSourceProperties.getUsernameRead());
            dataSource.setPassword(mysqlDataSourceProperties.getPasswordRead());
            dataSource.setValidationQuery("select 1");
            dataSource.setMaxActive(300);
            dataSource.setMaxWait(60000);
            dataSource.setFilters("stat");
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxOpenPreparedStatements(100);
            dataSource.setDefaultAutoCommit(true);
            dataSource.setMinEvictableIdleTimeMillis(30000);
            dataSource.setRemoveAbandoned(true);
            dataSource.setRemoveAbandonedTimeoutMillis(600);
            dataSource.setLogAbandoned(true);
            dataSource.setTestWhileIdle(true);
            dataSource.setUseUnfairLock(true);
            // dataSource.setConnectionInitSqls("set names utf8mb4;");
            dataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
    @Bean(name="multipleDataSource")
    @Primary
    public MultipleDataSource multipleDataSource(){
        MultipleDataSource multipleDataSource=new MultipleDataSource();
        Map<Object,Object> mp=new HashMap<>();
        mp.put("dataSourceWrite",dataSourceWrite());
        mp.put("dataSourceRead",dataSourceRead());
        multipleDataSource.setDefaultTargetDataSource(dataSourceWrite());
        multipleDataSource.setTargetDataSources(mp);
        return  multipleDataSource;
    }
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(multipleDataSource());
        sessionFactory.setMapperLocations(applicationContext.getResources(mapperPath));
        sessionFactory.setTypeAliasesPackage(aliasesPackage);
        return sessionFactory.getObject();
    }
    /**
     * 配置事务管理器
     */
    @Bean
    @Order(2)
    public DataSourceTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(multipleDataSource());
    }
}
