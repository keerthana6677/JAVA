package Q5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatBooking {
    private int availableSeats;
    private final Object seatLock = new Object();

    public SeatBooking(int availableSeats) {
        if (availableSeats < 0) {
            throw new IllegalArgumentException("Seat count cannot be negative");
        }
        this.availableSeats = availableSeats;
    }

    public boolean bookRaceUnsafe() {
        if (availableSeats > 0) {
            Thread.yield();
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean bookSynchronized() {
        synchronized (seatLock) {
            if (availableSeats > 0) {
                availableSeats--;
                return true;
            }
            return false;
        }
    }

    public int getAvailableSeats() {
        synchronized (seatLock) {
            return availableSeats;
        }
    }

    private static int runUnsafe(int seatCount, int buyers)
            throws InterruptedException {
        SeatBooking booking = new SeatBooking(seatCount);
        CountDownLatch start = new CountDownLatch(1);
        Thread[] threads = new Thread[buyers];
        AtomicInteger successes = new AtomicInteger();
        for (int i = 0; i < buyers; i++) {
            threads[i] = new Thread(() -> {
                try {
                    start.await();
                    if (booking.bookRaceUnsafe()) {
                        successes.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads[i].start();
        }
        start.countDown();
        for (Thread thread : threads) {
            thread.join();
        }
        return successes.get();
    }

    private static int runFixed(int seatCount, int buyers)
            throws InterruptedException {
        SeatBooking booking = new SeatBooking(seatCount);
        CountDownLatch start = new CountDownLatch(1);
        Thread[] threads = new Thread[buyers];
        AtomicInteger successes = new AtomicInteger();
        for (int i = 0; i < buyers; i++) {
            threads[i] = new Thread(() -> {
                try {
                    start.await();
                    if (booking.bookSynchronized()) {
                        successes.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads[i].start();
        }
        start.countDown();
        for (Thread thread : threads) {
            thread.join();
        }
        return successes.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final int buyers = 10_000;
        long unsafeStart = System.nanoTime();
        int unsafeSuccesses = runUnsafe(100, buyers);
        long unsafeMillis = (System.nanoTime() - unsafeStart) / 1_000_000;

        long fixedStart = System.nanoTime();
        int fixedSuccesses = runFixed(100, buyers);
        long fixedMillis = (System.nanoTime() - fixedStart) / 1_000_000;

        System.out.println("Race-first: 100 seats, " + buyers + " buyers");
        System.out.println("Unsafe accepted bookings: " + unsafeSuccesses
                + ", time=" + unsafeMillis + " ms");
        System.out.println("Synchronized accepted bookings: " + fixedSuccesses
                + ", time=" + fixedMillis + " ms");
        System.out.println("Static synchronization alternative locks the Class object.");
    }
}
