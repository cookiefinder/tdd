package com.example.tdd;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanOptionParserTest {
    @Test
    void should_not_accept_extra_argument_for_boolean_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new BooleanOptionParser().parse(asList("-l", "t"), option("l"));
        });

        assertEquals("l", e.getOption());
    }

    @Test
    void should_set_default_value_to_false_if_option_not_present() {
        assertFalse(new BooleanOptionParser().parse(asList(), option("l")));
    }

    @Test
    void should_set_boolean_option_to_true_if_option_present() {
        assertTrue(new BooleanOptionParser().parse(asList("-l"), option("l")));
    }

    static Option option(String value) {
        return new Option() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}