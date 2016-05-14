package io.acme.solution.query.conf;

import com.google.gson.Gson;
import io.acme.solution.query.messaging.EventHandler;
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
 * Apache Kafka Messaging Configurer
 */
@Configuration
public class EventMessagingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(EventMessagingConfigurer.class);

    @Value("${apache.kafka.consumer.properties}")
    private String kafkaConsumerProperties;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Gson gson;

    @Autowired
    private TaskExecutor consumerTaskExecutor;

    @Value("${kafka.event.topic}")
    private String topic;

    @Value("${event.handler.package.base}")
    private String eventHandlerPackageBase;

    @Value("${kafka.consumer.timeout}")
    private Long timeout;

    @PostConstruct
    private void setup() {

        try {

            final Map<String, EventHandler> registry = EventHandlerUtils.buildEventHandlersRegistry(this.eventHandlerPackageBase, this.context);
            final File consumerPropertiesFile = this.context.getResource("classpath:" + this.kafkaConsumerProperties).getFile();

            EventMessagingConsumer eventConsumer = new EventMessagingConsumer(consumerPropertiesFile, this.topic, this.timeout, this.gson, registry);

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
