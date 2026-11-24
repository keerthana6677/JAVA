#include <stdio.h>
#include <pthread.h>
#include <time.h>

#define CAMERAS 64
#define WORKLOAD 1000000

typedef struct {
    int start;
    int end;
} Task;

void *process_cameras(void *arg)
{
    Task *task = (Task *)arg;
    volatile long result = 0;

    for (int camera = task->start; camera < task->end; camera++) {
        for (int i = 0; i < WORKLOAD; i++) {
            result += (camera + i) % 10;
        }
    }

    return NULL;
}

double run_test(int pool_size)
{
    pthread_t threads[16];
    Task tasks[16];

    int cameras_per_thread = CAMERAS / pool_size;
    int remaining = CAMERAS % pool_size;

    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    int current = 0;

    for (int i = 0; i < pool_size; i++) {
        int count = cameras_per_thread;

        if (i < remaining)
            count++;

        tasks[i].start = current;
        tasks[i].end = current + count;
        current += count;

        pthread_create(&threads[i], NULL, process_cameras, &tasks[i]);
    }

    for (int i = 0; i < pool_size; i++) {
        pthread_join(threads[i], NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &end);

    return (end.tv_sec - start.tv_sec) +
           (end.tv_nsec - start.tv_nsec) / 1000000000.0;
}

int main(void)
{
    double serial_time;
    struct timespec start, end;

    clock_gettime(CLOCK_MONOTONIC, &start);
    Task serial_task = {0, CAMERAS};
    process_cameras(&serial_task);
    clock_gettime(CLOCK_MONOTONIC, &end);

    serial_time = (end.tv_sec - start.tv_sec) +
                  (end.tv_nsec - start.tv_nsec) / 1000000000.0;

    printf("64 Camera Processing Simulation\n");
    printf("Serial Execution Time: %.4f seconds\n\n", serial_time);

    int pools[] = {4, 8, 16};

    for (int i = 0; i < 3; i++) {
        double parallel_time = run_test(pools[i]);
        double speedup = serial_time / parallel_time;

        printf("Thread Pool Size: %d\n", pools[i]);
        printf("Execution Time: %.4f seconds\n", parallel_time);
        printf("Speedup: %.2fx\n\n", speedup);
    }

    return 0;
}
