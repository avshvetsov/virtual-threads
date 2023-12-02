package org.shvetsov.httpclientsapplication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class HttpClientService {

    public final String BLOCKING_CALL_URL;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final RestClient restClient;

    public HttpClientService(RestTemplateBuilder builder, WebClient.Builder webClientBuilder, RestClient.Builder restClientBuilder,
                             @Value("${http.blocking.host}") String host,
                             @Value("${http.blocking.port}") String port) {
        this.restTemplate = builder.build();
        this.webClient = webClientBuilder.build();
        this.restClient = restClientBuilder.build();
        this.BLOCKING_CALL_URL = "http://" + host + ":" + port + "/blocking";
    }

    public String restTemplateHandler() {
        String str = null;
        try {
            str = restTemplate.getForObject(BLOCKING_CALL_URL, String.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return str;
    }

    public Mono<String> webClientHandler() {
        Mono<String> str = null;
        try {
            str = webClient.get()
                    .uri(BLOCKING_CALL_URL)
                    .retrieve()
                    .bodyToMono(String.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return str;
    }

    public String restClientHandler() {
        String str = null;
        try {
            str = restClient.get()
                    .uri(BLOCKING_CALL_URL)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return str;
    }
}
