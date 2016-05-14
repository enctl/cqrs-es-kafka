package io.acme.solution.infrastructure.conf;

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
 * Spring boot configuration class for registering the queues on Rabbit for event bus
 */
@Configuration
public class EventMessagingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(EventMessagingConfigurer.class);

    @Autowired
    private ApplicationContext context;

    @Value("${apache.kafka.producer.properties}")
    private String kafkaProducerProperties;

    @Bean
    public KafkaProducer<String, String> persistentEventProducer() {

        try {
            Properties properties = new Properties();
            properties.load(this.context.getResource("classpath:" + this.kafkaProducerProperties).getInputStream());

            return new KafkaProducer<String, String>(properties);
        } catch (IOException exception) {
            log.error("Error loading Kafka producer properties", exception);
        }

        return null;
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
