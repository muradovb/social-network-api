package com.akamai.socialnetwork.advice;


import com.akamai.socialnetwork.exception.ElementNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostControllerAdvice {

    @ExceptionHandler(value = {ElementNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(ElementNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
