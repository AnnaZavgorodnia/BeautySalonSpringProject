package com.salon.controller;

import com.salon.controller.error.ResponseError;
import com.salon.controller.exceptions.NotUniqueUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

//@ControllerAdvice
//public class ErrorHandler extends ResponseEntityExceptionHandler {
//
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler
//    @ResponseBody
//    public ResponseError conflictExceptionHandler(SQLIntegrityConstraintViolationException ex) {
//        return new ResponseError(ex.getMessage());
//    }
//}
