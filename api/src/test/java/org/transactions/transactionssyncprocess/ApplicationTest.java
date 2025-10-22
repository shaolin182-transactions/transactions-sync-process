package org.transactions.transactionssyncprocess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.transactions.persistence.repositories.TransactionAggregateRepository;
import org.transactions.persistence.repositories.TransactionsRepository;
import org.transactions.transactionssyncprocess.service.AppStartService;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTest {

    @Autowired
    AppStartService service;

    // Mock persistence layer
    @MockitoBean
    TransactionsRepository repository;

    // Mock persistence layer
    @MockitoBean
    TransactionAggregateRepository aggregateRepository;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertNotNull(service);
    }
}