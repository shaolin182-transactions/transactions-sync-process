package org.transactions.persistence.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;


@Configuration
public class ElasticSearchClientConfig extends ElasticsearchConfiguration {

    @Autowired
    ElasticSearchDatabaseConfig esConfig;

    @Override
    public ClientConfiguration clientConfiguration() {

        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.add("Accept", "application/vnd.elasticsearch+json;compatible-with=8");
        defaultHeaders.add("Content-Type", "application/vnd.elasticsearch+json;compatible-with=8");

        return ClientConfiguration.builder()
                .connectedTo(esConfig.getHostname())
                .withDefaultHeaders(defaultHeaders)
                .build();
    }
}
