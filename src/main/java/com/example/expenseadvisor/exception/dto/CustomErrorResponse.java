package com.example.expenseadvisor.exception.dto;

import lombok.Getter;

@Getter
public class CustomErrorResponse<T> {

    private final String code;
    private final String message;
    private final T errors;

    public CustomErrorResponse(String code, String message, T errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

}
