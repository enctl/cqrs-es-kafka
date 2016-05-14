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

package io.acme.solution.query.handler;

import io.acme.solution.query.dao.ProfileCredentialsDao;
import io.acme.solution.query.dao.ProfileDao;
import io.acme.solution.query.messaging.EventHandler;
import io.acme.solution.query.model.ProfileCredentials;
import io.acme.solution.query.model.QueryableProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

/**
 * Profile registered event handler
 */
public class ProfileRegisteredEventHandler implements EventHandler {

    private static final String INTEREST = "ProfileRegisteredEvent";

    private static final String MAPKEY_USERNAME = "username";
    private static final String MAPKEY_EMAIL = "email";
    private static final String MAPKEY_HASHEDPASS = "hashedPassword";

    private static final Logger log = LoggerFactory.getLogger(ProfileRegisteredEventHandler.class);

    @Autowired
    private ProfileDao profileDao;

    @Autowired
    private ProfileCredentialsDao profileCredentialsDao;

    @Override
    public void handleMessage(final Map<String, Object> eventEntries) {

        if (eventEntries.containsKey(MEMKEY_AGGREGATE_ID)) {
            final UUID id = UUID.fromString(eventEntries.get(MEMKEY_AGGREGATE_ID).toString());
            final Long version = eventEntries.get(MEMKEY_VERSION) != null ?
                    Double.valueOf(eventEntries.get(MEMKEY_VERSION).toString()).longValue() : null;
            final String username = eventEntries.get(MAPKEY_USERNAME) != null ? eventEntries.get(MAPKEY_USERNAME).toString() : null;
            final String email = eventEntries.get(MAPKEY_EMAIL) != null ? eventEntries.get(MAPKEY_EMAIL).toString() : null;
            final String hashedPassword = eventEntries.get(MAPKEY_HASHEDPASS) != null ? eventEntries.get(MAPKEY_HASHEDPASS).toString() : null;


            if (id != null && username != null && version != null && email != null && hashedPassword != null) {
                final QueryableProfile profile = new QueryableProfile(id, version, username, email);
                final ProfileCredentials credentials = new ProfileCredentials(id, hashedPassword);

                this.profileDao.save(profile);
                this.profileCredentialsDao.save(credentials);

                log.info("New profile registered");
            } else {
                log.trace("Profile discarded due to missing mandatory attributes in the event entries");
            }
        } else {
            log.trace("Profile discarded due to missing aggregateId in the event entries");
        }
    }

    @Override
    public String getInterest() {
        return INTEREST;
    }
}
