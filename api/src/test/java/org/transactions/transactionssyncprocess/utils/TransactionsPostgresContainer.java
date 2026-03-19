package org.transactions.transactionssyncprocess.utils;

import org.testcontainers.postgresql.PostgreSQLContainer;

public class TransactionsPostgresContainer extends PostgreSQLContainer {

    private static final String POSTGRES_IMAGE = "postgres:18-alpine";

    public TransactionsPostgresContainer() {
        super(POSTGRES_IMAGE);
        addEnv("POSTGRES_DB", "postgres");
        addEnv("POSTGRES_USER", "postgres");
        addEnv("POSTGRES_PASSWORD", "ytUuHsZoVbQ&Wj4^RciHDu#qYhQd&w5H");

        addFixedExposedPort(5433, 5432);
    }
}
