package org.transactions.sync.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.transactions.clients.transactions.model.Transaction;
import org.transactions.sync.connector.ITransactionsAggregateDatasource;
import org.transactions.sync.mapper.TransactionMapper;
import org.transactions.transactionssyncprocess.clients.transactions.TransactionRestClient;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FromPgToEsSyncServiceTest {

    @Test
    void syncDatabase(@Mock TransactionRestClient readOnlyDatasource, @Mock ITransactionsAggregateDatasource aggregateDatasource, @Mock TransactionMapper mapper) {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction().id("1"));
        transactions.add(new Transaction().id("2"));
        transactions.add(new Transaction().id("3"));
        transactions.add(new Transaction().id("4"));
        when(readOnlyDatasource.getAllTransactions()).thenReturn(transactions);

        new FromPgToEsSyncService(readOnlyDatasource, aggregateDatasource, mapper).syncDatabase();

        verify(aggregateDatasource, times(1)).resetData();
        verify(mapper, times(4)).transactionFromRest(any());
        verify(readOnlyDatasource, times(1)).getAllTransactions();
        verify(aggregateDatasource, times(1)).publishData(Mockito.any());

    }

}