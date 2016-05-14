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

package io.acme.solution.application.conf;

import com.google.gson.Gson;
import io.acme.solution.application.messaging.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Spring boot configuration class for registering the queues on Rabbit for command bus
 */
@Configuration
public class CommandMessagingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CommandMessagingConfigurer.class);

    @Value("${apache.kafka.consumer.properties}")
    private String kafkaConsumerProperties;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Gson gson;

    @Autowired
    private TaskExecutor consumerTaskExecutor;

    @Value("${kafka.command.topic}")
    private String topic;

    @Value("${command.handler.package.base}")
    private String commandHandlerPackageBase;

    @Value("${kafka.consumer.timeout}")
    private Long timeout;

    @PostConstruct
    private void setup() {

        try {

            final Map<String, CommandHandler> registry = CommandHandlerUtils.buildCommandHandlersRegistry(this.commandHandlerPackageBase, this.context);
            final File consumerPropertiesFile = this.context.getResource("classpath:" + this.kafkaConsumerProperties).getFile();

            CommandMessagingConsumer eventConsumer = new CommandMessagingConsumer(consumerPropertiesFile, this.topic, this.timeout, this.gson, registry);

            this.consumerTaskExecutor.execute(eventConsumer);
        } catch (IOException exception) {
            log.error("Couldn't load consumer properties file");
        }
    }


    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public TaskExecutor consumerTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
