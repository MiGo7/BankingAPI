package com.backend.banking.controller;

import com.backend.banking.dtos.AccountDTO;
import com.backend.banking.dtos.CustomerDTO;
import com.backend.banking.dtos.TransactionDTO;
import com.backend.banking.entity.Account;
import com.backend.banking.entity.Customer;
import com.backend.banking.service.BankingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BankingController.class)
@AutoConfigureMockMvc
public class BankingControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankingService bankingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testOpenAccount() throws Exception {

        // Mock service method
        when(bankingService.openAccount(anyLong(), anyDouble())).thenReturn(createMockAccount());

        // Perform POST request and validate response
        mockMvc.perform(post("/api/account/open")
                        .param("customerId", String.valueOf(1L))
                        .param("initialCredit", String.valueOf(500)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1L))
                .andExpect(jsonPath("$.balance").value(500));

        // Verify service interaction
        verify(bankingService, times(1)).openAccount(anyLong(), anyDouble());
    }


    @Test
    public void testGetCustomerInfo() throws Exception {
        // Prepare mock CustomerDTO
        List<TransactionDTO> transactions1 = Arrays.asList(
                new TransactionDTO(50.0),
                new TransactionDTO(50.0)
        );

        List<TransactionDTO> transactions2 = Arrays.asList(
                new TransactionDTO(200.0)
        );

        List<AccountDTO> accounts = Arrays.asList(
                new AccountDTO(1L, 100.0, transactions1),
                new AccountDTO(2L, 200.0, transactions2)
        );

        CustomerDTO customerInfo = new CustomerDTO(
                "John",
                "Doe",
                300.0,
                accounts
        );

        // Mock the service call
        when(bankingService.getCustomerDetails(1L)).thenReturn(customerInfo);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/customer/{customerId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.totalBalance").value(300.0))
                .andExpect(jsonPath("$.accounts[0].accountId").value(1L))
                .andExpect(jsonPath("$.accounts[0].balance").value(100.0))
                .andExpect(jsonPath("$.accounts[0].transactions[0].amount").value(50.0))
                .andExpect(jsonPath("$.accounts[1].accountId").value(2L))
                .andExpect(jsonPath("$.accounts[1].balance").value(200.0))
                .andExpect(jsonPath("$.accounts[1].transactions[0].amount").value(200.0));

        // Verify the service method was called once
        verify(bankingService, times(1)).getCustomerDetails(1L);
    }

    private Account createMockAccount() {
        final Account mockAccount = new Account();
        mockAccount.setAccountId(1L);
        mockAccount.setBalance(500);
        mockAccount.setCustomer(new Customer());
        mockAccount.setTransactions(new ArrayList<>());

        return mockAccount;
    }

    private Customer createMockCustomer() {
        final Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("John");
        customer.setSurname("Doe");
        customer.setAccounts(new ArrayList<>());

        return customer;
    }
}
