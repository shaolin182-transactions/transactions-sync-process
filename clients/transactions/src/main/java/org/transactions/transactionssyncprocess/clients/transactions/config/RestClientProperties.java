package org.transactions.transactionssyncprocess.clients.transactions.config;

public class RestClientProperties {

    private String baseUrl;

    private String sslBundleName;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSslBundleName() {
        return sslBundleName;
    }

    public void setSslBundleName(String sslBundleName) {
        this.sslBundleName = sslBundleName;
    }
}
