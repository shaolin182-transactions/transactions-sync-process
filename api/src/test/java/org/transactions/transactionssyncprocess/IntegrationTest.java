package org.transactions.transactionssyncprocess;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.transactions.persistence.model.TransactionES;
import org.transactions.persistence.repositories.TransactionAggregateRepository;
import org.transactions.transactionssyncprocess.service.AppStartService;
import org.transactions.transactionssyncprocess.utils.TransactionsElasticsearchContainer;
import org.transactions.transactionssyncprocess.utils.TransactionsMongoDbContainer;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

    @Container
    private static final TransactionsElasticsearchContainer esContainer = new TransactionsElasticsearchContainer();

    @Container
    private static final TransactionsMongoDbContainer mongoDbContainer = new TransactionsMongoDbContainer();

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ElasticsearchOperations esTemplate;

    @Autowired
    private TransactionAggregateRepository aggregateRepository;

    @Autowired
    AppStartService startService;

    @BeforeAll
    static void setUp() {
        esContainer.start();
        mongoDbContainer.start();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(esContainer.isRunning());
        recreateIndex();
        emptyDb();
    }

    private void emptyDb() {
        mongoTemplate.dropCollection(Transaction.class);
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
    void nominalCase() throws Exception {
        // Prepare Data - Insert Data into mongodb database

        // Get sample JSON file
        loadTransactionsInBDD();

        // Run sync service
        startService.start();

        // Assertions - records must exist in ES database
        Iterable<TransactionES> result = aggregateRepository.findAll();
        Assertions.assertTrue(result.iterator().hasNext());

    }

    private void loadTransactionsInBDD() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:data/sample_file.json");
        // Convert it into Transanction object
        List<Transaction> transactions = mapper.readValue(resource.getFile(), mapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));
        // Persist them into database
        transactions.forEach(item -> mongoTemplate.save(item, "transaction"));
    }

    @AfterAll
    static void destroy() {
        esContainer.stop();
        mongoDbContainer.stop();
    }
}
