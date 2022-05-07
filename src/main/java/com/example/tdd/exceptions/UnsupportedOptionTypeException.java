package com.example.tdd.exceptions;

public class UnsupportedOptionTypeException extends RuntimeException {
    private String option;
    private Class<?> type;

    public UnsupportedOptionTypeException(String option, Class<?> type) {
        this.option = option;
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public Class<?> getType() {
        return type;
    }
}
