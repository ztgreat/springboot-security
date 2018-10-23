package com.springboot.security.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 这里需要注意:
 * 暂时将session的有效 时间配置在这里
 * 配置在文件中没有生效,暂时没有找到原因,不知道是不是有地方配置错了
 * 在 spring boot 中
 * org.springframework.boot.autoconfigure.session.RedisSessionConfiguration->customize
 * 中有配置 maxInactiveIntervalInSeconds,但是发现没有被调用执行
 * 
 * @// FIXME: 2018/10/22
 */

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 28800)
public class HttpSessionConfig {

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

}
