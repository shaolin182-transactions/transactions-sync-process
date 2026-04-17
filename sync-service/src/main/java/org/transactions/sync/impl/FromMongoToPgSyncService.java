package org.transactions.sync.impl;

import org.model.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.transactions.connector.ICommonDataDatasource;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.connector.ITransactionsReadOnlyDatasource;
import org.transactions.sync.ISyncService;

import java.util.List;

@Service
@Profile("mongo-to-pg")
public class FromMongoToPgSyncService implements ISyncService {

    private static final Logger log = LoggerFactory.getLogger(FromMongoToPgSyncService.class);
    private final ITransactionsReadOnlyDatasource transactionsDatasource;

    private final ITransactionDataSource targetDatasource;

    private final ICommonDataDatasource commonDataDatasource;

    @Autowired
    public FromMongoToPgSyncService(@Qualifier("mongodbDatasource") ITransactionsReadOnlyDatasource readOnlyDatasource,
                                    @Qualifier("postgresDatasource") ITransactionDataSource targetDatasource,
                                    @Qualifier("posgresCommonDatasource" ) ICommonDataDatasource commonDataDatasource){
        this.targetDatasource = targetDatasource;
        this.transactionsDatasource = readOnlyDatasource;
        this.commonDataDatasource = commonDataDatasource;
    }

    @Override
    public void syncDatabase() {

        // Get data from mongodb datasource
        List<Transaction> transactions = transactionsDatasource.getAllTransactions();

        // Extract bank accounts and persist them
        for ( Transaction transaction : transactions){
            for (var subTransaction : transaction.getTransactions()){
                commonDataDatasource.saveBankAccount(subTransaction.getBankAccount());
            }
        }

        // Extract categories and persist them
        for ( Transaction transaction : transactions){
            for (var subTransaction : transaction.getTransactions()){
                if (subTransaction.getCategory() != null){
                    commonDataDatasource.saveCategory(subTransaction.getCategory());
                }
            }
        }

        for ( Transaction transaction : transactions){
            // Convert MongoDB data to Postgres format and publish to Postgres datasource
            try {
                targetDatasource.saveTransactions(transaction);
            } catch (Exception e){
                log.atError().setCause(e)
                    .log("Error while saving transaction with id {} to Postgres datasource", transaction.getId());
            }
        }
    }
}
