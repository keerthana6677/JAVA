#include <stdio.h>

#define MAX_PROCESSES 1000
#define MAX_RESOURCES 3

int allocation[MAX_PROCESSES][MAX_RESOURCES];
int maximum[MAX_PROCESSES][MAX_RESOURCES];
int need[MAX_PROCESSES][MAX_RESOURCES];
int available[MAX_RESOURCES];

int processes, resources;

int safety_check(int sequence[])
{
    int work[MAX_RESOURCES];
    int finish[MAX_PROCESSES] = {0};
    int count = 0;

    for (int j = 0; j < resources; j++)
        work[j] = available[j];

    while (count < processes) {
        int found = 0;

        for (int i = 0; i < processes; i++) {
            if (!finish[i]) {
                int possible = 1;

                for (int j = 0; j < resources; j++) {
                    if (need[i][j] > work[j]) {
                        possible = 0;
                        break;
                    }
                }

                if (possible) {
                    for (int j = 0; j < resources; j++)
                        work[j] += allocation[i][j];

                    sequence[count++] = i;
                    finish[i] = 1;
                    found = 1;
                }
            }
        }

        if (!found)
            return 0;
    }

    return 1;
}

int request_resources(int process, int request[])
{
    for (int j = 0; j < resources; j++) {
        if (request[j] > need[process][j] ||
            request[j] > available[j]) {
            return 0;
        }
    }

    for (int j = 0; j < resources; j++) {
        available[j] -= request[j];
        allocation[process][j] += request[j];
        need[process][j] -= request[j];
    }

    int sequence[MAX_PROCESSES];

    if (safety_check(sequence))
        return 1;

    for (int j = 0; j < resources; j++) {
        available[j] += request[j];
        allocation[process][j] -= request[j];
        need[process][j] += request[j];
    }

    return 0;
}

int main(void)
{
    int sequence[MAX_PROCESSES];

    printf("Enter number of processes and resources: ");
    scanf("%d %d", &processes, &resources);

    if (processes < 1 || processes > MAX_PROCESSES ||
        resources < 1 || resources > MAX_RESOURCES) {
        printf("Invalid input size.\n");
        return 1;
    }

    printf("Enter Allocation Matrix:\n");
    for (int i = 0; i < processes; i++)
        for (int j = 0; j < resources; j++)
            scanf("%d", &allocation[i][j]);

    printf("Enter Maximum Matrix:\n");
    for (int i = 0; i < processes; i++)
        for (int j = 0; j < resources; j++)
            scanf("%d", &maximum[i][j]);

    printf("Enter Available Resources:\n");
    for (int j = 0; j < resources; j++)
        scanf("%d", &available[j]);

    for (int i = 0; i < processes; i++) {
        for (int j = 0; j < resources; j++) {
            if (allocation[i][j] < 0 ||
                maximum[i][j] < allocation[i][j] ||
                available[j] < 0) {
                printf("Invalid resource data.\n");
                return 1;
            }

            need[i][j] = maximum[i][j] - allocation[i][j];
        }
    }

    if (safety_check(sequence)) {
        printf("\nSystem is in SAFE state.\n");
        printf("Safe Sequence: ");

        for (int i = 0; i < processes; i++)
            printf("P%d ", sequence[i]);

        printf("\n");
    } else {
        printf("\nSystem is in UNSAFE state.\n");
    }

    int process;
    int request[MAX_RESOURCES];

    printf("\nEnter process number for resource request: ");
    scanf("%d", &process);

    if (process < 0 || process >= processes) {
        printf("Invalid process number.\n");
        return 1;
    }

    printf("Enter request values:\n");
    for (int j = 0; j < resources; j++)
        scanf("%d", &request[j]);

    if (request_resources(process, request))
        printf("Request GRANTED. System remains SAFE.\n");
    else
        printf("Request DENIED. System remains unchanged.\n");

    return 0;
}
