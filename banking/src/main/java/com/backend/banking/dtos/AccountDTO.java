package com.backend.banking.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountDTO {
    private Long accountId;
    private double balance;
    private List<TransactionDTO> transactions;

    public AccountDTO(Long accountId, double balance, List<TransactionDTO> transactions) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactions = transactions;
    }

    // Getters and setters
}

