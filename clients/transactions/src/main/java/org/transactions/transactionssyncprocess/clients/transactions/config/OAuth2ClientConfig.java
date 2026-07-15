package org.transactions.transactionssyncprocess.clients.transactions.config;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.restclient.autoconfigure.RestClientSsl;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.RestClientClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Configuration
public class OAuth2ClientConfig {

    @ConfigurationProperties(prefix = "client.oauth2")
    @Bean("client-oauth2-config")
    public RestClientProperties restClientProperties(){
        return new RestClientProperties();
    }

    @Bean("oAuth2RestClient")
    public RestClient oauth2RestClient(RestClient.Builder builder, RestClientSsl ssl, @Qualifier("client-oauth2-config") RestClientProperties config){
         builder
            .configureMessageConverters(converters -> {
                converters.addCustomConverter(new FormHttpMessageConverter());
                converters.addCustomConverter(new OAuth2AccessTokenResponseHttpMessageConverter());
            })
            .defaultStatusHandler(new OAuth2ErrorResponseErrorHandler());

         if (ssl != null && StringUtils.hasText(config.getSslBundleName())){
             builder.apply(ssl.fromBundle(config.getSslBundleName()));
         }

        return builder.build();
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService, @Qualifier("oAuth2RestClient") RestClient restClient) {

        var tokenResponseClient = new RestClientClientCredentialsTokenResponseClient();
        tokenResponseClient.setRestClient(restClient);

        var provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials(clientCredentialsGrantBuilder -> clientCredentialsGrantBuilder.accessTokenResponseClient(tokenResponseClient))
                .build();

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2ClientCredentialsGrantRequest> tokenResponseClient(@Qualifier("oAuth2RestClient") RestClient restClient) {
        var tokenResponseClient = new RestClientClientCredentialsTokenResponseClient();
        tokenResponseClient.setRestClient(restClient);

        return tokenResponseClient;
    }
}
