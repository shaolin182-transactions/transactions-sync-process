package org.transactions.transactionssyncprocess.clients.transactions;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.transactions.clients.transactions.model.BankAccount;
import org.transactions.clients.transactions.model.Category;
import org.transactions.clients.transactions.model.Transaction;
import org.transactions.clients.transactions.model.TransactionDetail;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
@AutoConfigureStubRunner(
        ids = "org.transactions:server:1.5.0-RC5:stubs",
        stubsMode = StubRunnerProperties.StubsMode.CLASSPATH,
        minPort = 8080, maxPort = 8080
)
class TransactionRestClientTest {

    @Autowired
    TransactionRestClient client;

    @RegisterExtension
    static WireMockExtension wmServer = WireMockExtension.newInstance()
            .options(
                    wireMockConfig().port(8084).httpsPort(8444)
                            .keystorePassword("password")
                            .keystoreType("PKCS12")
                            .keystorePath("src/test/resources/wiremock.p12")
            )
            .build();

    @Test
    void createTransaction() {

        wmServer.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/realms/transactions")).willReturn(
                WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "access_token": "token",
                                    "scope":"openid",
                                    "token_type": "bearer",
                                    "expires_in": 3600
                                }
                                """)
        ));

        var details = new TransactionDetail()
                .bankAccount(new BankAccount().id(1l).category("BK").label("BKLABEL"))
                .category(new Category().id(2l).category("CAT").label("catégorie label").type(Category.TypeEnum.COURANTE))
                .income(100f).outcome(10f);

        var transaction = new Transaction();
        transaction.setTransactions(List.of(details));
        transaction.setDate(OffsetDateTime.of(2026, 6, 1, 12, 0, 0, 0, ZoneOffset.UTC));

        var result = client.createTransaction(transaction);

        Assertions.assertAll(
                () -> Assertions.assertEquals(100f, result.getTransactions().get(0).getIncome()),
                () -> Assertions.assertEquals("CAT", result.getTransactions().get(0).getCategory().getCategory()),
                () -> Assertions.assertEquals("catégorie label", result.getTransactions().get(0).getCategory().getLabel()),
                () -> Assertions.assertEquals(2l, result.getTransactions().get(0).getCategory().getId()),
                () -> Assertions.assertEquals(Category.TypeEnum.COURANTE, result.getTransactions().get(0).getCategory().getType()),
                () -> Assertions.assertEquals("BK", result.getTransactions().get(0).getBankAccount().getCategory()),
                () -> Assertions.assertEquals("BKLABEL", result.getTransactions().get(0).getBankAccount().getLabel()),
                () -> Assertions.assertEquals(1l, result.getTransactions().get(0).getBankAccount().getId()),
                () -> Assertions.assertNotNull(result.getTransactions().get(0).getCost()),
                () -> Assertions.assertNotNull(result.getTransactions().get(0).getCostAbs()),
                () -> Assertions.assertNotNull(result.getCost()),
                () -> Assertions.assertNotNull(result.getCostAbs())

        );


    }

}