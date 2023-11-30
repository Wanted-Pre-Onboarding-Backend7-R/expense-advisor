package com.example.expenseadvisor.exception.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // AUTH
    AUTH_MEMBER_NOT_EXISTS("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST),

    // BUDGET(예산)
    BUD_ALREADY_EXISTS("이미 해당 사용자는 예산을 가지고 있습니다.", HttpStatus.BAD_REQUEST),

    // CATEGORY(카테고리)
    CAT_NOT_EXISTS("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),

    // GENERAL
    GEN_NOT_VALID_ARGUMENTS("전달된 값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final String message;
    private final HttpStatus httpStatus;

}
