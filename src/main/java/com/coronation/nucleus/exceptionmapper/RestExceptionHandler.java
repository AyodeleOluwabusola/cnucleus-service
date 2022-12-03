package com.coronation.nucleus.exceptionmapper;

import com.coronation.nucleus.enums.IResponseEnum;
import com.coronation.nucleus.pojo.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String responseDescription = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        ResponseData<String> response = ResponseData.getResponseData(IResponseEnum.INVALID_REQUEST,"Invalid request : " + responseDescription, null );

        return new ResponseEntity(response, headers, HttpStatus.OK);
    }

}
