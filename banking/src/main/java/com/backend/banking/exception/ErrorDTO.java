package com.backend.banking.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDTO {
    private String message;
    private String details;
    private int statusCode;
}
