package org.transactions.sync.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.model.transactions.Transaction;
import org.transactions.connector.ITransactionsReadOnlyDatasource;
import org.transactions.sync.connector.ITransactionsAggregateDatasource;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FromMongoToEsSyncServiceTest {

    @Test
    void syncDatabase(@Mock ITransactionsReadOnlyDatasource readOnlyDatasource, @Mock ITransactionsAggregateDatasource aggregateDatasource) {

        List<Transaction> transactions = new ArrayList<>();
        Mockito.when(readOnlyDatasource.getAllTransactions()).thenReturn(transactions);

        new FromMongoToEsSyncService(readOnlyDatasource, aggregateDatasource).syncDatabase();

        verify(aggregateDatasource, times(1)).resetData();
        verify(readOnlyDatasource, times(1)).getAllTransactions();
        verify(aggregateDatasource, times(1)).publishData(transactions);

    }
}