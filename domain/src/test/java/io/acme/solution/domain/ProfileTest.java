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

import io.acme.solution.domain.event.profile.ProfileRegisteredEvent;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;

/**
 * Unit testing for the domain entity named profile
 */
public class ProfileTest {

    private static final Logger log = LoggerFactory.getLogger(ProfileTest.class);

    private static final String DATA_USERNAME = "testname";
    private static final String DATA_EMAIL = "test.mail@domain.com";
    private static final String DATA_PASSWORD = "passw0rd";

    @Test
    public void testProfileRegistration() {
        final UUID id = UUID.randomUUID();
        final Profile profile = new Profile(id, DATA_USERNAME, DATA_EMAIL, DATA_PASSWORD);
        final Set<Event> events = profile.getChangeLog();

        Assert.assertNotNull(profile);
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(1L, profile.getVersion().longValue());

        final Event registrationEvent = events.iterator().next();

        Assert.assertNotNull(registrationEvent);
        Assert.assertEquals(1L, registrationEvent.getVersion().longValue());
        Assert.assertEquals(id, profile.getId());
        Assert.assertEquals(profile.getId(), registrationEvent.getAggregateId());
        Assert.assertEquals(ProfileRegisteredEvent.class, registrationEvent.getClass());
        Assert.assertEquals(DATA_USERNAME, ((ProfileRegisteredEvent) registrationEvent).getUsername());
        Assert.assertEquals(DATA_EMAIL, ((ProfileRegisteredEvent) registrationEvent).getEmail());
        Assert.assertNotEquals(DATA_PASSWORD, ((ProfileRegisteredEvent) registrationEvent).getHashedPassword());
    }

    @Test
    public void testMemento() {
        final UUID id = UUID.randomUUID();
        final Profile profile = new Profile(id, DATA_USERNAME, DATA_EMAIL, DATA_PASSWORD);
        AggregateMemento memento = profile.saveToMemento();

        Assert.assertNotNull(memento);
        Assert.assertEquals(1L, memento.getVersion().longValue());
        Assert.assertNotNull(memento.getState());

        log.info("Generated memento for profile v1.0 " + memento);

        profile.restoreFromMemento(memento);

        Assert.assertEquals(1L, profile.getVersion().longValue());
        Assert.assertEquals(id, profile.getId());
        Assert.assertEquals(DATA_USERNAME, profile.getUsername());
        Assert.assertEquals(DATA_EMAIL, profile.getEmail());
        Assert.assertNotEquals(DATA_PASSWORD, profile.getHashedPassword());
    }
}
