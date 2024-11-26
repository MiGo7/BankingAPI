package com.backend.banking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long txId;

    @NonNull
    private double amount;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "accountId")
    @JsonBackReference
    private Account account;
}
