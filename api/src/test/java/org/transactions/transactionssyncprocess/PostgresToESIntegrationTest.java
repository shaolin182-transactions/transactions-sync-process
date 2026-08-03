package org.transactions.transactionssyncprocess;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.transactions.persistence.model.TransactionES;
import org.transactions.persistence.repositories.TransactionAggregateRepository;
import org.transactions.transactionssyncprocess.service.AppStartService;
import org.transactions.transactionssyncprocess.utils.TransactionsElasticsearchContainer;
import org.transactions.transactionssyncprocess.utils.TransactionsPostgresContainer;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "pg-to-es", "postgresql", "es"})
@Tag("IntregrationTest")
@Disabled
class PostgresToESIntegrationTest {

    @Container
    private static final TransactionsElasticsearchContainer esContainer = new TransactionsElasticsearchContainer();

    @Container
    private static final TransactionsPostgresContainer pgContainer = new TransactionsPostgresContainer();

    @Autowired
    private ElasticsearchOperations esTemplate;

    @Autowired
    private TransactionAggregateRepository aggregateRepository;

    @Autowired
    AppStartService startService;

    @BeforeAll
    static void setUp() {
        esContainer.start();
        pgContainer.start();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(esContainer.isRunning());
        assertTrue(pgContainer.isRunning());
        recreateIndex();
        emptyDb();
    }

    private void emptyDb() {
    }

    private void recreateIndex() {
        if (esTemplate.indexOps(TransactionES.class).exists()) {
            esTemplate.indexOps(TransactionES.class).delete();
            esTemplate.indexOps(TransactionES.class).create();
        }
    }

    @Test
    @Tag("IntegrationTest")
    @DisplayName("Records existing in mongo database should be exported into ES database")
    void nominalCase() {
        // Prepare Data - Insert Data into postgresql database

        // Run sync service
        startService.start();

        // Assertions - records must exist in ES database
        Iterable<TransactionES> result = aggregateRepository.findAll();
        Assertions.assertTrue(result.iterator().hasNext());

    }

    @AfterAll
    static void destroy() {
        esContainer.stop();
        pgContainer.stop();
    }
}
