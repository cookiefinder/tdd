package com.example.tdd;

import com.example.tdd.exceptions.IllegalValueException;
import com.example.tdd.exceptions.InsufficientArgumentsException;
import com.example.tdd.exceptions.TooManyArgumentsException;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class OptionParsers {

    public static <T> OptionParser<T> unary(T defaultValue, Function<String, T> valueParser) {
        return (arguments, option) -> values(arguments, option, 1)
                .map(it -> parseValue(option, it.get(0), valueParser))
                .orElse(defaultValue);
    }

    public static OptionParser<Boolean> bool() {
        return (arguments, option) -> values(arguments, option, 0).isPresent();
    }

    public static <T> OptionParser<T[]> list(IntFunction<T[]> generator, Function<String, T> valueParser) {
        return (arguments, option) -> values(arguments, option)
                .map(it -> it.stream().map(value -> parseValue(option, value, valueParser)).toArray(generator))
                .orElse(generator.apply(0));
    }

    public static <K, V> OptionParser<Map<K, V>> map(Map<K, V> defaultValue, Function<String, K> keyParser, Function<String, V> valueParser) {
        return (arguments, option) -> values(arguments, option)
                .map(it -> parseValue(option, it, keyParser, valueParser))
                .orElse(defaultValue);
    }

    private static Optional<List<String>> values(List<String> arguments, Option option) {
        int[] indexes = IntStream.range(0, arguments.size())
                .filter(it -> {
                    String flag = FORMATS.get(option.format()).concat(option.value());
                    return flag.equals(arguments.get(it));
                }).toArray();
        if (indexes.length == 0) {
            return Optional.empty();
        }
        return Optional.of(Arrays.stream(indexes)
                .mapToObj(index -> values(arguments, index))
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    private static final Map<Format, String> FORMATS = Map.of(Format.DASH, "--",
            Format.HYPHEN, "-");

    private static Optional<List<String>> values(List<String> arguments, Option option, int expectedSize) {
        return values(arguments, option).map(it -> checkSize(option, expectedSize, it));
    }

    private static List<String> checkSize(Option option, int expectedSize, List<String> values) {
        if (values.size() < expectedSize) {
            throw new InsufficientArgumentsException(option.value());
        }
        if (values.size() > expectedSize) {
            throw new TooManyArgumentsException(option.value());
        }
        return values;
    }

    private static <T> T parseValue(Option option, String value, Function<String, T> valueParser) {
        try {
            return valueParser.apply(value);
        } catch (Exception e) {
            throw new IllegalValueException(option.value(), value);
        }
    }

    private static <K, V> Map<K, V> parseValue(Option option, List<String> values, Function<String, K> keyParser, Function<String, V> valueParser) {
        Map<K, V> map = new HashMap<>();
        values.forEach(value -> {
            try {
                String[] split = value.split("=");
                map.put(keyParser.apply(split[0]), valueParser.apply(split[1]));
            } catch (Exception e) {
                throw new IllegalValueException(option.value(), value);
            }
        });
        return map;
    }

    private static List<String> values(List<String> arguments, int index) {
        return arguments.subList(index + 1, IntStream.range(index + 1, arguments.size())
                .filter(it -> arguments.get(it).matches("^-[-]?[a-zA-Z]+$"))
                .findFirst().orElse(arguments.size()));
    }
}
