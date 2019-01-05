package com.hlyp.api.dao.mybatis;


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;
import java.util.Properties;

/**
 * Created by Lenovo on 2018/12/25.
 */
@Configuration
public class MyBatisMapperScannerConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.hlyp.api.dao.mybatis");    //初始化扫描器的相关配置，这里我们要创建一个Mapper的父类
        Properties properties = new Properties();
        properties.setProperty("mappers", "com.hlyp.api.dao.baseDao.MyMapper");
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");

        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
}
