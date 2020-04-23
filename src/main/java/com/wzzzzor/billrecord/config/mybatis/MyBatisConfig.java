package com.wzzzzor.billrecord.config.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.wzzzzor.billrecord.intercepter.PageInterceptor;


@Configuration
public class MyBatisConfig implements TransactionManagementConfigurer{

    @Autowired
    private DataSource dataSource;

    @Value("${spring.mybatis.typeAliasesPackage}")
    private String typeAliasesPackage;

    @Value("${spring.mybatis.mapperLocations}")
    private String mapperLocations;

    @Value("${spring.mybatis.dialect}")
    private String dialect;

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //设置dataSource
        bean.setDataSource(dataSource);
        //设置typeAlias包扫描路径
        bean.setTypeAliasesPackage(typeAliasesPackage);
        Properties properties = new Properties();
        properties.setProperty("databaseType", dialect);
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(properties);
        Interceptor[] plugins = new Interceptor[] { pageInterceptor };
        bean.setPlugins(plugins);
        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources(mapperLocations));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Override
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
