package seb42_main_026.mainproject.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {

    private List<FieldError> fieldErrors;

    private List<ConstraintViolationError> violationErrors;

    private HttpStatus httpStatus;

    private String message;

    private CustomErrors customErrors;

    public ErrorResponse( CustomErrors CustomErrors){

        this.customErrors = CustomErrors;

    }

    public ErrorResponse(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;

    }



    public ErrorResponse(List<FieldError> fieldErrors, List<ConstraintViolationError> violationErrors){
        this.fieldErrors = fieldErrors;
        this.violationErrors = violationErrors;

    }

    public static ErrorResponse of(ExceptionCode exceptionCode){
        return new ErrorResponse(CustomErrors.of(exceptionCode));
    }

    public static ErrorResponse of(BindingResult bindingResult){
        return new ErrorResponse(FieldError.of(bindingResult), null);
    }

    public static ErrorResponse of(Set<ConstraintViolation<?>> violations){
        return new ErrorResponse(null, ConstraintViolationError.of(violations));
    }

    public static ErrorResponse of(HttpStatus httpStatus){
        return new ErrorResponse(httpStatus, httpStatus.getReasonPhrase());
    }

    @Getter
    public static class FieldError {

        private String field;
        private String rejectedValue;
        private String message;

        public FieldError(String field, String rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public static List<FieldError> of(BindingResult bindingResult) {

            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();

            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());

        }

    }

        @Getter
        public static class ConstraintViolationError{
            private String propertyPath;
            private String invalidValue;
            private String message;

            public ConstraintViolationError(String propertyPath, String invalidValue, String message) {
                this.propertyPath = propertyPath;
                this.invalidValue = invalidValue;
                this.message = message;
            }

            public static List<ConstraintViolationError> of(Set<ConstraintViolation<?>> constraintViolations){
                return constraintViolations.stream()
                        .map(constraintViolation -> new ConstraintViolationError(
                                constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getInvalidValue().toString(),
                                constraintViolation.getMessage()))
                        .collect(Collectors.toList());


            }
        }


    @Getter
    public static class CustomErrors{
        private HttpStatus httpStatus;
        private String message;
        private String field;

        public CustomErrors(HttpStatus httpStatus, String message, String field) {
            this.httpStatus = httpStatus;
            this.message = message;
            this.field = field;
        }

        public static CustomErrors of(ExceptionCode exceptionCode){

            CustomErrors customErrors = new CustomErrors(exceptionCode.getHttpStatus(), exceptionCode.getMessage(), exceptionCode.getField());

            return customErrors;

        }
    }


 }



