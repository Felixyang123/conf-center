package com.wly.config.core.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * HTTP客户端封装类
 */
@Slf4j
public record HttpClient(RestTemplate restTemplate) {

    /**
     * GET请求
     */
    public <T> T get(String url, Class<T> responseType) {
        return get(url, null, responseType, null);
    }

    public <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
        return get(url, headers, responseType, null);
    }

    public <T> T get(String url, Map<String, String> headers, Class<T> responseType,
                     Map<String, Object> uriVariables) {
        HttpEntity<?> entity = new HttpEntity<>(buildHeaders(headers));
        ResponseEntity<T> response = restTemplate.exchange(
                buildUrlWithParams(url, uriVariables),
                HttpMethod.GET,
                entity,
                responseType
        );
        return response.getBody();
    }

    public <T> T get(String url, Map<String, String> headers, ParameterizedTypeReference<T> responseType,
                     Map<String, Object> uriVariables) {
        String requestId = generateRequestId();
        logRequestStart(requestId, HttpMethod.GET, url, headers, null, uriVariables);
        try {
            HttpEntity<?> entity = new HttpEntity<>(buildHeaders(headers));
            ResponseEntity<T> response = restTemplate.exchange(
                    buildUrlWithParams(url, uriVariables),
                    HttpMethod.GET,
                    entity,
                    responseType
            );
            logRequestSuccess(requestId, response);
            return response.getBody();
        } catch (Exception e) {
            logRequestError(requestId, HttpMethod.GET, url, e);
            throw new HttpClientException("HTTP request failed: " + e.getMessage(), e);
        }
    }

    public <T> T get(String url, ParameterizedTypeReference<T> responseType) {
        return get(url, null, responseType, null);
    }

    /**
     * POST请求
     */
    public <T> T post(String url, Object requestBody, Class<T> responseType) {
        return post(url, null, requestBody, responseType);
    }

    public <T> T post(String url, Map<String, String> headers,
                      Object requestBody, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(requestBody, buildHeaders(headers));
        ResponseEntity<T> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, responseType
        );
        return response.getBody();
    }

    public <T> T post(String url, Object requestBody, ParameterizedTypeReference<T> responseType) {
        return post(url, null, requestBody, responseType);
    }

    public <T> T post(String url, Map<String, String> headers,
                      Object requestBody, ParameterizedTypeReference<T> responseType) {
        String requestId = generateRequestId();
        logRequestStart(requestId, HttpMethod.POST, url, headers, requestBody, null);
        try {
            HttpEntity<?> entity = new HttpEntity<>(requestBody, buildHeaders(headers));
            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, responseType
            );
            return response.getBody();
        } catch (Exception e) {
            logRequestError(requestId, HttpMethod.POST, url, e);
            throw new HttpClientException("HTTP request failed: " + e.getMessage(), e);
        }
    }

    /**
     * PUT请求
     */
    public <T> T put(String url, Object requestBody, Class<T> responseType) {
        return put(url, null, requestBody, responseType);
    }

    public <T> T put(String url, Map<String, String> headers,
                     Object requestBody, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(requestBody, buildHeaders(headers));
        ResponseEntity<T> response = restTemplate.exchange(
                url, HttpMethod.PUT, entity, responseType
        );
        return response.getBody();
    }

    /**
     * DELETE请求
     */
    public void delete(String url) {
        delete(url, null);
    }

    public void delete(String url, Map<String, String> headers) {
        HttpEntity<?> entity = new HttpEntity<>(buildHeaders(headers));
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    /**
     * 构建请求头
     */
    private HttpHeaders buildHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::add);
        }
        // 默认Content-Type
        if (!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return httpHeaders;
    }

    /**
     * 构建带参数的URL
     */
    private String buildUrlWithParams(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        params.forEach(builder::queryParam);
        return builder.build().toUriString();
    }

    private void logRequestStart(String requestId, HttpMethod method, String url,
                                 Map<String, String> headers, Object body, Map<String, Object> params) {
        if (log.isDebugEnabled()) {
            log.debug("Request {}: {} {} - Headers: {} - Params: {} - Body: {}",
                    requestId, method, url, headers, params, body);
        } else {
            log.info("Request {}: {} {}", requestId, method, url);
        }
    }

    private <T> void logRequestSuccess(String requestId, ResponseEntity<T> response) {
        if (log.isDebugEnabled()) {
            log.debug("Request {} completed - Status: {} - Body: {}",
                    requestId, response.getStatusCode(), response.getBody());
        } else {
            log.info("Request {} completed - Status: {}", requestId, response.getStatusCode());
        }
    }

    private void logRequestError(String requestId, HttpMethod method, String url, Exception e) {
        log.error("Request {} failed: {} {} - Error: {}",
                requestId, method, url, e.getMessage(), e);
    }

    public static class HttpClientException extends RuntimeException {
        public HttpClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private String generateRequestId() {
        return "req-" + System.currentTimeMillis() + "-" + Thread.currentThread().threadId();
    }
}