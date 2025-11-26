package com.sgrej.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
 
/**
 * MultipartFile配置
 */
@Configuration
public class MultipartFileConfig {
 
    @Bean
    public MultipartConfigElement getMultipartConfig() {
        MultipartConfigFactory config = new MultipartConfigFactory();
        config.setMaxRequestSize(DataSize.parse("10MB"));//单次上传的单个文件大小上限(默认1MB)
        config.setMaxRequestSize(DataSize.parse("100MB"));//单次上传的多个文件总大小上限
        //上面两个最好配置一下，下面两个看个人喜好
//        config.setFileSizeThreshold(DataSize.parse("10MB"));//临时目录中文件大小大于指定值则将数据写入硬盘
//        config.setLocation("/");//保存文件临时目录
        return config.createMultipartConfig();
    }
 
}