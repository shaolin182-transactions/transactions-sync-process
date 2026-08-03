package org.transactions.transactionssyncprocess.clients.transactions.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.transactions.clients.transactions.ApiClient;
import org.transactions.clients.transactions.api.TransactionsApi;

@Configuration
public class TransactionRestClientConfig {

    @ConfigurationProperties(prefix = "client.transactions")
    @Bean("clientTransactionsConfig")
    public RestClientProperties restClientProperties(){
        return new RestClientProperties();
    }

    @Bean
    public TransactionsApi transactionsApi(ApiClient client) {
        return new TransactionsApi(client);
    }

    @Bean
    public ApiClient apiClient(@Qualifier("restClientTransaction") RestClient restClient, @Qualifier("clientTransactionsConfig") RestClientProperties clientProperties) {
        var client = new ApiClient(restClient);
        client.setBasePath(clientProperties.getBaseUrl());
        return client;
    }

    @Bean("restClientTransaction")
    public RestClient restClient(RestClient.Builder builder, OAuth2ClientHttpRequestInterceptor oAuth2Interceptor){
        builder.requestInterceptor(oAuth2Interceptor);

        return builder.build();
    }

    @Bean
    public OAuth2ClientHttpRequestInterceptor oauth2Interceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        var oAuth2ClientHttpRequestInterceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        oAuth2ClientHttpRequestInterceptor.setClientRegistrationIdResolver(request -> "keycloak");
        return oAuth2ClientHttpRequestInterceptor;
    }
}
