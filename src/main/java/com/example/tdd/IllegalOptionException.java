package com.example.tdd;

public class IllegalOptionException extends RuntimeException {
    private String parameter;

    public IllegalOptionException(String option) {
        this.parameter = option;
    }

    public String getParameter() {
        return parameter;
    }
}
