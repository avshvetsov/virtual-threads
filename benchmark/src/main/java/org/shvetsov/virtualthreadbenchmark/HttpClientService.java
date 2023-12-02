package org.shvetsov.virtualthreadbenchmark;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class HttpClientService {

    private final RestTemplate restTemplate;

    public HttpClientService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String httpClientExecutor(String url) {
        String str = null;
//        log.info(Thread.currentThread().getName() + Thread.currentThread().isVirtual());
        try {
            str = restTemplate.getForObject(url, String.class);
        } catch (ResourceAccessException e) {
            log.info(e.getMessage());
        }
        return str;
    }

}
