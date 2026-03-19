package org.transactions.transactionssyncprocess.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("org.transactions.persistence.pg.repositories")
@EntityScan("org.transactions.persistence.pg.entities")
@Profile("mongo-to-pg")
public class MongoToPGConfig {
}
