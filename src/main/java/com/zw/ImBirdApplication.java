package com.zw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@MapperScan(value = "com.zw.mapper")
@ComponentScan(basePackages = {"com.zw","com.n3r.idworker"})
@SpringBootApplication
public class ImBirdApplication extends SpringBootServletInitializer {
    @Bean
    public SpringUtil getSpringUtil(){
        return  new SpringUtil();
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ImBirdApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(ImBirdApplication.class, args);
    }

}
