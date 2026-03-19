package org.transactions.sync.impl;

import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.connector.ITransactionsReadOnlyDatasource;
import org.transactions.sync.ISyncService;

import java.util.List;

@Service
@Profile("mongo-to-pg")
public class FromMongoToPgSyncService implements ISyncService {

    private final ITransactionsReadOnlyDatasource transactionsDatasource;

    private final ITransactionDataSource targetDatasource;

    @Autowired
    public FromMongoToPgSyncService(@Qualifier("mongodbDatasource") ITransactionsReadOnlyDatasource readOnlyDatasource, @Qualifier("postgresDatasource") ITransactionDataSource targetDatasource){
        this.targetDatasource = targetDatasource;
        this.transactionsDatasource = readOnlyDatasource;
    }

    @Override
    public void syncDatabase() {

        // Get data from mongodb datasource
        List<Transaction> transactions = transactionsDatasource.getAllTransactions();

        // Convert MongoDB data to Postgres format and publish to Postgres datasource
        transactions.stream().forEach(item -> targetDatasource.saveTransactions(item));
    }
}
