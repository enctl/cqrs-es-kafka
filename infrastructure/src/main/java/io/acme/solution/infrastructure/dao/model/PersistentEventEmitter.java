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

import java.util.UUID;

/**
 * A persistent object for storing the domain entities aggregates
 */
@Document(collection = "emitters")
public class PersistentEventEmitter implements Comparable<PersistentEventEmitter> {

    @Id
    private UUID id;
    private Long version;
    private String emitterType;

    public PersistentEventEmitter() {

    }

    public PersistentEventEmitter(final UUID id, final Long version, final String emitterType) {
        this.id = id;
        this.version = version;
        this.emitterType = emitterType;
    }

    public UUID getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

    public String getEmitterType() {
        return emitterType;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PersistentEventEmitter) && ((PersistentEventEmitter) obj).getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(final PersistentEventEmitter eventEmitter) {
        int idCompare = this.id.compareTo(eventEmitter.getId());

        if (idCompare == 0) {
            return (int) (this.version - eventEmitter.version);
        } else {
            return idCompare;
        }
    }

    @Override
    public String toString() {
        return String.format("{%s: id: %s, version: %s, type: %s}", this.getClass().getSimpleName(),
                this.id, this.version, this.emitterType);
    }
}
