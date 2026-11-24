# A5 Transactions and SQL Injection

## Wallet transfer pseudocode
```text
BEGIN
  load Connection
  originalAutoCommit = connection.getAutoCommit()
  connection.setAutoCommit(false)
  TRY
    PreparedStatement debit for hostel wallet
    set hostel wallet ID and 500
    executeUpdate(debit)
    IF updatedRows != 1 THEN THROW Insufficient/InvalidAccount

    PreparedStatement credit for canteen wallet
    set canteen wallet ID and 500
    executeUpdate(credit)
    IF updatedRows != 1 THEN THROW InvalidCanteenAccount

    connection.commit()
  CATCH SQLException OR transfer validation exception
    connection.rollback()
  FINALLY
    connection.setAutoCommit(originalAutoCommit)
  END TRY
END
```
The second update succeeds logically only if the transaction commits; both changes become visible atomically.

## SQL injection
Unsafe:
```java
String sql = "SELECT * FROM users WHERE username='" + username
           + "' AND password='" + password + "'";
```
For input `anything' OR '1'='1`, the SQL can become a tautology and bypass authentication.

Safe:
```java
String sql = "SELECT COUNT(*) FROM users WHERE username=? AND password=?";
try (PreparedStatement ps = connection.prepareStatement(sql)) {
    ps.setString(1, username);
    ps.setString(2, password);
    try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getInt(1) > 0;
    }
}
```

One transaction is atomic: both wallet changes succeed or both are rolled back. Two independent auto-commit updates can leave only the debit or only the credit after a failure.
