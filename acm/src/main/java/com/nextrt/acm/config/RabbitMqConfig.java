package com.nextrt.acm.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    Queue taskQueue() {
        return new Queue(SystemConfig.CJudgeTask, true);
    }

    @Bean
    Queue resultQueue() {
        return new Queue(SystemConfig.CJudgeResult, true);
    }
    @Bean
    Queue heartQueue() {
        return new Queue(SystemConfig.CJudgeHeart, true);
    }
    @Bean
    MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
