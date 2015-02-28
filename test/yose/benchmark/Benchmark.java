package yose.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static yose.testsupport.HttpGetRequest.get;

public class Benchmark {

    private List<Getter> getters = new ArrayList<>();

    public BenchmarkResult parallelGet(int nThreads) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Getter getter = new Getter("http://localhost:8080/");
            getters.add(getter);
            executor.execute(getter);
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        BenchmarkResult result = new BenchmarkResult();
        result.elapsedTime = System.currentTimeMillis() - startTime;
        result.successfullGets = (int) getters.stream().mapToInt(g -> g.responseCode).filter(code -> code == 200).count();

        System.out.println("\nTest elapsed time = " + result.elapsedTime);
        String getsTime = getters.stream()
                .map(g -> Long.toString(g.elapsedTime))
                .collect(Collectors.joining(", "));
        System.out.println(getsTime);

        return result;
    }

    public class Getter implements Runnable {
        private final String uri;
        public int responseCode;
        public long elapsedTime;
        public Exception exception;

        Getter(String url) {
            this.uri = url;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            try {
                responseCode = get(uri).code;
            } catch (Exception e) {
                exception = e;
            }
            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.print(".");
        }
    }
}
