package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {
    /**
     * Exception handler method for SignUpRestrictedException
     *
     * @param exe SignUpRestrictedException exception
     * @param request WebRequest
     * @return ErrorResponse returning message and HTTP status
     */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictedException(SignUpRestrictedException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.CONFLICT);
    }

    /**
     * Exception handler for AuthenticationFailedException
     *
     * @param exe AuthenticationFailedException exception
     * @param request WebRequest
     * @return ErrorResponse returning message and HTTP status
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Exception handler for SignOutRestrictedException
     *
     * @param exe SignOutRestrictedException exception
     * @param request WebRequest
     * @return ErrorResponse returning message and HTTP status
     */
    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.UNAUTHORIZED );
    }

    /**
     * Exception handler for AuthorizationFailedException
     *
     * @param exe AuthorizationFailedException exception
     * @param request WebRequest
     * @return ErrorResponse returning message and HTTP status
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.FORBIDDEN);
    }

    /**
     * Exception handler for UserNotFoundException
     *
     * @param exe UserNotFoundException exception
     * @param request WebRequest
     * @return ErrorResponse returning message and HTTP status
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exe, WebRequest request) {

        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for InvalidQuestionException
     *
     * @param exe InvalidQuestionException exception
     * @param request WebRequest
     * @return ErrorResponse returning message and HTTP status
     */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(InvalidQuestionException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for AnswerNotFoundException
     *
     * @param exe AnswerNotFoundException exception
     * @param request WebRequest
     * @return ErrorResponse returning message and HTTP status
     */
    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFoundException(AnswerNotFoundException exe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }
}
