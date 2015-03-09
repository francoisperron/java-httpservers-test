package application.testsupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Benchmark {

    private List<Getter> getters = new ArrayList<>();

    public BenchmarkResult parallelGet(int nThreads) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        runBenchmark(nThreads);
        BenchmarkResult result = buildResult(startTime);
        printResult(result);
        return result;
    }

    private void runBenchmark(int nThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Getter getter = new Getter("http://localhost:8080/");
            getters.add(getter);
            executor.execute(getter);
        }
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    private BenchmarkResult buildResult(long startTime) {
        BenchmarkResult result = new BenchmarkResult();
        result.elapsedTime = System.currentTimeMillis() - startTime;
        result.successfulGets = (int) getters.stream().mapToInt(g -> g.responseCode).filter(code -> code == 200).count();
        result.individualTestsElapsedTime = getters.stream().map(g -> Long.toString(g.elapsedTime)).collect(Collectors.joining(", "));
        return result;
    }

    private void printResult(BenchmarkResult result) {
        System.out.println("\nTest elapsed time = " + result.elapsedTime);
        System.out.println(result.individualTestsElapsedTime);
    }
}
