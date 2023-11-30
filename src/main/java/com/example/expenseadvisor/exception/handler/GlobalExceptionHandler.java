package com.example.expenseadvisor.exception.handler;

import com.example.expenseadvisor.exception.domain.CustomException;
import com.example.expenseadvisor.exception.domain.ErrorCode;
import com.example.expenseadvisor.exception.dto.CustomErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExceptionHandler 호출 우선순위
 * 1. 가장 구체적인 예외 유형의 핸들러
 * 2. 더 먼저(더 위에) 정의된 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse<Object>> handleCustomException(CustomException ex) {
        logError(ex);
        ErrorCode errorCode = ex.getErrorCode();
        return createResponseEntity(errorCode.name(), errorCode.getMessage(), null, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse<Map<String, List<String>>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        logError(ex);
        ErrorCode errorCode = ErrorCode.GEN_NOT_VALID_ARGUMENTS;
        return createResponseEntity(errorCode.name(), errorCode.getMessage(), getArgumentValidationMessages(ex), errorCode.getHttpStatus());
    }

    /**
     * MethodArgumentNotValidExeption으로부터 field 별 에러 메시지를 생성하여 리턴한다.
     *
     * @param ex MethodArgumentNotValidException
     * @return field 별 에러 메시지
     */
    private Map<String, List<String>> getArgumentValidationMessages(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, List<String>> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(
                error -> {
                    if (errorMap.containsKey(error.getField())) {
                        errorMap.get(error.getField()).add(error.getDefaultMessage());
                    } else if (error.getDefaultMessage() != null) {
                        errorMap.put(error.getField(), List.of(error.getDefaultMessage()));
                    } else {
                        errorMap.put(error.getField(), List.of());
                    }
                }
        );
        return errorMap;
    }

    private <T> ResponseEntity<CustomErrorResponse<T>> createResponseEntity(String code, String message, T errors, HttpStatus httpStatus) {
        CustomErrorResponse<T> customErrorResponse = new CustomErrorResponse<>(code, message, errors);
        return ResponseEntity
                .status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(customErrorResponse);
    }

    private void logError(Exception ex) {
        log.error(ex.getClass().getSimpleName(), ex);
    }

}
