package io.github.tayluansantos.expense_tracker_api.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(int status, String message, List<FieldError> fieldErrorList) {

    public static ErrorResponse defaultMessage(int status,String message){
        return new ErrorResponse(status,message,List.of());
    }
}
