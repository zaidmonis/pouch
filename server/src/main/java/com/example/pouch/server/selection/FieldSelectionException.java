package com.example.pouch.server.selection;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FieldSelectionException extends ResponseStatusException {
    public FieldSelectionException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
