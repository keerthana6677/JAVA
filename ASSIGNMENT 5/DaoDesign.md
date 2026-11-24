# A3 DAO Design

## Generic interface
```java
public interface Dao<T, K> {
    void insert(T item) throws SQLException;
    Optional<T> findById(K id) throws SQLException;
    List<T> findAll() throws SQLException;
    boolean update(T item) throws SQLException;
    boolean delete(K id) throws SQLException;
}
```

## Object-table mapping
`Student(registerNumber, name, department)` maps one object to one row in `student(register_number PRIMARY KEY, name, department)`.
The primary key uniquely identifies a row and is the lookup value for `findById`, `update`, and `delete`.

## JDBC
Use `PreparedStatement` for every caller-controlled value. Obtain and close `Connection`, `PreparedStatement`, and `ResultSet` with try-with-resources. The DAO translates SQL rows into Java objects and keeps JDBC details away from GUI/service code.

## Direct SQL comparison
SQL embedded throughout GUI or service classes duplicates mapping, credentials, resource handling, and error handling. A DAO centralizes these concerns and improves testability. The DAO is not needed for every tiny application, but it is appropriate for maintainable persistence code.
