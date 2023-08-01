package org.transactions.persistence.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.model.transactions.Transaction;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.transactions.persistence.model.TransactionES;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@Disabled
class TransactionESFactoryTest {

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private static ObjectMapper mapper = new ObjectMapper();

    @ParameterizedTest(name = "Test Factory {2}")
    @MethodSource(value = "getTransactionsDataset")
    void buildTransactionESListTest(Resource inputFile, Resource expectedResult, Integer expectedSize) throws IOException {

        List<Transaction> transactions = mapper.readValue(inputFile.getFile(), mapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));
        List<TransactionES> expected = mapper.readValue(expectedResult.getFile(), mapper.getTypeFactory().constructCollectionType(List.class, TransactionES.class));

        // Method to test
        List<TransactionES> result = new TransactionESFactory().buildTransactionESList(transactions);

        // Assert data is fine
        assertThat("Check size list", result, hasSize(expectedSize));
        assertThat("Check content list, ignoring order", result, containsInAnyOrder(expected.toArray()));
    }

    private static Stream<Arguments> getTransactionsDataset() {

        Resource simpleTransactionFile = resourceLoader.getResource("classpath:data/transactions/list_simple_transactions.json");
        Resource multiTransactionFile = resourceLoader.getResource("classpath:data/transactions/list_multi_transactions.json");
        Resource simpleResultFile = resourceLoader.getResource("classpath:data/transactions-es/list_simple_transactions.json");
        Resource multiResultFile = resourceLoader.getResource("classpath:data/transactions-es/list_multi_transactions.json");

        return Stream.of(
          Arguments.of(simpleTransactionFile, simpleResultFile, 4),
          Arguments.of(multiTransactionFile, multiResultFile, 7)
        );
    }
}