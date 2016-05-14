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

package io.acme.solution.api.command;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Register New User Profile Command.
 */
public class RegisterNewUserProfileCommand extends Command {

    public static final String MAPKEY_ID = "id";
    public static final String MAPKEY_USERNAME = "username";
    public static final String MAPKEY_EMAIL = "email";
    public static final String MAPKEY_PASSWORD = "password";

    private String username;
    private String email;
    private String password;

    public RegisterNewUserProfileCommand() {
        super();
    }

    public RegisterNewUserProfileCommand(final UUID id, final String username, final String email, final String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }


    @Override
    public Map<String, Object> getEntries() {
        final Map<String, Object> commandEntries = new HashMap<>();

        commandEntries.put(MAPKEY_ID, this.id);
        commandEntries.put(MAPKEY_USERNAME, this.username);
        commandEntries.put(MAPKEY_EMAIL, this.email);
        commandEntries.put(MAPKEY_PASSWORD, this.password);

        return commandEntries;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getSimpleName() + " : " + this.getId() + " : " + this.username + " : " + this.email + "}";
    }
}
