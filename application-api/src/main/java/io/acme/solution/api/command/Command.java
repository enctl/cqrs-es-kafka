package io.acme.solution.api.command;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * A base class for handling the created commands from the application layer
 */
public abstract class Command implements Serializable {

    protected UUID id;

    public Command() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Command) && ((Command) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }


    public abstract Map<String, Object> getEntries();
}
