package com.yang.study.es.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class ElasticsearchRestClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${app.elasticsearch.cluster.hosts}")
    private String hosts;

    @Value("${app.elasticsearch.cluster.schema:http}")
    private String schema;

    @Value("${app.elasticsearch.cluster.user}")
    private String userName;

    @Value("${app.elasticsearch.cluster.password}")
    private String password;

    @Value("${app.elasticsearch.cluster.connectTimeout:1000}")
    private Integer connTimeout;

    @Value("${app.elasticsearch.cluster.socketTimeout:30000}")
    private Integer socketTimeout;

    @Value("${app.elasticsearch.cluster.connReqTimeout:3000}")
    private Integer connReqTimeout;

    @Value("${app.elasticsearch.cluster.maxConnTotal:30}")
    private Integer maxConnTotal;

    @Value("${app.elasticsearch.cluster.maxConnPerRoute:10}")
    private Integer maxConnPerRoute;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        List<HttpHost> httpHosts = new ArrayList<>();

        String[] nodes = hosts.split(",");
        for (String node : nodes) {
            String[] split = node.split(":");
            String host = split[0].trim();
            int port = Integer.parseInt(split[1].trim());
            httpHosts.add(new HttpHost(host, port, schema));
        }
        RestClientBuilder builder = RestClient.builder(httpHosts.toArray(new HttpHost[]{}));

        // final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

        // 异步httpclient的连接超时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connTimeout);
            requestConfigBuilder.setSocketTimeout(socketTimeout);
            requestConfigBuilder.setConnectionRequestTimeout(connReqTimeout);
            return requestConfigBuilder;
        });

        // 异步httpclient的连接数配置
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.setMaxConnTotal(maxConnTotal);
            httpAsyncClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
            // httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpAsyncClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }
}
