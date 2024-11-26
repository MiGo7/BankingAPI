package com.backend.banking.service;

import com.backend.banking.entity.Account;
import com.backend.banking.entity.Customer;
import com.backend.banking.entity.Transaction;
import com.backend.banking.repository.AccountRepository;
import com.backend.banking.repository.CustomerRepository;
import com.backend.banking.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankingServiceTest {


    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private BankingService bankingService;


    @Test
    public void testOpenAccountWithInitialCredit() {
        // Arrange
        Long customerId = 1L;
        Double initialCredit = 500.0;

        Customer mockCustomer = createMockCustomer();

        Account mockAccount = new Account();
        mockAccount.setCustomer(mockCustomer);
        mockAccount.setBalance(initialCredit);

        Transaction mockTransaction = new Transaction();
        mockTransaction.setAmount(initialCredit);
        mockTransaction.setAccount(mockAccount);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        Account result = bankingService.openAccount(customerId, initialCredit);

        // Assert
        assertNotNull(result);

        System.out.println(mockCustomer);
        System.out.println(result);
        assertEquals(initialCredit, result.getBalance());
        assertEquals(mockCustomer, result.getCustomer());
        assertEquals(1, result.getTransactions().size());
        assertEquals(initialCredit, result.getTransactions().get(0).getAmount());

        // Verify interactions
        verify(customerRepository, times(1)).findById(customerId);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }


    @Test
    public void print() {

        System.out.println(createMockCustomer());
    }


    private Customer createMockCustomer() {
        final Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John");
        customer.setSurname("Doe");

        return customer;
    }

    private List<Account> createMockAccountsList() {

        List<Account> accountList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            accountList.add(createMockAccount());
        }
        return accountList;
    }


    private Account createMockAccount() {
        final Account mockAccount = new Account();
        mockAccount.setAccountId(1L);
        mockAccount.setBalance(500);
        mockAccount.setCustomer(new Customer());
        mockAccount.setTransactions(createMockTransactionsList());

        return mockAccount;
    }

    private List<Transaction> createMockTransactionsList() {
        List<Transaction> transactionList = new ArrayList<>();

        for (int i = 1; i < 3; i++) {
            transactionList.add(createMockTransaction());
        }
        return transactionList;
    }


    private Transaction createMockTransaction() {
        final Transaction transaction = new Transaction();
        transaction.setAmount(100);
        transaction.setTxId(1L);

        return transaction;
    }


    @Test
    public void testGetCustomerInfo() {

    }

}
