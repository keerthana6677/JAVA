package Q7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ResultProcessor {
    private final ConcurrentHashMap<Integer, String> results =
            new ConcurrentHashMap<>();

    public void process(int taskCount, int poolSize) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        List<Future<String>> futures = new ArrayList<>();
        long start = System.nanoTime();

        for (int i = 0; i < taskCount; i++) {
            final int taskId = i;
            futures.add(pool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return processTask(taskId);
                }
            }));
        }

        int failures = 0;
        int timeouts = 0;
        for (int i = 0; i < futures.size(); i++) {
            try {
                String value = futures.get(i).get(2, TimeUnit.SECONDS);
                results.put(i, value);
            } catch (TimeoutException e) {
                futures.get(i).cancel(true);
                timeouts++;
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                System.err.println("Task " + i + " failed: "
                        + cause.getClass().getSimpleName() + ": " + cause.getMessage());
                failures++;
            }
        }

        pool.shutdown();
        if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
            List<Runnable> cancelled = pool.shutdownNow();
            System.err.println("Forced shutdown; remaining tasks: " + cancelled.size());
        }

        long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("threads=%d, tasks=%d, successes=%d, failures=%d"
                        + ", timeouts=%d, elapsed=%d ms%n",
                poolSize, taskCount, results.size(), failures,
                timeouts, elapsedMillis);
    }

    private String processTask(int taskId) throws Exception {
        if (taskId % 17 == 0) {
            throw new IllegalStateException("Simulated processor fault");
        }
        if (taskId % 31 == 0) {
            Thread.sleep(10);
        }
        int work = 1;
        for (int i = 1; i <= 200; i++) {
            work = (work * 31 + i + taskId) & 0xFFFF;
        }
        return "Task-" + taskId + "-value-" + work;
    }

    public static void main(String[] args) throws InterruptedException {
        final int taskCount = 800;
        int[] poolSizes = {1, 2, 4, 8};
        for (int poolSize : poolSizes) {
            ResultProcessor processor = new ResultProcessor();
            processor.process(taskCount, poolSize);
        }
    }
}
