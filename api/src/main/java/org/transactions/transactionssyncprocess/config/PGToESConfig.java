package org.transactions.transactionssyncprocess.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("org.transactions.sync.repositories")
@EntityScan("org.transactions.sync.entities")
@EnableElasticsearchRepositories("org.transactions.persistence.repositories")
@Profile("pg-to-es")
public class PGToESConfig {
}
