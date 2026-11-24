# Assignment 4 – Event-Driven GUI Design

**Student:** Bhargavi N.  
**Register Number:** 192411144  
**Course:** Programming in Java (CSA09)  
**Topic:** Event-Driven GUI Design

## 1. Introduction
This report documents six Java Swing assignments covering GUI design, event sources and listeners, layout management, mouse and window events, and JDBC/MySQL integration. Each task is independent and is implemented as runnable Java source code.

## 2. Objectives
The work demonstrates separation of presentation and application logic, validation, exception handling, Swing painting, background database work, SQL safety, responsive layout, and maintainable architecture.

## 3. Software and Hardware
Java 26.0.2.1/JDK, Windows, VS Code, Java Swing/AWT, MySQL Connector/J, and MySQL are required for the full system. Maven configuration is included, although direct `javac` was used because Maven was unavailable.

## 4. Task 1 – Fee-Payment GUI Design Review
The poor design places every action behind one broad listener and mixes UI, validation, simulated payment, and database concerns. The correction uses `FeePaymentFrame`, `FeeService`, `FeeValidator`, `PaymentResult`, and `ReceiptService`. Separate lambda handlers isolate Pay, Reset, Receipt, and Exit actions.

String comparison is based on `equals`-style validation rather than `==`. Amount parsing is guarded. Database operations are absent because no real fee transaction is required. Design alternatives show separate listeners are clearer for distinct actions, `Action` objects improve reuse, and anonymous listeners are concise.

## 5. Task 2 – Hospital Appointment Desk
The event table includes department selection, doctor selection, date selection, booking, clearing, opening, and closing. `BorderLayout`, `GridLayout`, `JComboBox`, `JSpinner`, `JTextField`, and `JTextArea` form the containment. Department selection looks up a `Map` of doctor lists and refreshes the doctor combo. Validation and duplicate appointment checks happen before confirmation.

Loading all doctors once is O(1) per department lookup and O(d) for list rendering. Loading from a database each selection is generally slower and creates repeated I/O and connection overhead.

## 6. Task 3 – Library Search
`BorderLayout` divides top, centre, and bottom regions. `FlowLayout` keeps search controls and navigation controls usable. `JScrollPane` lets a `JTable` consume available space. Searching is O(n) and result storage is O(r). Pagination renders O(k) entries, where k is the page size.

## 7. Task 4 – Drawing Pad
The forensic defect is commonly caused by registering the wrong component/listener or naming a listener callback incorrectly. `@Override` exposes that problem at compile time. This solution stores `Point` objects and redraws them in `paintComponent()`, ensuring dots survive minimisation/restoring. `repaint()` schedules safe Swing painting.

## 8. Task 5 – Currency Converter
Four runnable classes implement frame-level `ActionListener`, named inner class, anonymous inner class, and lambda styles. Each supports button/Enter conversion and clear, displays errors and successful result dialogs, validates amounts, and uses `WindowAdapter`. Rates are demonstration constants.

## 9. Task 6 – Campus Event Registration
The system has a dashboard, student registration, committee login, committee dashboard, filtering, and CSV export. Presentation, service, and DAO classes are separate. `RegistrationDAO` uses parameterized SQL and try-with-resources. `RegistrationService` validates and calls the DAO. `SwingWorker` keeps database work away from the EDT.

Duplicate prevention is enforced in the UI and, critically, by `UNIQUE(student_id,event_id)`. Business validation rejects empty fields, malformed email/phone values, missing event selection, and duplicate requests.

## 10. Performance and Scalability
For n rows loaded at once, database work, transfer, memory, and table population are O(n). Pagination with `LIMIT p OFFSET o` moves only p rows and uses O(p) memory, but large offsets can require the database to scan and discard o rows. Keyset pagination using an indexed registration ID or composite key is preferable for deep pages.

## 11. Testing
Tasks 1–5 compile with Java 26. Task 6 compiles with the downloaded Connector/J. GUI and live database tests remain environment dependent because MySQL and an interactive desktop service were unavailable during verification.

| Test | Implementation | Environment Note |
|---|---|---|
| Java compilation, Tasks 1–5 | Passed | Verified with `javac` |
| Task 6 compilation | Passed | Verified with Connector/J |
| GUI launch | Not executed | Interactive desktop/MySQL unavailable |
| MySQL test cases | Not executed | Requires MySQL and credentials |

## 12. Advantages and Applications
The modular design supports reuse and testing, Swing event styles support many interaction requirements, and the DAO/service split reduces SQL and presentation coupling. Similar patterns apply to university fee systems, hospital scheduling, library systems, drawing tools, converters, and event management.

## 13. Conclusion
The project supplies complete source, SQL, documentation, class diagrams, and build instructions. Live GUI and database evidence must be captured on the target Windows machine with MySQL running.

## 14. References
- Oracle Java Platform documentation for Swing/AWT and JDBC.
- Oracle Java API documentation for SwingWorker and event dispatch thread.
- MySQL 8.0 documentation for Connector/J and constraints.
