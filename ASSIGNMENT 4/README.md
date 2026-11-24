# Task 6 – Campus Event Registration

## Problem Statement
Create a Swing/MySQL campus event-registration system with student and committee workflows.

## Layers
Presentation: Swing frames and dialogs. Service: validation and registration rules. DAO: PreparedStatements and JDBC.

## Key Classes
`Main`, `MainDashboard`, `StudentRegistrationFrame`, `CommitteeLoginFrame`, `CommitteeDashboard`, `RegistrationService`, `ValidationService`, `RegistrationDAO`, `DatabaseConnection`, `Registration`, `Event`, `CsvExporter`.

## Database
Execute `sql/campus_event_registration.sql`. Copy `config/database.properties.example` to `config/database.properties`, which is Git ignored. No password is committed.

## Compile and Run
```powershell
javac -cp lib\mysql-connector-j-9.4.0.jar -d bin (Get-ChildItem src\*.java | % FullName)
java -cp "bin;lib\mysql-connector-j-9.4.0.jar" Main
```

## Database Rules
The `UNIQUE(student_id,event_id)` constraint is the final duplicate-registration safeguard. `PreparedStatement` parameters prevent SQL injection. All JDBC resources use try-with-resources.

## Background Processing
Event loading, registration, filtering, login, and CSV export use `SwingWorker` so database work does not run directly on the EDT.

## Testing
Execute success, empty name, invalid email/phone, missing event, duplicate, cross-event, login, view/filter, export, database failure, and empty-results cases. Record actual pass/fail outcomes; no result should be claimed as passed until executed.

## Complexity
Registration validation is O(n) in field length. Indexed insertion/duplicate lookup is normally O(log n) or O(1) plus disk I/O. Loading all n rows is O(n) time and memory; paginated LIMIT/OFFSET loading is O(p) data for page size p but deep OFFSET can become expensive.
