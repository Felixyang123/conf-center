package com.wly.config.core.config;

import com.wly.config.core.client.CacheRegistryClient;
import com.wly.config.core.client.HttpClient;
import com.wly.config.core.client.RegistryClient;
import com.wly.config.core.client.RegistryHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(RegistryClientProps.class)
public class RegistryClientConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient(RestTemplate restTemplate) {
        return new HttpClient(restTemplate);
    }

    @Bean
    public RegistryClient registryClient(HttpClient httpClient) {
        return new RegistryClient(httpClient);
    }

    @Bean
    public CacheRegistryClient cacheInstanceClient(RegistryClient registryClient, RegistryClientProps registryClientProps) {
        return new CacheRegistryClient(registryClientProps, registryClient);
    }

    @Bean
    public RegistryHelper registryHelper(CacheRegistryClient cacheRegistryClient, RegistryClient registryClient, RegistryClientProps props) {
        return new RegistryHelper(cacheRegistryClient, registryClient, props);
    }
}
