package com.backend.banking.service;

import com.backend.banking.dtos.AccountDTO;
import com.backend.banking.dtos.CustomerDTO;
import com.backend.banking.dtos.TransactionDTO;
import com.backend.banking.entity.Account;
import com.backend.banking.entity.Customer;
import com.backend.banking.entity.Transaction;
import com.backend.banking.exception.UserNotFoundException;
import com.backend.banking.repository.AccountRepository;
import com.backend.banking.repository.CustomerRepository;
import com.backend.banking.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankingService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @PostConstruct
    public void initDB() {

        final Customer customer1 = new Customer();
        customer1.setName("John");
        customer1.setSurname("Doe");

        final Customer customer2 = new Customer();
        customer2.setName("Jane");
        customer2.setSurname("Smith");

        customerRepository.save(customer1);
        customerRepository.save(customer2);
    }


    @Transactional
    public Account openAccount(final Long customerId, final Double initialCredit) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + customerId + " not found"));

        final Account newAccount = new Account();
        newAccount.setCustomer(customer);

        if (initialCredit > 0) {
            newAccount.setBalance(initialCredit);
            final Transaction transaction = new Transaction();
            transaction.setAmount(initialCredit);
            transaction.setAccount(newAccount);
            newAccount.getTransactions().add(transaction);
            transactionRepository.save(transaction);
        }

        return accountRepository.save(newAccount);
    }

    public CustomerDTO getCustomerDetails(final Long customerId) {
        // Fetch the customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + customerId + " not found"));

        // Aggregate account details
        double totalBalance = 0.0;
        List<AccountDTO> accounts = new ArrayList<>();

        for (Account account : customer.getAccounts()) {
            totalBalance += account.getBalance();

            List<TransactionDTO> transactions = account.getTransactions().stream()
                    .map(transaction -> new TransactionDTO(transaction.getAmount()))
                    .collect(Collectors.toList());

            accounts.add(new AccountDTO(account.getAccountId(), account.getBalance(), transactions));
        }

        // Return the DTO with all information
        return new CustomerDTO(customer.getName(), customer.getSurname(), totalBalance, accounts);
    }
}
