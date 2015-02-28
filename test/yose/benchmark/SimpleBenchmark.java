package yose.benchmark;

import http.simple.SimpleServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yose.http.HttpResponse;
import yose.http.Server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static yose.YoseRoutes.yoseRoutes;
import static yose.http.routing.Router.routing;
import static yose.testsupport.HttpGetRequest.get;

public class SimpleBenchmark {

    private Server server;
    private static int counter;

    @Before
    public void startYose() throws Exception {
        server = new SimpleServer(8080, routing(yoseRoutes()));
        server.start();
        counter = 0;
    }

    @After
    public void stopYose() throws IOException {
        server.stop();
    }

    @Test
    public void tenGetsShouldReturn() throws InterruptedException {
        int nThreads = 10;
        parallelGet(nThreads);

        assertThat(counter, equalTo(nThreads));
    }

    @Test
    public void aHundredGetsShouldReturn() throws InterruptedException {
        int nThreads = 100;
        parallelGet(nThreads);

        assertThat(counter, equalTo(nThreads));
    }

    private void parallelGet(int nThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Runnable worker = new EndpointHammer("http://localhost:8080/");
            executor.execute(worker);
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    public class EndpointHammer implements Runnable {
        private final String uri;

        EndpointHammer(String url) {
            this.uri = url;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            HttpResponse response = get(uri);
            long elapsedTime = System.currentTimeMillis() - startTime;

            System.out.print("[" + elapsedTime + "]");
            if (response.code == 200) {
                counter++;
            }
        }
    }
}