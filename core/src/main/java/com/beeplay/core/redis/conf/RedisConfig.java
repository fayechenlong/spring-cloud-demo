package com.beeplay.core.redis.conf;

import com.beeplay.core.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Autowired
    private RedisProperties redisProperties;
    @Bean(name="redisManager",initMethod = "init")
    public RedisManager redisManager(){
        RedisManager redisManager=new RedisManager();
        redisManager.setHost(redisProperties.getHost());
        redisManager.setPort(redisProperties.getPort());
        redisManager.setAuth(redisProperties.getAuth());
        redisManager.setDb(redisProperties.getDb());
        redisManager.setMaxActive(redisProperties.getMaxActive());
        redisManager.setMaxIdle(redisProperties.getMaxIdle());
        redisManager.setMaxWait(redisProperties.getMaxWait());
        redisManager.setTimeOut(redisProperties.getTimeOut());
        return redisManager;
    }

}
