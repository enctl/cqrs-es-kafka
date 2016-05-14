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

package io.acme.solution.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * A builder class for creating/reading mementos
 */
public class MementoBuilder {

    private static final Logger log = LoggerFactory.getLogger(MementoBuilder.class);

    public static String flatten(final HashMap<String, Object> attributesMap) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(attributesMap);
        } catch (JsonProcessingException exception) {
            log.debug("Failed to craete memento object");
            log.trace("Failed to create memento object for the passed attributes map", exception);
            return null;
        }
    }

    public static HashMap<String, Object> construct(final String state) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return (HashMap<String, Object>) mapper.readValue(state, HashMap.class);
        } catch (IOException exception) {
            log.debug("Failed to reconstruct memento object");
            log.trace("Failed to reconstruct the memento attributes map", exception);
            return null;
        }
    }
}
