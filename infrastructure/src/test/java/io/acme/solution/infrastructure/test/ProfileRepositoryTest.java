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

package io.acme.solution.infrastructure.test;

import io.acme.solution.domain.Event;
import io.acme.solution.domain.Profile;
import io.acme.solution.domain.api.repo.ProfileRepository;
import io.acme.solution.infrastructure.InfrastructureContext;
import io.acme.solution.infrastructure.dao.EventDao;
import io.acme.solution.infrastructure.dao.EventEmitterDao;
import io.acme.solution.infrastructure.dao.model.PersistentEvent;
import io.acme.solution.infrastructure.dao.model.PersistentEventEmitter;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;
import java.util.UUID;

/**
 * Unit test application for the profile repository
 */
@ActiveProfiles("default,test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InfrastructureContext.class)
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private EventEmitterDao eventEmitterDao;

    @Autowired
    private EventDao eventDao;

    @Before
    public void setup() {
        Assert.assertThat(this.profileRepository, Matchers.notNullValue());
    }

    @Test
    public void testSaveProfile() {
        final Profile profile = new Profile(UUID.randomUUID(), "username", "email@domain.com", "password");
        final Set<Event> changeSet = profile.getChangeLog();
        PersistentEvent currentPersistentEvent = null;

        this.profileRepository.save(profile);

        PersistentEventEmitter persistentEventEmitter = this.eventEmitterDao.getById(profile.getId());

        Assert.assertThat(persistentEventEmitter, Matchers.notNullValue());
        Assert.assertThat(persistentEventEmitter.getVersion(), Matchers.equalTo(profile.getVersion()));

        for (Event event : changeSet) {
            currentPersistentEvent = this.eventDao.getById(event.getId());

            Assert.assertThat(currentPersistentEvent, Matchers.notNullValue());
            Assert.assertThat(currentPersistentEvent.getVersion(), Matchers.equalTo(event.getVersion()));
        }
    }
}
