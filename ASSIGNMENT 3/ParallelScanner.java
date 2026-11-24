package Q4;

import java.util.concurrent.atomic.AtomicInteger;

public class ParallelScanner {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger completed = new AtomicInteger();

        System.out.println("Version 1: Thread subclasses");
        Thread[] threadWorkers = new Thread[3];
        for (int i = 0; i < threadWorkers.length; i++) {
            final int scriptId = i + 1;
            threadWorkers[i] = new Thread(() -> scan(scriptId, completed)) {
                @Override
                public void run() {
                    System.out.println(getName() + " state before work: " + getState());
                    super.run();
                }
            };
            threadWorkers[i].setName("Script-Thread-" + scriptId);
            threadWorkers[i].setPriority(Thread.MIN_PRIORITY + i);
        }

        for (Thread worker : threadWorkers) {
            worker.start();
        }
        for (Thread worker : threadWorkers) {
            worker.join();
        }
        System.out.println("Joined Thread subclasses; completed scripts: " + completed);

        System.out.println("\nVersion 2: Runnable interface");
        completed.set(0);
        Thread runnableWorker = new Thread(new RunnableScanner(9, completed), "Runnable-Worker");
        runnableWorker.setPriority(Thread.MAX_PRIORITY);
        runnableWorker.start();
        runnableWorker.join();
        System.out.println("Runnable worker result: " + completed);

        System.out.println("\nDaemon experiment");
        Thread daemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(25);
                    System.out.println("daemon heartbeat");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Daemon-Heartbeat");
        daemon.setDaemon(true);
        daemon.start();
        Thread.sleep(100);

        System.out.println("\nEvidence that run() is not scheduling:");
        Thread direct = new Thread(() -> System.out.println("Code executed by direct run()"), "Direct-Run");
        direct.run();
        direct.start();
        direct.join();
    }

    private static void scan(int scriptId, AtomicInteger completed) {
        try {
            System.out.printf("%s scanning script %d (priority=%d)%n",
                    Thread.currentThread().getName(), scriptId,
                    Thread.currentThread().getPriority());
            Thread.sleep(80 + scriptId * 20L);
            completed.incrementAndGet();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static final class RunnableScanner implements Runnable {
        private final int scriptId;
        private final AtomicInteger completed;

        RunnableScanner(int scriptId, AtomicInteger completed) {
            this.scriptId = scriptId;
            this.completed = completed;
        }

        @Override
        public void run() {
            scan(scriptId, completed);
        }
    }
}
