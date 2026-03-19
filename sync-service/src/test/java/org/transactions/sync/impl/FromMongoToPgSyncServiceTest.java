package org.transactions.sync.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.model.transactions.Transaction;
import org.transactions.connector.ITransactionsReadOnlyDatasource;
import org.transactions.sync.entities.MigrationData;
import org.transactions.sync.mapper.TransactionMapper;
import org.transactions.sync.repositories.MigrationDataRepository;
import org.transactions.sync.repositories.MigrationHistoryRepository;
import org.transactions.transactionssyncprocess.clients.transactions.TransactionRestClient;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FromMongoToPgSyncServiceTest {

    @Mock
    ITransactionsReadOnlyDatasource trDatasource;

    @Mock
    MigrationDataRepository migrationDataRepository;

    @Mock
    MigrationHistoryRepository migrationHistoryRepository;

    @Mock
    TransactionMapper transactionMapper;

    @Mock
    TransactionRestClient transactionRestClient;

    @Test
    void syncDatabase() {

        // Prepare data & mocks
        var list = new ArrayList<Transaction>();
        list.add(new Transaction().id("1"));
        list.add(new Transaction().id("2"));
        list.add(new Transaction().id("3"));
        list.add(new Transaction().id("4"));
        doReturn(list).when(trDatasource).getAllTransactions();


        var migrationData = new MigrationData();
        migrationData.setFromIdRecord("1");
        migrationData.setStatus("DONE");
        when(migrationDataRepository.findByFromIdAndStatus("1", "DONE")).thenReturn(java.util.Optional.of(migrationData));

        var trRest = new org.transactions.clients.transactions.model.Transaction().id("someID");
        when(transactionRestClient.createTransaction(any())).thenReturn(trRest);

        when(transactionMapper.transactionToRest(any())).thenReturn(new org.transactions.clients.transactions.model.Transaction());

        // Run service
        var service = new FromMongoToPgSyncService(trDatasource, transactionRestClient, transactionMapper, migrationHistoryRepository, migrationDataRepository);
        service.syncDatabase();

        // Assertions
        verify(trDatasource, times(1)).getAllTransactions();
        verify(transactionMapper, times(3)).transactionToRest(any());
        verify(migrationDataRepository, times(4)).findByFromIdAndStatus(anyString(), anyString());
        verify(transactionRestClient, times(3)).createTransaction(any());
        verify(migrationHistoryRepository, times(1)).save(any());
    }
}