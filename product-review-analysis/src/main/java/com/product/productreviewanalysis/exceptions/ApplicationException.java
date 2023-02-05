package com.product.productreviewanalysis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationException{


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String > handlerMethodArgumentException(MethodArgumentNotValidException exception)
    {
        Map<String ,String > errorMessages = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(
                fieldError -> {
                    errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
        );
        return errorMessages;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String,String> handlerUserNotFoundException(UserNotFoundException exception)
    {
        Map<String ,String > errorMsgs = new HashMap<>();

        errorMsgs.put("error messsage",exception.getMessage());

        return errorMsgs;
    }

}
