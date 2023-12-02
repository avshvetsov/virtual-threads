package org.shvetsov.httpclientsapplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class HttpClientController {

    private final HttpClientService service;

    public HttpClientController(HttpClientService service) {
        this.service = service;
    }

    @GetMapping("/rest-template")
    public String restTemplateChallenge() {
        return service.restTemplateHandler();
    }

    @GetMapping("/web-client")
    public Mono<String> webClientChallenge() {
        return service.webClientHandler();
    }
    @GetMapping("/rest-client")
    public String restClientChallenge() {
        return service.restClientHandler();
    }
}