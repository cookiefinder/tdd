package com.example.tdd;

import com.example.tdd.exceptions.InsufficientArgumentsException;
import com.example.tdd.exceptions.TooManyArgumentsException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static com.example.tdd.OptionParsers.bool;
import static com.example.tdd.OptionParsers.unary;
import static com.example.tdd.OptionParsersTest.BoolOptionParser.option;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OptionParsersTest {

    @Nested
    class UnaryOptionParser {
        @Test
        void should_not_accept_extra_arguments_for_single_valued_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                unary(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
            });

            assertEquals("p", e.getOption());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-p", "-p -l"})
        void should_not_accept_insufficient_argument_for_single_valued_option(String arguments) {
            InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
                unary(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
            });

            assertEquals("p", e.getOption());
        }

        @Test
        void should_set_default_value_to_0_for_int_option() {
            Function<String, Object> whatever = it -> null;
            Object defaultValue = new Object();
            assertSame(defaultValue, unary(defaultValue, whatever).parse(asList(), option("p")));
        }

        @Test
        void should_parse_value_if_flag_present() {
            Object parsed = new Object();
            Function<String, Object> parse = it -> parsed;
            Object whatever = new Object();
            assertSame(parsed, unary(whatever, parse).parse(asList("-p", "8080"), option("p")));
        }
    }

    @Nested
    class BoolOptionParser {
        @Test
        void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                bool().parse(asList("-l", "t"), option("l"));
            });

            assertEquals("l", e.getOption());
        }

        @Test
        void should_set_default_value_to_false_if_option_not_present() {
            assertFalse(bool().parse(asList(), option("l")));
        }

        @Test
        void should_set_boolean_option_to_true_if_option_present() {
            assertTrue(bool().parse(asList("-l"), option("l")));
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
}