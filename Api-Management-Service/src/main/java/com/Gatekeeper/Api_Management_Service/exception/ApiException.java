package com.Gatekeeper.Api_Management_Service.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException{

    private final HttpStatus httpStatus;

    public ApiException( HttpStatus httpStatus,String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
