package org.transactions.sync.impl;

import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.transactions.connector.ITransactionsReadOnlyDatasource;
import org.transactions.sync.ISyncService;
import org.transactions.sync.connector.ITransactionsAggregateDatasource;

import java.util.List;

@Profile("mongo-to-es")
@Service
public class FromMongoToEsSyncService implements ISyncService {

    ITransactionsAggregateDatasource aggregateDatasource;

    ITransactionsReadOnlyDatasource transactionsDatasource;


    @Autowired
    public FromMongoToEsSyncService(ITransactionsReadOnlyDatasource readOnlyDatasource, ITransactionsAggregateDatasource aggregateDatasource){
        this.aggregateDatasource = aggregateDatasource;
        this.transactionsDatasource = readOnlyDatasource;
    }

    @Override
    public void syncDatabase() {
        // Delete index from elasticSearch database
        aggregateDatasource.resetData();

        // Get data from mongodb datasource
        List<Transaction> transactions = transactionsDatasource.getAllTransactions();

        // publish data to elasticSearch datasource
        aggregateDatasource.publishData(transactions);
        System.out.println("DONE");
    }
}
