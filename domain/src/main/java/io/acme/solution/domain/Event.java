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

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * Domain events resulting from the interaction with the domain entities
 */
public abstract class Event implements Serializable, Comparable<Event> {


    private UUID id;
    private UUID aggregateId;
    private Long version;


    public Event(final UUID aggregateId, final Long version) {
        this.id = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.version = version;
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

    public abstract Map<String, Object> getEntries();

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Event) && ((Event) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(final Event event) {
        int idCompare = this.id.compareTo(event.getId());

        if (idCompare == 0) {
            return (int) (this.version - event.version);
        } else {
            return idCompare;
        }
    }
}
