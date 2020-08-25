package org.transactions.transactionssyncprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.transactions")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
