package org.transactions.sync.repositories;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.transactions.sync.entities.MigrationData;
import org.transactions.sync.entities.MigrationHistory;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Testcontainers
@SpringBootTest
public class MigrationDataRepositoryTest {

    @Autowired
    MigrationHistoryRepository historyRepo;

    @Autowired
    MigrationDataRepository dataRepo;

    @Container
    static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:18-alpine"))
            .withDatabaseName("transactionsdb-sync-test")
            .withUsername("username")
            .withPassword("password");

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeAll
    static void startDb() {
        postgresContainer.start();
    }

    @AfterAll
    static void stopDb() {
        postgresContainer.stop();
    }

    @Test
    @Transactional
    @DisplayName("should interact with database correctly")
    void testDb() {

        var history = new MigrationHistory();
        history.setDate(OffsetDateTime.now());
        history.setDuration(123456789L);
        history.setStatus("DONE");
        history.setNbRecordMigrated(100L);

        var data = new MigrationData();
        data.setFromIdRecord("ID_1");
        data.setToIdRecord("ID_2");
        data.setStatus("DONE");
        data.setHistory(history);

        var datas = new ArrayList<MigrationData>();
        datas.add(data);


        history.setMigrationData(datas);

        historyRepo.save(history);


        Assertions.assertEquals(1, historyRepo.count());
        Assertions.assertEquals(1, dataRepo.count());

        historyRepo.deleteAll();

        Assertions.assertEquals(0, historyRepo.count());
        Assertions.assertEquals(0, dataRepo.count());
    }
}
