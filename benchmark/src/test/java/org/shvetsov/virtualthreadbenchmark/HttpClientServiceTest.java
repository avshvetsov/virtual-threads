package org.shvetsov.virtualthreadbenchmark;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@State(Scope.Benchmark)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = VirtualThreadBenchmarkApplication.class)
@BenchmarkMode(Mode.SingleShotTime)
public class HttpClientServiceTest {

    private static String HTTP_CLIENTS_HOST;
    private static String HTTP_CLIENTS_PORT;
    private static HttpClientService httpClientService;


    @Autowired
    public void setUp(HttpClientService httpClientService,
                      @Value("${http.client.host}") String host,
                      @Value("${http.client.port}") String port
    ) {
        HttpClientServiceTest.httpClientService = httpClientService;
        HttpClientServiceTest.HTTP_CLIENTS_HOST = host;
        HttpClientServiceTest.HTTP_CLIENTS_PORT = port;
    }

    @ParameterizedTest
    @ValueSource(ints = {500})
    public void executeJmhRunner(int threads) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(1)
                .warmupTime(TimeValue.seconds(2))
                .measurementIterations(3)
                .measurementTime(TimeValue.seconds(2))
                .forks(0)
                .threads(threads)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.CSV)
                .result("virtual-thread-enabled-" + threads + ".csv")
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public String restTemplateBenchmark() {
        return httpClientService.httpClientExecutor("http://" + HTTP_CLIENTS_HOST + ":" + HTTP_CLIENTS_PORT + "/rest-template");
    }

    @Benchmark
    public String webClientBenchmark() {
        return httpClientService.httpClientExecutor("http://" + HTTP_CLIENTS_HOST + ":" + HTTP_CLIENTS_PORT + "/web-client");
    }

    @Benchmark
    public String restClientBenchmark() {
        return httpClientService.httpClientExecutor("http://" + HTTP_CLIENTS_HOST + ":" + HTTP_CLIENTS_PORT + "/rest-client");
    }
}