package application.benchmark;

import application.testsupport.Benchmark;
import application.testsupport.BenchmarkResult;
import http.undertow.UndertowServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import application.http.Server;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static application.ApplicationRoutes.applicationRoutes;
import static application.http.routing.Router.routing;

public class UndertowBenchmark {

    private Server server;
    private Benchmark benchmark;

    @Before
    public void start() throws Exception {
        server = new UndertowServer(8080, routing(applicationRoutes()));
        server.start();
        benchmark = new Benchmark();
    }

    @After
    public void stop() throws IOException {
        server.stop();
    }

    @Test
    public void tenGetsShouldReturn() throws InterruptedException {
        int nThreads = 10;
        BenchmarkResult result = benchmark.parallelGet(nThreads);

        assertThat(result.successfulGets, equalTo(nThreads));

    }

    @Test
    public void aHundredGetsShouldReturn() throws InterruptedException {
        int nThreads = 100;
        BenchmarkResult result = benchmark.parallelGet(nThreads);

        assertThat(result.successfulGets, equalTo(nThreads));
    }
}