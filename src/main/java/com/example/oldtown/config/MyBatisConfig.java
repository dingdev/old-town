package com.example.oldtown.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * MyBatis配置类
 * Created by dyp on 2019/4/8.
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.example.oldtown.modules.*.mapper"})
public class MyBatisConfig {

    // @Value("${spring.datasource.url}")
    // private String URL;
    // @Value("${spring.datasource.type}")
    // private String TYPE;
    // @Value("${spring.datasource.driver-class-name}")
    // private String DRIVER;
    // @Value("${spring.datasource.username}")
    // private String USERNAME;
    // @Value("${spring.datasource.password}")
    // private String PASSWORD;
    //
    // // mysql数据源
    // @Bean
    // public DataSource dataSource(){
    //     return DataSourceBuilder.create().url(URL).driverClassName(DRIVER).username(USERNAME).password(PASSWORD).build();
    // }

    // 分页
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    // // 设置geometry的拦截器
    // @Bean
    // public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
    //     SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    //     bean.setDataSource(dataSource);
    //     bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml"));
    //
    //     MybatisConfiguration configuration = new MybatisConfiguration();
    //     configuration.addInterceptor(new MybatisGeometryHandler());
    //     bean.setConfiguration(configuration);
    //     return bean.getObject();
    // }

    // @Bean
    // public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
    //     return new DataSourceTransactionManager(dataSource);
    // }
    //
    // @Bean
    // public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
    //     return new SqlSessionTemplate(sqlSessionFactory);
    // }
}
