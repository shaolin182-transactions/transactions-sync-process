package org.transactions.transactionssyncprocess.utils;

import org.testcontainers.containers.MongoDBContainer;

public class TransactionsMongoDbContainer extends MongoDBContainer {

    private static final String MONGODB_IMAGE = "mongo:6.0.24";

    public TransactionsMongoDbContainer() {
        super(MONGODB_IMAGE);
        addFixedExposedPort(27017, 27017);
    }
}
