package org.transactions.transactionssyncprocess;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.transactions.transactionssyncprocess.service.AppStartService;
import org.transactions.transactionssyncprocess.utils.TransactionsMongoDbContainer;
import org.transactions.transactionssyncprocess.utils.TransactionsPostgresContainer;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "mongo-to-pg", "postgresql", "mongodb"})
@Tag("IntregrationTest")
@Disabled
class MongoToPostgresIntegrationTest {

    @Container
    private static final TransactionsPostgresContainer pgContainer = new TransactionsPostgresContainer();

    @Container
    private static final TransactionsMongoDbContainer mongoDbContainer = new TransactionsMongoDbContainer();

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private ObjectMapper mapper = new JsonMapper();

    @Autowired
    private MongoTemplate mongoTemplate;

    @RegisterExtension
    static WireMockExtension wmServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8080))
            .build();


    @Autowired
    AppStartService startService;

    @BeforeAll
    static void setUp() {
        pgContainer.start();
        mongoDbContainer.start();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(pgContainer.isRunning());
        emptyDb();
    }

    private void emptyDb() {
        mongoTemplate.dropCollection(Transaction.class);
    }


    @Test
    @Tag("IntegrationTest")
    @DisplayName("Records existing in mongo database should be exported into ES database")
    @Transactional
    void nominalCase() throws Exception {
        // Prepare Data - Insert Data into mongodb database

        // Get sample JSON file
        loadTransactionsInBDD();

        // Mock transactions api server
        wmServer.stubFor(WireMock.post("/transactions").willReturn(WireMock.aResponse().withStatus(200)));

        // Run sync service
        startService.start();

        // Assertions - Transactions REST client should have been called 10 times
        WireMock.verify(10, WireMock.postRequestedFor(WireMock.urlEqualTo("/transactions")));

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
        pgContainer.stop();
        mongoDbContainer.stop();
    }
}
