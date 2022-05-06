package com.example.tdd;

import java.util.List;
import java.util.function.Function;

class SingleValuedOptionParser<T> implements OptionParser {
    Function<String, T> valueParser;

    public SingleValuedOptionParser(Function<String, T> valueParser) {
        this.valueParser = valueParser;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-".concat(option.value()));
        String value = arguments.get(index + 1);
        return valueParser.apply(value);
    }

}
