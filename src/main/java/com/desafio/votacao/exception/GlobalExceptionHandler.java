package com.desafio.votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAll(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        HttpStatus status = resolveStatus(ex);
        return new ResponseEntity<>(body, status);
    }

    private HttpStatus resolveStatus(RuntimeException ex) {
        if (ex instanceof CpfNotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof ProposalNotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof DuplicateVoteException) return HttpStatus.CONFLICT;
        if (ex instanceof UserUnableToVoteException) return HttpStatus.FORBIDDEN;
        return HttpStatus.BAD_REQUEST;
    }
}