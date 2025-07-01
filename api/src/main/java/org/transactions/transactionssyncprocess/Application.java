package org.transactions.transactionssyncprocess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.transactions.transactionssyncprocess.service.AppStartService;

@SpringBootApplication(scanBasePackages = "org.transactions", exclude = {ElasticsearchDataAutoConfiguration.class})
@EnableMongoRepositories("org.transactions.persistence.repositories")
@EnableElasticsearchRepositories("org.transactions.persistence.repositories")
@ConfigurationPropertiesScan("org.transactions.persistence.config")
public class Application {

	@Autowired
	AppStartService startService;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		SpringApplication.exit(context);
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner run() {
		return args -> startService.start();
	}
}
