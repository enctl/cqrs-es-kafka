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

package io.acme.solution.api.app;

import io.acme.solution.api.broker.CommandPublisher;
import io.acme.solution.api.bundle.RESTKeys;
import io.acme.solution.api.command.RegisterNewUserProfileCommand;
import io.acme.solution.api.model.CommandPromise;
import io.acme.solution.api.model.UserProfile;
import io.acme.solution.api.validation.UserProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * The application interface responsible for handling the users profiles.
 * The class is located in the application layer
 */
@RestController
@RequestMapping(RESTKeys.Profile.BASE)
public class ProfileApplication {

    private static final Logger log = LoggerFactory.getLogger(ProfileApplication.class);

    @Autowired
    private CommandPublisher commandPublisher;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {

        binder.setValidator(new UserProfileValidator());
    }


    @RequestMapping(value = RESTKeys.Profile.REGISTRATION, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommandPromise> registerProfile(@Valid @RequestBody final UserProfile profile) {
        final RegisterNewUserProfileCommand registerCommand = new RegisterNewUserProfileCommand(UUID.randomUUID(),
                profile.getUsername(), profile.getEmail(), profile.getPassword());

        this.commandPublisher.publish(registerCommand);
        return new ResponseEntity<CommandPromise>(new CommandPromise(registerCommand.getId(), "Success!"), HttpStatus.OK);
    }
}
