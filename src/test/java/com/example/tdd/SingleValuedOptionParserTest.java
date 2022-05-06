package com.example.tdd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;

import static com.example.tdd.BooleanOptionParserTest.option;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SingleValuedOptionParserTest {
    @Test
    void should_not_accept_extra_arguments_for_single_valued_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValuedOptionParser<>(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
        });

        assertEquals("p", e.getOption());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-p", "-p -l"})
    void should_not_accept_insufficient_argument_for_single_valued_option(String arguments) {
        InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
            new SingleValuedOptionParser<>(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
        });

        assertEquals("p", e.getOption());
    }

    @Test
    void should_set_default_value_to_0_for_int_option() {
        Function<String, Object> whatever = it -> null;
        Object defaultValue = new Object();
        assertSame(defaultValue, new SingleValuedOptionParser<>(defaultValue, whatever).parse(asList(), option("p")));
    }

    @Test
    void should_parse_value_if_flag_present() {
        Object parsed = new Object();
        Function<String, Object> parse = it -> parsed;
        Object whatever = new Object();
        assertSame(parsed, new SingleValuedOptionParser<>(whatever, parse).parse(asList("-p", "8080"), option("p")));
    }
}