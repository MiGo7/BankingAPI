package com.backend.banking.controller;

import com.backend.banking.dtos.CustomerDTO;
import com.backend.banking.entity.Account;
import com.backend.banking.service.BankingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling banking-related operations.
 * This class provides REST APIs for opening an account and customer details retrieval.
 */
@Tag(name = "Banking", description = "The Banking API")
@RestController
@RequestMapping("/api")
public class BankingController {

    @Autowired
    private BankingService bankingService;

    /**
     * API endpoint to open a new bank account.
     *
     * @param customerId    the ID of the customer for whom the account is to be opened
     * @param initialCredit the initial deposit amount for the account
     * @return ResponseEntity Account containing the details of the newly created account
     */
    @PostMapping("/account/open")
    public ResponseEntity<Account> openAccount(@RequestParam final Long customerId, @RequestParam final Double initialCredit) {
        return ResponseEntity.ok(bankingService.openAccount(customerId, initialCredit));
    }

    /**
     * API endpoint to retrieve details of a customer by their ID.
     *
     * @param customerId the ID of the customer whose details are being requested
     * @return ResponseEntity containing the customer details encapsulated in a CustomerDTO
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerDetails(@PathVariable final Long customerId) {
        return ResponseEntity.ok(bankingService.getCustomerDetails(customerId));
    }
}
