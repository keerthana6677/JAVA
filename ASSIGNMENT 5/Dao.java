package assignment5.a3;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T, K> {
    void insert(T item) throws SQLException;
    Optional<T> findById(K id) throws SQLException;
    List<T> findAll() throws SQLException;
    boolean update(T item) throws SQLException;
    boolean delete(K id) throws SQLException;
}
