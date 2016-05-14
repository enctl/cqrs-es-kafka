package io.acme.solution.query.conf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.acme.solution.query.messaging.EventHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka consumer class that run in async mode
 */
public class EventMessagingConsumer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(EventMessagingConfigurer.class);

    private Long timeout;
    private Gson gson;
    private Map<String, EventHandler> handlerRegistry;
    private File consumerProperties;
    private String topic;


    public EventMessagingConsumer(final File consumerProperties, final String topic, final Long timeout, final Gson gson,
                                  final Map<String, EventHandler> handlerRegistry) {

        this.timeout = timeout;
        this.consumerProperties = consumerProperties;
        this.gson = gson;
        this.handlerRegistry = handlerRegistry;
        this.topic = topic;
    }


    @Override
    public void run() {
        ConsumerRecords<String, String> events;
        EventHandler handler;
        Long pollTimeout = this.timeout;
        KafkaConsumer<String, String> consumer = getKafkaEventConsumer();

        log.info("Initializing event handler registry. Found the following event handlers: " + this.printRegistry(this.handlerRegistry));

        while (true) {
            events = consumer.poll(this.timeout);
            if (events != null && events.count() == 0) {
                pollTimeout++;
            } else {
                log.info("Polled {} domain events for query part update after {} timeout", events.count(), pollTimeout);
                pollTimeout = this.timeout;

                for (ConsumerRecord<String, String> event : events) {
                    handler = this.handlerRegistry.get(event.key());
                    if (handler != null) {
                        handler.handleMessage(gson.fromJson(event.value(), new TypeToken<Map<String, Object>>() {
                        }.getType()));
                    } else {
                        log.warn("Couldn't find an event handler for event type: {" + event.key() + "}");
                    }
                }
            }
        }
    }

    private KafkaConsumer<String, String> getKafkaEventConsumer() {
        try {
            KafkaConsumer<String, String> consumer = null;
            Properties properties = new Properties();
            properties.load(new FileReader(this.consumerProperties));

            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Arrays.asList(this.topic));

            return consumer;
        } catch (IOException exception) {
            log.error("Error loading Kafka consumer properties", exception);
        }

        return null;
    }

    private String printRegistry(final Map<String, EventHandler> registry) {
        final String separator = System.getProperty("line.separator");
        final StringBuilder builder = new StringBuilder();

        registry.forEach((k, v) -> {
            builder.append(k + " -> " + v.getClass().getName());
            builder.append(separator);
        });

        return builder.toString();
    }
}
