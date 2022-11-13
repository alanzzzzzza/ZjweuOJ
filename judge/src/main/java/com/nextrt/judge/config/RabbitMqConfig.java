package com.nextrt.judge.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.nextrt.judge.config.SystemConfig.*;

@Configuration
public class RabbitMqConfig {

    @Bean
    Queue taskQueue() {
        return new Queue(CJudgeTask, true);
    }
    @Bean
    Queue resultQueue() {
        return new Queue(CJudgeResult, true);
    }
    @Bean
    Queue heartQueue() {
        return new Queue(CJudgeHeart, true);
    }

    @Bean
    MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
