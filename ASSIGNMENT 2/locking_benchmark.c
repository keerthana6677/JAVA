#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

#define THREADS 8
#define BOOKINGS 100000
#define TRAINS 4
#define COACHES 4
#define SEATS 50

int seats[TRAINS][COACHES][SEATS];

pthread_mutex_t global_lock;
pthread_mutex_t train_locks[TRAINS];
pthread_mutex_t coach_locks[TRAINS][COACHES];

int strategy;

void *book_tickets(void *arg)
{
    long id = (long)arg;
    unsigned int seed = (unsigned int)(id + 100);

    for (int i = 0; i < BOOKINGS; i++) {
        int train = rand_r(&seed) % TRAINS;
        int coach = rand_r(&seed) % COACHES;
        int seat = rand_r(&seed) % SEATS;

        if (strategy == 1)
            pthread_mutex_lock(&global_lock);
        else if (strategy == 2)
            pthread_mutex_lock(&train_locks[train]);
        else
            pthread_mutex_lock(&coach_locks[train][coach]);

        if (seats[train][coach][seat] == 0)
            seats[train][coach][seat] = 1;

        if (strategy == 1)
            pthread_mutex_unlock(&global_lock);
        else if (strategy == 2)
            pthread_mutex_unlock(&train_locks[train]);
        else
            pthread_mutex_unlock(&coach_locks[train][coach]);
    }

    return NULL;
}

void run_test(int selected_strategy, const char *name)
{
    pthread_t threads[THREADS];
    struct timespec start, end;

    strategy = selected_strategy;

    for (int t = 0; t < TRAINS; t++) {
        for (int c = 0; c < COACHES; c++) {
            for (int s = 0; s < SEATS; s++)
                seats[t][c][s] = 0;
        }
    }

    clock_gettime(CLOCK_MONOTONIC, &start);

    for (long i = 0; i < THREADS; i++)
        pthread_create(&threads[i], NULL, book_tickets, (void *)i);

    for (int i = 0; i < THREADS; i++)
        pthread_join(threads[i], NULL);

    clock_gettime(CLOCK_MONOTONIC, &end);

    double elapsed = (end.tv_sec - start.tv_sec) +
                     (end.tv_nsec - start.tv_nsec) / 1000000000.0;

    printf("%s: %.4f seconds, %.0f booking attempts/second\n",
           name, elapsed, (THREADS * (double)BOOKINGS) / elapsed);
}

int main(void)
{
    pthread_mutex_init(&global_lock, NULL);

    for (int t = 0; t < TRAINS; t++) {
        pthread_mutex_init(&train_locks[t], NULL);

        for (int c = 0; c < COACHES; c++)
            pthread_mutex_init(&coach_locks[t][c], NULL);
    }

    printf("Railway Ticket Booking Locking Benchmark\n");
    printf("Threads: %d, booking attempts per thread: %d\n\n",
           THREADS, BOOKINGS);

    run_test(1, "Global Lock");
    run_test(2, "Per-Train Lock");
    run_test(3, "Per-Coach Lock");

    pthread_mutex_destroy(&global_lock);

    for (int t = 0; t < TRAINS; t++) {
        pthread_mutex_destroy(&train_locks[t]);

        for (int c = 0; c < COACHES; c++)
            pthread_mutex_destroy(&coach_locks[t][c]);
    }

    return 0;
}
