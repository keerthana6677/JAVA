package Q6;

import java.util.ArrayDeque;
import java.util.Queue;

public class TokenCounter {
    private final Queue<Integer> tokens = new ArrayDeque<>();
    private final int capacity;

    public TokenCounter(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public synchronized void produce(int token) throws InterruptedException {
        while (tokens.size() == capacity) {
            System.out.println("Producer waiting: buffer full");
            wait();
        }
        tokens.add(token);
        System.out.println("Produced " + token
                + "; buffered=" + tokens.size());
        notifyAll();
    }

    public synchronized int consume() throws InterruptedException {
        while (tokens.isEmpty()) {
            System.out.println("Consumer waiting: buffer empty");
            wait();
        }
        int token = tokens.remove();
        System.out.println("Consumed " + token
                + "; buffered=" + tokens.size());
        notifyAll();
        return token;
    }

    public synchronized int size() {
        return tokens.size();
    }

    public static void main(String[] args) throws InterruptedException {
        TokenCounter counter = new TokenCounter(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    counter.produce(i);
                    Thread.sleep(35);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    counter.consume();
                    Thread.sleep(55);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println("Final buffer size: " + counter.size());
    }
}
