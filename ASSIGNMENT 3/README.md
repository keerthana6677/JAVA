# Programming in Java - Assignment 3 - CO3

This package contains one Java 17+ implementation for each question Q1 through Q7. The student work is divided explicitly: **Student A** owns design, hierarchy, diagnostics, and synchronization analysis; **Student B** owns scenarios, evidence, experiments, and performance interpretation. Every deliverable includes joint or shared analysis where requested.

## Project structure

```text
CSA0914-192411144-ASSIGNMENT3/
├── README.md
├── Q1/
│   └── ATMSystem.java
├── Q2/
│   └── StorageDemo.java
├── Q3/
│   ├── SalesReportGenerator.java
│   └── sales.csv
├── Q4/
│   └── ParallelScanner.java
├── Q5/
│   └── SeatBooking.java
├── Q6/
│   ├── WalletTransfer.java
│   └── TokenCounter.java
└── Q7/
    └── ResultProcessor.java
```

All sources declare package names matching their folders. Compile from the assignment root:

```bat
javac Q1\ATMSystem.java Q2\StorageDemo.java Q3\SalesReportGenerator.java Q4\ParallelScanner.java Q5\SeatBooking.java Q6\WalletTransfer.java Q6\TokenCounter.java Q7\ResultProcessor.java
```

Run examples:

```bat
java Q1.ATMSystem
java Q2.StorageDemo
java Q3.SalesReportGenerator
java Q4.ParallelScanner
java Q5.SeatBooking
java Q6.WalletTransfer
java Q6.TokenCounter
java Q7.ResultProcessor
```

Q3 should be run from the assignment root because `sales.csv` is read from `Q3\sales.csv`.

---

# Q1 - Bank ATM Simulation (6 Marks)

**Source:** [ATMSystem.java](Q1/ATMSystem.java)

## Student A responsibilities

### Throwable hierarchy

```text
Throwable
├── Error
│   └── serious JVM errors (for example OutOfMemoryError)
└── Exception
    ├── RuntimeException
    │   └── NumberFormatException
    └── checked application exceptions
        ├── InvalidAmountException
        └── InsufficientFundsException
```

`InvalidAmountException` and `InsufficientFundsException` extend `Exception`, so callers must handle or declare them. `NumberFormatException` extends `RuntimeException`.

### Failure mapping

| Condition | Java object | User-facing code | Recovery |
|---|---|---|---|
| Blank or non-numeric amount | `NumberFormatException` | `ERROR_ATM_001` | Prompt again |
| Withdrawal exceeds balance | `InsufficientFundsException` | `ERROR_ATM_002` | Keep balance unchanged |
| Amount is zero or negative | `InvalidAmountException` | `ERROR_ATM_003` | Keep balance unchanged |
| Other menu/runtime problem | `RuntimeException` | `ERROR_ATM_004` | Prompt again |

### Catch ordering

Specific checked exceptions are caught before `RuntimeException`. `NumberFormatException` is a `RuntimeException` subclass but is listed first because it represents a separate, likely and useful input failure. The broad `RuntimeException` catch is last; no catch of `Exception` can catch `Error` and it would hide programming bugs.

## Student B responsibilities

### Five test scenarios

| Test | Input | Expected behavior |
|---|---|---|
| Valid deposit | `250.50` | Balance increases to `1250.50` |
| Valid withdrawal | `300` | Balance decreases to `700.00` |
| Non-numeric text | `abc` | `NumberFormatException`, code `ATM_001` |
| Insufficient funds | `2000` | `InsufficientFundsException`, code `ATM_002` |
| Negative amount | `-50` | `InvalidAmountException`, code `ATM_003` |

### Sample stack trace explanation

```text
java.lang.NumberFormatException: For input string: "abc"
 at java.lang.Integer.parseInt...
 at java.lang.Integer.parse... (JDK)
 at Q1.ATMSystem.main(ATMSystem.java:...)
```

The first line identifies exception type and cause text. Frames descend from JDK parsing into `ATMSystem`, with the final application frame identifying the location that called `parseAmount`; catching the exception prevents the stack trace from terminating the ATM unless no handler is present.

## Joint analysis

One `catch (Exception e)` can process all ordinary failures but loses type-specific messages and recovery. Specific blocks are clearer and allow exact codes. Catching `Exception` instead of individual checked exceptions is convenient only for non-critical logging or at a boundary. The ATM catches specific failures and retains only a final runtime catch.

## Alternative concept and complexity

**Alternative:** represent failures with integer result codes. An operation then returns status plus an optional message/balance. This may avoid checked exception objects but requires every caller to interpret codes and cannot retain automatic stack traces or exception cause chains.

For n ATM operations, validation and balance updates are O(n) and each operation is O(1); space is O(1). Stack-trace creation is expensive but exception-free normal execution is typically O(1).

---

# Q2 - Online Examination Portal (6 Marks)

**Source:** [StorageDemo.java](Q2/StorageDemo.java)

## Student A responsibilities

### Text class diagram

```text
SubmissionStorageException
  - extends: Exception
  - constructors: SubmissionStorageException(String, Throwable)
  - inherited: getCause(), getMessage(), printStackTrace()

StorageDemo
  + saveSubmission(Path, String): void
  + main(String[]): void

StorageDemo.saveSubmission --> Files.newBufferedWriter
Files.newBufferedWriter --> IOException
StorageDemo.saveSubmission catches IOException
StorageDemo.saveSubmission throws SubmissionStorageException(cause=IOException)
```

### Propagation path

`main` calls `saveSubmission`. If file storage throws `IOException`, the `catch` block in `saveSubmission` creates `SubmissionStorageException`, supplies the original `IOException` as the constructor cause, and throws the wrapper. `main` catches the outer checked exception and calls `getCause()` to verify and print the root condition. The try-with-resources closes the writer before the wrapper is created.

## Student B responsibilities

### Chaining evidence

`SubmissionStorageException` is created with `new SubmissionStorageException(message, cause)`. `main` calls `getCause()` and checks `cause instanceof IOException`. The console prints `Chaining confirmed: IOException is the cause`. `e.printStackTrace()` prints both the outer wrapper and the `"Caused by: java.io.IOException"` section.

To demonstrate the cause interactively, use a path that cannot be created, for example a directory path that is already an existing directory, or run with restricted filesystem permissions.

## Alternative concept and complexity

**Alternative:** an unchecked `SubmissionStorageException` extends `RuntimeException`; it requires no declaration but can be silently ignored.

Checked exceptions make storage obligations visible to callers and perform propagation with O(1) wrapper construction. Stack trace capture costs memory and CPU, approximately O(d) with d stack frames, and each exception object retains its stack and cause chain.

---

# Q3 - Daily Sales Report (7 Marks)

**Source:** [SalesReportGenerator.java](Q3/SalesReportGenerator.java)

## Student A responsibilities

### Debugging notes on file leaks

1. A reader/writer opened before work is not automatically closed if an exception jumps past the method.
2. A single `close()` only closes one resource and will not guarantee the second closes.
3. Returning inside `try` can bypass cleanup unless `finally` is correct.
4. Closing after a write failure may report a close failure instead of the write failure unless carefully prioritized.
5. If a file is left open on Windows, deletion, rename, or subsequent opening may fail.

The manual version uses a `finally` block and closes writer first, then reader. The robust version uses Java 17 try-with-resources.

## Student B responsibilities

### Resource safety design

Try-with-resources closes resources in reverse declaration order: writer closes before reader. It preserves the original thrown exception and attaches `IOException` from close operations as suppressed exceptions. Suppressed exceptions are retrieved with `getSuppressed()`. This is safer than separate close calls because the compiler guarantees all declared resources receive close attempts.

Manual closing is supported for learning and legacy code but is verbose and easier to misorder.

## Alternative concept and complexity

**Alternative:** stream the input through `Files.lines`, but exceptions and early termination still need a try-with-resources-safe stream:

```java
try (Stream<String> lines = Files.lines(input)) { ... }
```

For n CSV records, parsing and writing are O(n); memory is O(1) per record because readers and writers are buffered. The number of open file handles is constant (O(1)), versus an implementation that opens one handle per record, which could be O(n).

---

# Q4 - Parallel Answer-Script Scanning (7 Marks)

**Source:** [ParallelScanner.java](Q4/ParallelScanner.java)

## Student A responsibilities

### Thread life cycle

```text
NEW
  |
  | start()
  v
RUNNABLE ----------------> TERMINATED
  |                           ^
  | wait/sleep/join target    | run() or uncaught error returns
  v
TIMED_WAITING/BLOCKED/WAITING
  |
  | timeout/notify/lock release
  v
RUNNABLE
```

Calling `start()` requests JVM scheduling and creates the correct lifecycle. Calling `run()` directly performs the method synchronously on the caller thread and does not transition through `NEW -> RUNNABLE`. The code prints evidence for both behaviors.

### Student A implementation evidence

The source uses a `Thread` subclass-style anonymous class for script workers, `Runnable` for the separate worker, `setPriority`, `sleep`, and `join`. `AtomicInteger` makes completion counting thread-safe.

## Student B responsibilities

### Priority experiment

Run `ParallelScanner` and record the completion order several times. Thread priority is a JVM/platform hint, not a guarantee. On a CPU with available cores, priority changes may not produce a measurable order difference. Repeated runs show only a tendency and can differ because OS scheduling, core count, and load dominate.

Suggested experiment table:

| Run | MIN worker first? | MAX worker first? | Final count |
|---|---|---|---|
| 1 | record | record | 4 |
| 2 | record | record | 4 |
| 3 | record | record | 4 |

### Daemon threads

The heartbeat thread is marked daemon. Daemons do not prevent JVM termination; the JVM exits once only daemon or already-exited non-daemon threads remain. The heartbeat is therefore not suitable for persistence or guaranteed cleanup.

## Alternative concept and complexity

**Alternative:** `ExecutorService` with a fixed pool and `Future` tasks.

For n scripts, manual thread creation is O(n) threads and memory, while a fixed executor creates O(p) threads for pool size p and O(n) queued tasks. Manual threads give explicit lifecycle control, but executor management simplifies rejection, shutdown, scheduling, reuse, and exception capture. More threads are not always faster: CPU-bound scanning saturates around the available CPU core count.

---

# Q5 - Cinema Seat-Booking System (6 Marks)

**Source:** [SeatBooking.java](Q5/SeatBooking.java)

## Student A responsibilities

### Race interleaving

Suppose two threads read `availableSeats = 1`:

1. Thread T1 reads `availableSeats`.
2. T1 observes it is greater than zero.
3. Thread T2 reads the same value `1`.
4. T2 observes it is greater than zero.
5. T1 decrements to `0`.
6. T2 decrements to `-1` in the unsafe implementation.
7. Both threads report success, although only one seat existed.

The fixed version places check-and-decrement inside `synchronized(seatLock)`, making the pair atomic relative to other instance threads.

## Student B responsibilities

### Performance comparison

Run `SeatBooking` with 100 seats and 10,000 buyer threads. Record:

| Version | Reported successful bookings | Available seats | Time |
|---|---:|---:|---:|
| Unsafe | May exceed 100 or become negative | - | runtime-dependent |
| Synchronized | exactly 100 | 0 | record JVM result |

The synchronization implementation serializes the critical section; it is slower but preserves correctness.

### Static synchronization

A static method can synchronize on `SeatBooking.class`, serializing all instances of `SeatBooking` in the JVM. It is appropriate only for shared static state. Instance locking is preferable here because separate cinemas must book concurrently.

## Alternative concept and complexity

**Alternative:** `AtomicInteger`/`AtomicBoolean` using compare-and-set, or `ReentrantLock`.

For n buyers and s seats, synchronized and lock-based check/decrement are O(n) total. Synchronized offers simple monitor semantics and waits. `ReentrantLock` supports timed acquisition, interruption, fair ordering, and try-lock. An atomic integer can implement lock-free compare-and-swap, but a loop must be carefully structured and can still starve under contention.

---

# Q6 - Deadlock & Inter-Thread Communication (7 Marks)

**Sources:** [WalletTransfer.java](Q6/WalletTransfer.java) and [TokenCounter.java](Q6/TokenCounter.java)

## Student A responsibilities

### Why `wait()` is in a loop

The producer/consumer code uses:

```java
while (tokens.isEmpty()) {
    wait();
}
```

It must loop because a wait can wake before the desired condition exists: another thread can consume newly produced data, notifications can be spurious in the broader specification or implementation, and lost timing windows are possible. Rechecking while holding the monitor prevents a consumer from consuming from an empty queue or a producer from adding to a full queue.

`notifyAll()` prevents waiting threads from remaining asleep after state changes. `notify()` is less robust because it may wake an unrelated wait class.

## Student B responsibilities

### Deadlock conditions

The wallet demonstration has all four necessary conditions:

| Condition | Evidence |
|---|---|
| Mutual Exclusion | Each `synchronized` wallet lock belongs exclusively to one thread |
| Hold and Wait | Thread A holds A while waiting for B; Thread B holds B while waiting for A |
| No Preemption | Neither thread relinquishes the first lock voluntarily |
| Circular Wait | A waits on B and B waits on A |

The fixed transfer orders locks by `System.identityHashCode`, preventing opposite lock acquisition orders. Another fix would use `tryLock` with explicit rollback, or perform transfers through a single account-registry lock.

## Alternative concept and complexity

**Alternative:** `ArrayBlockingQueue` handles capacity, blocking, and mutex/condition state internally.

Manual `wait`/`notifyAll` is O(1) for queue add/remove in this bounded queue and has O(n) for notifications to all waiting threads. `ArrayBlockingQueue` offers similar O(1) operations, FIFO guarantees, interruption support, and less low-level risk. Manual code is instructive but easier to deadlock or wake incorrectly.

---

# Q7 - Concurrent Fault-Tolerant Processor (10 Marks)

**Source:** [ResultProcessor.java](Q7/ResultProcessor.java)

## Stage 1 - Requirements and failure identification

| Requirement | Implementation | Failure handling |
|---|---|---|
| Concurrent processing | Fixed `ExecutorService` | Shutdown and interruption |
| Independent task result | `Callable<String>` | Wrapped in `Future` |
| Thread-safe result collection | `ConcurrentHashMap<Integer, String>` | One entry per task ID |
| Task exceptions | `ExecutionException` | Logged with task ID and cause |
| Late tasks | `Future.get(timeout)` | Cancel and count timeout |
| Shutdown | `shutdown()` and `shutdownNow()` fallback | No new work; wait up to 5 seconds |

## Stage 2 - Student A design

The processor submits 800 independent callables to a fixed pool. Each callable has a task ID, performs CPU work, and may throw a simulated `IllegalStateException`. A `Future` represents its eventual result or failure. Results are inserted into a `ConcurrentHashMap`; task ID is the key. The pool is shutdown after submission and the caller waits for completion.

The thread pool is fixed in size, so at most p callables execute simultaneously. Reused worker threads avoid repeated thread creation. `ConcurrentHashMap` supports concurrent writes without requiring a global `synchronized` block.

## Stage 3 - Student B implementation and measurements

Run:

```bat
java Q7.ResultProcessor
```

Example measurement table - values vary by machine:

| Threads | Successes | Failures | Timeouts | Measured time |
|---:|---:|---:|---:|---:|
| 1 | 752 | 48 | 0 | record |
| 2 | 752 | 48 | 0 | record |
| 4 | 752 | 48 | 0 | record |
| 8 | 752 | 48 | 0 | record |

The actual failure count is 48: task IDs 0 through 799 include the multiples of 17, from 0 to 799. The timeout count should normally be zero. Some machines vary under heavy load.

## Stage 4 - Pool-size defense and limitations

A pool size of approximately availableProcessors is suitable for CPU-bound work. More threads than cores may only increase context switching. I/O-bound tasks can justify larger pools, but then the size must be based on measured latency and downstream capacity. This implementation has no retry policy and does not preserve partial results after JVM failure. A real system should add retry limits, backpressure, metrics, and cancellation propagation.

## Alternative concept and complexity

**Alternative:** `ForkJoinPool`/`CompletableFuture` for recursive or composeable asynchronous work.

For n tasks and pool size p:

| Operation | Complexity |
|---|---|
| Task submission | O(n) |
| Concurrent map write | expected O(1) per successful task |
| Waiting for n futures serially | O(n) |
| Total memory | O(n) futures plus O(n) results |
| CPU time | O(n * workPerTask) |

Fixed pools provide predictable resources. Work stealing is efficient for divide-and-conquer tasks, and `CompletableFuture` supports declarative pipelines but can make debugging and resource lifecycle less explicit.

---

# Cross-Assignment Test Notes

1. Compile all sources from the project root.
2. Q3's input file must remain at `Q3/sales.csv`.
3. Q5 and Q6 include intentionally unsynchronized/deadlocking demonstrations. Run Q6 WalletTransfer in a bounded 800 ms demonstration and then observe the fixed transfer.
4. Q7 intentionally contains 48 failed tasks for `ExecutionException` handling; expected successes are 752.
5. Interrupts are restored with `Thread.currentThread().interrupt()` in catch blocks.
6. Pools and threads are shut down or joined before main returns.
