package com.yang.study.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.nacos.config")
public class TestConfigProperties {
    // server-addr: 127.0.0.1:8848
    //        file-extension: yaml
    //        namespace: 7302d4c3-e843-4db1-af23-777499a2b76a
    //        group: DEFAULT_GROUP
    //        ip: 192.168.1.7
    //        refresh-enabled: true
    //        enabled: true

    private String namespace;
    private String ip;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "TestConfigProperties{" +
                "namespace='" + namespace + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
