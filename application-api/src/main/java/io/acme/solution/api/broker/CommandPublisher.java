package io.acme.solution.api.broker;

import com.google.gson.Gson;
import io.acme.solution.api.command.Command;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Commands publisher for the interested parties
 */
@Component
public class CommandPublisher {

    private static final Logger log = LoggerFactory.getLogger(CommandPublisher.class);

    @Value("${kafka.command.topic}")
    private String topic;

    @Autowired
    private Gson gson;

    @Autowired
    private KafkaProducer<String, String> commandProducer;


    public void publish(final Command command) {
        this.commandProducer.send(new ProducerRecord<String, String>(
                this.topic, command.getClass().getSimpleName(), this.gson.toJson(command.getEntries())));
    }

    public void publish(final Set<Command> commandSet) {

        for (Command currentCommand : commandSet) {
            this.publish(currentCommand);
        }
    }
}
