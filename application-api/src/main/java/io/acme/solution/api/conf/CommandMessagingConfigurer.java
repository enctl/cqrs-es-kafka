/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Eslam Nawara
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.acme.solution.api.conf;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * Configurer class to bootstrap the needed artifacts for RabbitMQ
 */
@Configuration
public class CommandMessagingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CommandMessagingConfigurer.class);

    @Autowired
    private ApplicationContext context;

    @Value("${apache.kafka.producer.properties}")
    private String kafkaProducerProperties;

    @Bean
    public KafkaProducer<String, String> commandProducer() {

        try {
            Properties properties = new Properties();
            properties.load(this.context.getResource("classpath:" + this.kafkaProducerProperties).getInputStream());

            return new KafkaProducer<String, String>(properties);
        } catch (final IOException exception) {
            log.error("Error loading Kafka producer properties", exception);
        }

        return null;
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

}
