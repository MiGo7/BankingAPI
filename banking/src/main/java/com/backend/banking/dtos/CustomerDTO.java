package com.backend.banking.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CustomerDTO {
    private String name;
    private String surname;
    private double totalBalance;
    private List<AccountDTO> accounts;

    public CustomerDTO(String name, String surname, double totalBalance, List<AccountDTO> accounts) {
        this.name = name;
        this.surname = surname;
        this.totalBalance = totalBalance;
        this.accounts = accounts;
    }

    // Getters and setters
}

