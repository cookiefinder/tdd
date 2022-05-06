package com.example.tdd;

import java.util.List;

class BooleanOptionParser implements OptionParser {

    @Override
    public Object parse(List<String> arguments, Option option) {
        return arguments.contains("-".concat(option.value()));
    }
}
