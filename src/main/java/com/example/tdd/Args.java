package com.example.tdd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class Args {
    public static <T> T parse(Class<T> optionClass, String... args) {
        try {
            Constructor<?> constructor = optionClass.getDeclaredConstructors()[0];
            Parameter parameter = constructor.getParameters()[0];
            Option annotation = parameter.getDeclaredAnnotation(Option.class);
            List<String> arguments = Arrays.stream(args).toList();
            return (T) constructor.newInstance(arguments.contains("-".concat(annotation.value())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
