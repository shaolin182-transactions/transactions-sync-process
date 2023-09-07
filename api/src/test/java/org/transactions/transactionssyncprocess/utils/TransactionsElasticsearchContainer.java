package org.transactions.transactionssyncprocess.utils;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

public class TransactionsElasticsearchContainer extends ElasticsearchContainer {

    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:8.8.1";
    private static final String CLUSTER_NAME = "cluster.name";
    private static final String ELASTIC_SEARCH = "elasticsearch";
    private static final String DISCOVERY_TYPE = "discovery.type";
    private static final String DISCOVERY_TYPE_SINGLE_NODE = "single-node";
    private static final String XPACK_SECURITY_ENABLED = "xpack.security.enabled";

    public TransactionsElasticsearchContainer() {
        super(ELASTICSEARCH_IMAGE);
        addFixedExposedPort(9200, 9200);
        addEnv(DISCOVERY_TYPE, DISCOVERY_TYPE_SINGLE_NODE);
        addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
        addEnv(XPACK_SECURITY_ENABLED, Boolean.FALSE.toString());
    }
}
