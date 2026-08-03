package org.transactions.transactionssyncprocess.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("org.transactions.persistence.repositories")
@Profile("mongo-to-es")
public class MongoToESConfig {
}
