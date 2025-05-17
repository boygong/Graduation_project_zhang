package com.article.Config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisSonConfig {

    //自定义的简单配置RedisSon
    @Bean
    public RedissonClient getRedisSon() throws Exception{
        Config config=new Config();
        config.useSingleServer().setAddress("redis://121.43.96.182:15112").setConnectionPoolSize(10).setPassword("123456")  // 连接池的最大连接数
                .setConnectionMinimumIdleSize(5)  // 连接池中的最小空闲连接数
                .setTimeout(10000);  // 设置超时
        return Redisson.create(config);
    }
}
