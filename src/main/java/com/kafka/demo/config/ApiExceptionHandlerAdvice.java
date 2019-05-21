package com.kafka.demo.config;

import com.gemantic.springcloud.model.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.MissingFormatArgumentException;



@RestControllerAdvice
public class ApiExceptionHandlerAdvice {
    
    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    @ExceptionHandler(value = {ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class, IllegalArgumentException.class,
            IllegalStateException.class, MissingFormatArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage constraintViolationException(HttpServletRequest request, Exception ex) {
        LOG.error(request.getRequestURI() + "?" + request.getQueryString(), ex);
        return ResponseMessage.error(500, -5001, ex.getMessage());
    }
    
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage unknownException(HttpServletRequest request, Exception ex) {
        LOG.error(request.getRequestURI() + "?" + request.getQueryString(), ex);
        return ResponseMessage.error(500, -5002, ex.getMessage());
    }
    
}
