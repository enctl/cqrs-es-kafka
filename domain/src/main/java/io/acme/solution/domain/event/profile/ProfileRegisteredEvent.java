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

package io.acme.solution.domain.event.profile;

import io.acme.solution.domain.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Domain event for user solution
 */
public class ProfileRegisteredEvent extends Event {

    public static final String MAPKEY_USERNAME = "username";
    public static final String MAPKEY_EMAIL = "email";
    public static final String MAPKEY_HASHEDPASS = "hashedPassword";

    private String username;
    private String email;
    private String hashedPassword;

    /**
     * @param aggregateId the identification of the domain aggregate/entity that generated that event
     * @param version     the version of the aggregated AFTER the event has been applied
     */
    public ProfileRegisteredEvent(final UUID aggregateId, final Long version, final String username, final String email,
                                  final String hashedPassword) {

        super(aggregateId, version);
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    @Override
    public Map<String, Object> getEntries() {
        final Map<String, Object> eventEntries = new HashMap<>();

        eventEntries.put(MAPKEY_USERNAME, this.username);
        eventEntries.put(MAPKEY_EMAIL, this.email);
        eventEntries.put(MAPKEY_HASHEDPASS, this.hashedPassword);

        return eventEntries;
    }
}
