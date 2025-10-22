package org.transactions.persistence;

import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.transactions.persistence.config.ElasticSearchDatabaseConfig;
import org.transactions.persistence.factories.TransactionESFactory;
import org.transactions.persistence.model.TransactionES;
import org.transactions.persistence.repositories.TransactionAggregateRepository;
import org.transactions.sync.connector.ITransactionsAggregateDatasource;

import java.util.List;

@Component
public class TransactionAggregateDatasource implements ITransactionsAggregateDatasource {

    private TransactionAggregateRepository repository;

    private ElasticsearchOperations clientES;

    private TransactionESFactory factory;

    private ElasticSearchDatabaseConfig esConfig;

    @Autowired
    public TransactionAggregateDatasource(TransactionESFactory factory, TransactionAggregateRepository repository, ElasticsearchOperations clientES, ElasticSearchDatabaseConfig esConfig){
        this.factory = factory;
        this.repository = repository;
        this.esConfig = esConfig;
        this.clientES = clientES;

    }

    @Override
    public void publishData(List<Transaction> transactions) {

        // Transform data from mongodb datasource into elastic search datasource format
        List<TransactionES> transactionsToPublish = factory.buildTransactionESList(transactions);

        // publish them to ES
        if (!clientES.indexOps(IndexCoordinates.of(esConfig.getIndex())).exists()){
            clientES.indexOps(IndexCoordinates.of(esConfig.getIndex())).create();
        }
        repository.saveAll(transactionsToPublish);

    }

    @Override
    public void resetData() {
        clientES.indexOps(IndexCoordinates.of(esConfig.getIndex())).delete();
    }
}
