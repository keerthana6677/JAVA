# Task 06 – Deadlock Handling and Banker's Algorithm

## 1. Introduction

A deadlock occurs when two or more processes wait indefinitely for resources held by one another. Deadlock handling is an important responsibility of an operating system.

## 2. Necessary Conditions for Deadlock

Four conditions must hold simultaneously for a deadlock to occur:

1. **Mutual Exclusion:** A resource can be used by only one process at a time.
2. **Hold and Wait:** A process holds one resource while waiting for another.
3. **No Preemption:** Resources cannot be forcibly taken away from a process.
4. **Circular Wait:** A circular chain exists in which each process waits for a resource held by the next process.

## 3. Resource Allocation Graph

A Resource Allocation Graph represents processes and resources.

- Circles represent processes.
- Squares represent resources.
- A request edge represents a process waiting for a resource.
- An allocation edge represents a resource assigned to a process.

A cycle in the graph indicates a possible deadlock. For a single instance of each resource type, a cycle indicates deadlock.

## 4. Banker's Algorithm

Banker's Algorithm is a deadlock avoidance technique. It checks whether granting a resource request keeps the system in a safe state.

The program uses:

- **Allocation:** Resources currently allocated to each process.
- **Maximum:** Maximum resources required by each process.
- **Need:** Remaining resources required by each process.
- **Available:** Resources currently available.

Need is calculated as:

`Need = Maximum - Allocation`

The safety algorithm checks whether all processes can finish in some order without causing deadlock.

## 5. Compilation and Execution

**Compilation command:**

```bash
gcc -std=c11 -Wall -Wextra bankers_algorithm.c -o bankers_algorithm
```

**Execution command:**

```bash
./bankers_algorithm
```

## 6. Test Case 1 – Safe State and Granted Request

**Input:**

```text
Processes and Resources: 5 3

Allocation Matrix:
0 1 0
2 0 0
3 0 2
2 1 1
0 0 2

Maximum Matrix:
7 5 3
3 2 2
9 0 2
2 2 2
4 3 3

Available Resources:
3 3 2

Process Number: 1
Request: 1 0 2
```

**Output:**

```text
System is in SAFE state.
Safe Sequence: P1 P3 P4 P0 P2
Request GRANTED. System remains SAFE.
```

## 7. Test Case 2 – Denied Request

The program was executed again using the same initial allocation, maximum and available resource values.

**Request:**

```text
Process Number: 0
Request: 0 0 3
```

**Output:**

```text
System is in SAFE state.
Safe Sequence: P1 P3 P4 P0 P2
Request DENIED. System remains unchanged.
```

The request was denied because it exceeded the available amount of the third resource.

## 8. Result

Banker's Algorithm was successfully implemented and tested. The program identified a safe sequence, granted a request that preserved system safety, and denied a request that exceeded available resources.

## 9. Conclusion

This task demonstrates deadlock conditions, Resource Allocation Graphs and Banker's Algorithm. The implementation helps prevent unsafe resource allocation by checking system safety before granting requests.
