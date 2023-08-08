package org.transactions.transactionssyncprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "org.transactions", exclude = {ElasticsearchDataAutoConfiguration.class})
@EnableMongoRepositories("org.transactions.persistence.repositories")
@EnableElasticsearchRepositories("org.transactions.persistence.repositories")
@ConfigurationPropertiesScan("org.transactions.persistence.config")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
