package Q6;

import java.util.concurrent.CountDownLatch;

public class WalletTransfer {
    private final String name;
    private int balance;

    public WalletTransfer(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public synchronized void add(int amount) {
        balance += amount;
    }

    public synchronized void subtract(int amount) {
        balance -= amount;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public void transferDeadlock(WalletTransfer target, int amount) {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName()
                    + " locked " + name);
            Thread.yield();
            synchronized (target) {
                subtract(amount);
                target.add(amount);
            }
        }
    }

    public void transferFixed(WalletTransfer target, int amount) {
        WalletTransfer first = System.identityHashCode(this)
                <= System.identityHashCode(target) ? this : target;
        WalletTransfer second = first == this ? target : this;

        synchronized (first) {
            synchronized (second) {
                subtract(amount);
                target.add(amount);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WalletTransfer a = new WalletTransfer("A", 1000);
        WalletTransfer b = new WalletTransfer("B", 1000);
        CountDownLatch ready = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            ready.countDown();
            try {
                ready.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            a.transferDeadlock(b, 100);
        }, "A-to-B");
        Thread t2 = new Thread(() -> {
            ready.countDown();
            try {
                ready.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            b.transferDeadlock(a, 100);
        }, "B-to-A");

        t1.start();
        t2.start();
        t1.join(800);
        t2.join(800);
        System.out.println("Deadlock demonstration alive states: "
                + t1.getState() + ", " + t2.getState());
        if (t1.isAlive()) {
            t1.interrupt();
        }
        if (t2.isAlive()) {
            t2.interrupt();
        }
        t1.join();
        t2.join();

        t1 = new Thread(() -> a.transferFixed(b, 100), "Fixed-A-to-B");
        t2 = new Thread(() -> b.transferFixed(a, 100), "Fixed-B-to-A");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Fixed transfer balances: " + a.getBalance()
                + ", " + b.getBalance());
    }
}
