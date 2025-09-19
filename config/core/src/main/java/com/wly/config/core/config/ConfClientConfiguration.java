package com.wly.config.core.config;

import com.wly.config.core.client.*;
import com.wly.config.core.listener.ConfListenProcessor;
import com.wly.config.core.listener.ConfListener;
import com.wly.config.core.listener.ConfListenerRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties({ConfClientProps.class})
public class ConfClientConfiguration {
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
    public ConfClient confClient(HttpClient httpClient) {
        return new ConfClient(httpClient);
    }

    @Bean
    public ConfListenerRegistrar confListenerRegistrar(@Autowired(required = false) List<ConfListener> listeners) {
        return new ConfListenerRegistrar(listeners);
    }

    @Bean
    public CacheConfClient cacheConfClient(ConfClient confClient, ConfClientProps confClientProps, ConfListenerRegistrar confListenerRegistrar) {
        return new CacheConfClient(confClient, confClientProps, confListenerRegistrar);
    }

    @Bean
    public CacheConfRefreshJob cacheConfRefreshJob(CacheConfClient cacheConfClient) {
        return new CacheConfRefreshJob(cacheConfClient);
    }

    @Bean
    public ConfHelper confHelper() {
        return new ConfHelper();
    }

    @Bean
    @DependsOn("confHelper")
    public ConfListenProcessor confListenProcessor(ConfListenerRegistrar confListenerRegistrar, ApplicationContext applicationContext) {
        return new ConfListenProcessor(confListenerRegistrar, applicationContext);
    }
}
