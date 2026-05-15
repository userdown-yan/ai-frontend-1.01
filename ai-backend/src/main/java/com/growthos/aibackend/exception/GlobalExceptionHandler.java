package com.growthos.aibackend.exception;

import com.growthos.aibackend.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handle(Exception e) {

        return Result.error(e.getMessage());
    }
}