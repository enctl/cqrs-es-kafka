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

package io.acme.solution.infrastructure.repo;

import io.acme.solution.domain.Event;
import io.acme.solution.domain.Profile;
import io.acme.solution.domain.api.repo.ProfileRepository;
import io.acme.solution.infrastructure.broker.EventPublisher;
import io.acme.solution.infrastructure.dao.EventDao;
import io.acme.solution.infrastructure.dao.EventEmitterDao;
import io.acme.solution.infrastructure.dao.SnapshotDao;
import io.acme.solution.infrastructure.dao.model.PersistentEvent;
import io.acme.solution.infrastructure.dao.model.PersistentEventEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * NoSQL implementation for profile repository
 */
@Component
public class ProfileRepositoryImpl implements ProfileRepository {

    private static final Logger log = LoggerFactory.getLogger(ProfileRepositoryImpl.class);

    @Autowired
    private EventEmitterDao eventEmitterDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private SnapshotDao snapshotDao;

    @Autowired
    private EventPublisher eventPublisher;

    @Value("${persistence.db.snapshot.bucket}")
    private Integer snapshotBucket;

    @Override
    public void save(final Profile profile) {

        log.info("Saving the profile");

        PersistentEventEmitter persistentEventEmitter = this.eventEmitterDao.getById(profile.getId());
        Set<PersistentEvent> persistentEventSet = new HashSet<>();
        Set<Event> eventSet = null;

        if (persistentEventEmitter == null) {
            persistentEventEmitter = new PersistentEventEmitter(profile.getId(), profile.getVersion(),
                    profile.getClass().getSimpleName());
        } else {
            persistentEventEmitter.setVersion(profile.getVersion());
        }

        eventSet = profile.getChangeLog();

        persistentEventSet.addAll(eventSet.stream().map(currentEvent -> new PersistentEvent(currentEvent.getId(),
                currentEvent.getAggregateId(), currentEvent.getVersion(), currentEvent.getClass().getSimpleName(),
                currentEvent.getEntries())).collect(Collectors.toList()));

        this.eventEmitterDao.save(persistentEventEmitter);
        this.eventDao.save(persistentEventSet);
        this.eventPublisher.publish(persistentEventSet);

        profile.clear();

        //TODO Add Snapshot Logic
    }
}
