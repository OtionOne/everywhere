package com.powernode.everywhere.auth.interceptor;


import com.powernode.everywhere.common.redis.utils.RedisCache;
import com.powernode.everywhere.config.WebConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Import(WebConfig.class)
@Configuration
public class EwAuthAutoConfiguration {
    @Bean
    public LoginInterceptor loginInterceptor(RedisCache redisCache){
        return new LoginInterceptor(redisCache);
    }
}
