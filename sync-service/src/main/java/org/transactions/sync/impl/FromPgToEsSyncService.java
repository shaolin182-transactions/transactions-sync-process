package org.transactions.sync.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.transactions.sync.ISyncService;
import org.transactions.sync.connector.ITransactionsAggregateDatasource;
import org.transactions.sync.mapper.TransactionMapper;
import org.transactions.transactionssyncprocess.clients.transactions.TransactionRestClient;

import java.util.List;

@Profile("pg-to-es")
@Service
public class FromPgToEsSyncService implements ISyncService {

    private final TransactionMapper mapper;
    private final ITransactionsAggregateDatasource aggregateDatasource;

    private final TransactionRestClient transactionRestClient;


    @Autowired
    public FromPgToEsSyncService(TransactionRestClient restClient, ITransactionsAggregateDatasource aggregateDatasource, TransactionMapper mapper){
        this.aggregateDatasource = aggregateDatasource;
        this.transactionRestClient = restClient;
        this.mapper = mapper;
    }

    /**
     * Get all data from datasource, convert them to correct format and publish them to ElasticSearch
     */
    @Override
    public void syncDatabase() {
        // Delete index from elasticSearch database
        aggregateDatasource.resetData();

        // Get data from postgres datasource
        List<org.transactions.clients.transactions.model.Transaction> transactions = transactionRestClient.getAllTransactions();
        var trToPublish = transactions.stream()
                .map(mapper::transactionFromRest)
                .toList();

        // publish data to elasticSearch datasource
        aggregateDatasource.publishData(trToPublish);
    }
}
