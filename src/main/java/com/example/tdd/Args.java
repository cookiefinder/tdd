package com.example.tdd;

import com.example.tdd.exceptions.IllegalOptionException;
import com.example.tdd.exceptions.UnsupportedOptionTypeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.tdd.OptionParsers.bool;
import static com.example.tdd.OptionParsers.list;
import static com.example.tdd.OptionParsers.unary;


public class Args {
    public static <T> T parse(Class<T> optionClass, String... args) {
        try {
            List<String> arguments = Arrays.stream(args).toList();
            Constructor<?> constructor = optionClass.getDeclaredConstructors()[0];

            Object[] values = Arrays.stream(constructor.getParameters()).map(it -> parseOption(arguments, it)).toArray();

            return (T) constructor.newInstance(values);
        } catch (IllegalOptionException | UnsupportedOptionTypeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object parseOption(List<String> arguments, Parameter parameter) {
        if (!parameter.isAnnotationPresent(Option.class)) {
            throw new IllegalOptionException(parameter.getName());
        }
        Class<?> type = parameter.getType();
        Option option = parameter.getDeclaredAnnotation(Option.class);
        if (!PARSERS.keySet().contains(type)) {
            throw new UnsupportedOptionTypeException(option.value(), type);
        }
        return getOptionParser(type).parse(arguments, option);
    }

    private static OptionParser getOptionParser(Class<?> type) {
        return PARSERS.get(type);
    }

    private static Map<Class<?>, OptionParser> PARSERS = Map.of(
            boolean.class, bool(),
            int.class, unary(0, Integer::parseInt),
            String.class, unary("", String::valueOf),
            String[].class, list(String[]::new, String::valueOf),
            Integer[].class, list(Integer[]::new, Integer::parseInt)
    );
}
