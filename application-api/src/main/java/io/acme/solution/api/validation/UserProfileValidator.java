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

package io.acme.solution.api.validation;

import io.acme.solution.api.bundle.MessageKeys;
import io.acme.solution.api.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * Spring Bean Validation 1.1 for the UserProfile object
 */
public class UserProfileValidator implements Validator {

    private static final Logger log = LoggerFactory.getLogger(UserProfileValidator.class);

    @Override
    public boolean supports(final Class<?> clazz) {
        return UserProfile.class.equals(clazz);
    }

    @Override
    public void validate(final Object profile, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, UserProfile.FIELD_USERNAME, MessageKeys.UserProfile.EMPTY_USERNAME);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, UserProfile.FIELD_EMAIL, MessageKeys.UserProfile.EMPTY_EMAIL);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, UserProfile.FIELD_PASSWORD, MessageKeys.UserProfile.EMPTY_PASSWORD);
        this.rejectIfNotEmail((UserProfile) profile, errors);
        this.rejectIfNotUsername((UserProfile) profile, errors);
    }


    private void rejectIfNotUsername(final UserProfile profile, final Errors errors) {
        Pattern validUsernamePattern = Pattern.compile(USERNAME_PATTERN, Pattern.CASE_INSENSITIVE);
        if (!validUsernamePattern.matcher(profile.getUsername()).matches()) {
            errors.rejectValue(UserProfile.FIELD_USERNAME, MessageKeys.UserProfile.INVALID_USERNAME);
        }
    }

    private void rejectIfNotEmail(final UserProfile profile, final Errors errors) {
        Pattern validEMailPattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        if (!validEMailPattern.matcher(profile.getEmail()).matches()) {
            errors.rejectValue(UserProfile.FIELD_EMAIL, MessageKeys.UserProfile.INVALID_EMAIL);
        }
    }

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9.\\-_]*$";
    private static final String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
}
