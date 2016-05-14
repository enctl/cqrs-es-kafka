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

package io.acme.solution.domain;

import io.acme.solution.domain.api.EventEmitter;
import io.acme.solution.domain.api.EventHandler;

import java.util.*;

/**
 * The abstract base for any event-based aggregate/entity in the domain model
 */
public abstract class BaseAggregate implements EventEmitter, Comparable<BaseAggregate> {

    protected static final String MEMKEY_VERSION = "version";

    private UUID id;
    private Long version;
    private Set<Event> deltaEvents;
    private Map<Class<? extends Event>, EventHandler> handlerRegistry;

    public BaseAggregate() {
        this.id = UUID.randomUUID();
        this.version = 0L;
        this.deltaEvents = new TreeSet<>();
        this.handlerRegistry = new HashMap<>();
    }

    public BaseAggregate(final UUID id) {
        this();
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public Set<Event> getChangeLog() {
        Set<Event> clonedEvents = new TreeSet<Event>();
        clonedEvents.addAll(this.deltaEvents);

        return clonedEvents;
    }

    @Override
    public <T extends Event> void apply(final T event) {
        final EventHandler<T> handler = this.handlerRegistry.get(event.getClass());

        if (handler != null) {
            handler.handle(event);
            this.setVersion(event.getVersion());
            this.deltaEvents.add(event);
        }
    }

    @Override
    public <T extends Event> void replay(List<T> eventsList) {
        Set<Event> upcomingEvents = new TreeSet<>();
        upcomingEvents.addAll(eventsList);

        for (Event current : upcomingEvents) {
            this.apply(current);
        }
    }

    @Override
    public void clear() {
        this.deltaEvents.clear();
    }

    @Override
    public int compareTo(final BaseAggregate aggregate) {
        int idCompare = this.id.compareTo(aggregate.getId());

        if (idCompare == 0) {
            return (int) (this.version - aggregate.version);
        } else {
            return idCompare;
        }
    }

    protected Long upgrade() {
        return ++this.version;
    }

    protected void setVersion(final Long version) {
        this.version = version;
    }

    protected <T extends Event> void registerEventHandler(Class<T> clazz, EventHandler<T> handler) {
        this.handlerRegistry.put(clazz, handler);
    }
}
