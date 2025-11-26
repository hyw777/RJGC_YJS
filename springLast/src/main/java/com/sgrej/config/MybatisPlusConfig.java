package com.sgrej.config;

import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//Spring boot方式
@EnableTransactionManagement // 开启事务
@Configuration
@MapperScan("com.sgrej.mapper")
public class MybatisPlusConfig {

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }
}
