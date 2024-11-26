package com.backend.banking.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private double amount;

    public TransactionDTO(double amount) {
        this.amount = amount;
    }

    // Getters and setters
}
