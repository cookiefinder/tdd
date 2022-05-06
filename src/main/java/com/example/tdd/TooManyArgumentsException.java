package com.example.tdd;

public class TooManyArgumentsException extends RuntimeException {
    private String option;

    public TooManyArgumentsException(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
