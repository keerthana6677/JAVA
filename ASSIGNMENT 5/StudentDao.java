package assignment5.a3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentDao implements Dao<Student, String> {
    private final Connection connection;

    public StudentDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Student student) throws SQLException {
        String sql = "INSERT INTO student(register_number,name,department) VALUES (?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, student.registerNumber());
            ps.setString(2, student.name());
            ps.setString(3, student.department());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Student> findById(String id) throws SQLException {
        String sql = "SELECT register_number,name,department FROM student WHERE register_number=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT register_number,name,department FROM student")) {
            while (rs.next()) students.add(map(rs));
        }
        return students;
    }

    @Override
    public boolean update(Student student) throws SQLException {
        String sql = "UPDATE student SET name=?,department=? WHERE register_number=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, student.name());
            ps.setString(2, student.department());
            ps.setString(3, student.registerNumber());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM student WHERE register_number=?")) {
            ps.setString(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(rs.getString("register_number"), rs.getString("name"),
                rs.getString("department"));
    }
}
