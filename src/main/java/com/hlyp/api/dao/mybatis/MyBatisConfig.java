package com.hlyp.api.dao.mybatis;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.Properties;

/**
 * Created by Lenovo on 2018/12/25.
 */
public class MyBatisConfig {
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(){
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setTypeAliasesPackage("com.hlyp.api.bean");

        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable","true");
        properties.setProperty("supportMethodsArguments","true");
        properties.setProperty("returnPageInfo","check");
        pageHelper.setProperties(properties);

        bean.setPlugins(new Interceptor[] {pageHelper});
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try{
            return bean.getObject();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
