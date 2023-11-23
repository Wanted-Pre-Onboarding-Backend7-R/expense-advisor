package com.example.expenseadvisor.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    AUTH_MEMBER_NOT_EXISTS("존재하지 않는 사용자입니다.", HttpStatus.UNAUTHORIZED)
    ;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final String message;
    private final HttpStatus httpStatus;

}
