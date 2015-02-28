package yose.benchmark;

import http.undertow.UndertowServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yose.http.Server;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static yose.YoseRoutes.yoseRoutes;
import static yose.http.routing.Router.routing;

public class UndertowBenchmark {

    private Server server;
    private Benchmark benchmark;

    @Before
    public void startYose() throws Exception {
        server = new UndertowServer(8080, routing(yoseRoutes()));
        server.start();
        benchmark = new Benchmark();
    }

    @After
    public void stopYose() throws IOException {
        server.stop();
    }

    @Test
    public void tenGetsShouldReturn() throws InterruptedException {
        int nThreads = 10;
        BenchmarkResult result = benchmark.parallelGet(nThreads);

        assertThat(result.successfullGets, equalTo(nThreads));

    }

    @Test
    public void aHundredGetsShouldReturn() throws InterruptedException {
        int nThreads = 100;
        BenchmarkResult result = benchmark.parallelGet(nThreads);

        assertThat(result.successfullGets, equalTo(nThreads));
    }
}