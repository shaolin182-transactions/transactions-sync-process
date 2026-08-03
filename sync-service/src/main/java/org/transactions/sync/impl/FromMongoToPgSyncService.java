package org.transactions.sync.impl;

import org.model.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.transactions.connector.ITransactionsReadOnlyDatasource;
import org.transactions.sync.ISyncService;
import org.transactions.sync.entities.MigrationData;
import org.transactions.sync.entities.MigrationHistory;
import org.transactions.sync.mapper.TransactionMapper;
import org.transactions.sync.repositories.MigrationDataRepository;
import org.transactions.sync.repositories.MigrationHistoryRepository;
import org.transactions.transactionssyncprocess.clients.transactions.TransactionRestClient;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("mongo-to-pg")
public class FromMongoToPgSyncService implements ISyncService {

    private static final Logger log = LoggerFactory.getLogger(FromMongoToPgSyncService.class);
    private final ITransactionsReadOnlyDatasource transactionsDatasource;

    private final TransactionRestClient restClient;
    private final TransactionMapper mapper;
    private final MigrationHistoryRepository historyRepo;
    private final MigrationDataRepository dataRepo;


    @Autowired
    public FromMongoToPgSyncService(@Qualifier("mongodbDatasource") ITransactionsReadOnlyDatasource readOnlyDatasource,
                                    TransactionRestClient restClient, TransactionMapper mapper, MigrationHistoryRepository historyRepo, MigrationDataRepository dataRepo){
        this.transactionsDatasource = readOnlyDatasource;
        this.restClient = restClient;
        this.mapper = mapper;
        this.historyRepo = historyRepo;
        this.dataRepo = dataRepo;
    }

    @Override
    public void syncDatabase() {

        // Init a MigrationHistory object
        var history = new MigrationHistory();
        history.setDate(OffsetDateTime.now(ZoneId.of("UTC")));

        var dataMigrated = new ArrayList<MigrationData>();

        // Get data from mongodb datasource
        List<Transaction> transactions = transactionsDatasource.getAllTransactions();

        for ( Transaction transaction : transactions){

            // Check if already migrated
            var alreadyMigrated = dataRepo.findByFromIdAndStatus(transaction.getId(), "DONE").isPresent();
            if (!alreadyMigrated){
                // Init a MigrationData Object
                var data = new MigrationData();
                data.setHistory(history);
                data.setFromIdRecord(transaction.getId());

                // Convert MongoDB data to Postgres format and publish to Postgres datasource
                try {
                    var restTransaction = mapper.transactionToRest(transaction);
                    var result = restClient.createTransaction(restTransaction);

                    data.setToIdRecord(result.getId());
                    data.setStatus("DONE");
                } catch (Exception e){
                    log.atError().setCause(e)
                            .log("Error while saving transaction with id {} to Postgres datasource", transaction.getId());
                    data.setStatus("ERROR");
                }

                dataMigrated.add(data);
            }
        }

        // Persist data
        history.setMigrationData(dataMigrated);
        history.setNbRecordMigrated(Long.valueOf(dataMigrated.size()));
        history.setDuration(OffsetDateTime.now(ZoneId.of("UTC")).toEpochSecond() - history.getDate().toEpochSecond());
        historyRepo.save(history);
    }
}
