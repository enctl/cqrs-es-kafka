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
 * A persistent object for storing the domain entities events snapshot to optimize retrieval
 */
@Document(collection = "snapshots")
public class PersistentSnapshot implements Comparable<PersistentSnapshot> {

    @Id
    private UUID id;
    private UUID aggregateId;
    private Long version;
    private Map<String, Object> state;

    public PersistentSnapshot() {

    }

    /**
     * @param aggregateId the id of the aggregate
     * @param version     the version of the currently stored state
     * @param state       the serialized state of the snapshot
     */
    public PersistentSnapshot(final UUID id, final UUID aggregateId, final Long version, final Map<String, Object> state) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.version = version;
        this.state = state;
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

    public Map<String, Object> getState() {
        return new HashMap<>(this.state);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PersistentSnapshot) && ((PersistentSnapshot) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(final PersistentSnapshot snapshot) {
        int idCompare = this.id.compareTo(snapshot.getId());

        if (idCompare == 0) {
            return (int) (this.version - snapshot.version);
        } else {
            return idCompare;
        }
    }

    @Override
    public String toString() {
        return String.format("{%s: id: %s, aggregateId: %s, version: %s, state: %s}", this.getClass().getSimpleName(),
                this.id, this.aggregateId, this.version, this.state);
    }
}
