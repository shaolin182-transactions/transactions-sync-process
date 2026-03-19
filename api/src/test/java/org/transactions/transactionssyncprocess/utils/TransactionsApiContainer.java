package org.transactions.transactionssyncprocess.utils;

import org.testcontainers.containers.GenericContainer;

/**
 * Container for starting our transaction App
 */
public class TransactionsApiContainer extends GenericContainer {

    private static final String TRANSACTION_API_CONTAINER = "docker.io/jugirard/transactions-java-server:1.4.3";

    public TransactionsApiContainer() {
        super(TRANSACTION_API_CONTAINER);
        addFixedExposedPort(8090, 8080);
    }
}
