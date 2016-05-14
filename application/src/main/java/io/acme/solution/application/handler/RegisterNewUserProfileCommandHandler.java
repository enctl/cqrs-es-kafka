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

package io.acme.solution.application.handler;

import io.acme.solution.application.messaging.CommandHandler;
import io.acme.solution.domain.Profile;
import io.acme.solution.domain.api.repo.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

/**
 * Command handler for the new user registration command
 */
public class RegisterNewUserProfileCommandHandler implements CommandHandler {

    private static final String INTEREST = "RegisterNewUserProfileCommand";

    public static final String MAPKEY_ID = "id";
    public static final String MAPKEY_USERNAME = "username";
    public static final String MAPKEY_EMAIL = "email";
    public static final String MAPKEY_PASSWORD = "password";

    private static final Logger log = LoggerFactory.getLogger(RegisterNewUserProfileCommandHandler.class);

    @Autowired
    private ProfileRepository profileRepository;


    @Override
    public void handleMessage(final Map<String, Object> commandEntries) {

        if (commandEntries.containsKey(MAPKEY_ID)) {
            final UUID id = UUID.fromString(commandEntries.get(MAPKEY_ID).toString());
            final String username = commandEntries.get(MAPKEY_USERNAME) != null ? commandEntries.get(MAPKEY_USERNAME).toString() : null;
            final String email = commandEntries.get(MAPKEY_EMAIL) != null ? commandEntries.get(MAPKEY_EMAIL).toString() : null;
            final String password = commandEntries.get(MAPKEY_PASSWORD) != null ? commandEntries.get(MAPKEY_PASSWORD).toString() : null;


            if (username != null && password != null && email != null) {
                final Profile userProfile = new Profile(id, username,
                        email, password);

                this.profileRepository.save(userProfile);
            } else {
                log.error("RegisterNewUserProfileCommandHandler received an invalid command type");
            }
        }
    }

    @Override
    public String getInterest() {
        return INTEREST;
    }

}
