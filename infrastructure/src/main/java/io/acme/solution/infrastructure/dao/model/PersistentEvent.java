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
package io.acme.solution.infrastructure.dao.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A persistent object for storing the domain entities generated events
 */
@Document(collection = "events")
public class PersistentEvent implements Comparable<PersistentEvent> {

    private static final String MEMKEY_VERSION = "version";
    private static final String MEMKEY_EVENTTYPE = "type";
    private static final String MEMKEY_ID = "id";
    private static final String MEMKEY_AGGREGATE_ID = "aggregateId";


    @Id
    private UUID id;
    private UUID aggregateId;
    private Long version;
    private String eventType;
    private Map<String, Object> entries;


    public PersistentEvent() {

    }

    public PersistentEvent(final UUID id, final UUID aggregateId, final Long version, final String eventType, final Map<String, Object> entries) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.version = version;
        this.eventType = eventType;
        this.entries = new HashMap<>(entries);
    }

    public UUID getId() {
        return id;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public Long getVersion() {
        return version;
    }

    public String getEventType() {
        return eventType;
    }

    public Map<String, Object> getEntries() {

        Map<String, Object> clone = new HashMap<>(this.entries);
        clone.put(MEMKEY_ID, this.id);
        clone.put(MEMKEY_VERSION, this.version);
        clone.put(MEMKEY_AGGREGATE_ID, this.aggregateId);
        clone.put(MEMKEY_EVENTTYPE, this.eventType);

        return clone;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PersistentEvent) && ((PersistentEvent) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(final PersistentEvent event) {
        int idCompare = this.id.compareTo(event.getId());

        if (idCompare == 0) {
            return (int) (this.version - event.version);
        } else {
            return idCompare;
        }
    }

    @Override
    public String toString() {
        return String.format("{%s: id: %s, aggregateId: %s, version: %s, entries: %s}", this.getClass().getSimpleName(),
                this.id, this.version, this.entries);
    }
}
