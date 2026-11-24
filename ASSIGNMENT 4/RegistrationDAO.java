import java.sql.*;
import java.time.ZoneId;
import java.util.*;

public class RegistrationDAO {
    public List<Event> findEvents() throws SQLException, java.io.IOException {
        String sql="SELECT event_id,event_name,event_date FROM events ORDER BY event_date,event_name";
        List<Event> events=new ArrayList<>();
        try(Connection connection=DatabaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql);
            ResultSet result=statement.executeQuery()){
            while(result.next()){
                java.sql.Date date=result.getDate("event_date");
                events.add(new Event(result.getInt("event_id"),result.getString("event_name"),
                        date==null?null:date.toLocalDate()));
            }
        }
        return events;
    }
    public void register(String studentId,String name,String email,String department,String phone,int eventId)
            throws SQLException,java.io.IOException{
        try(Connection connection=DatabaseConnection.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement upsert=connection.prepareStatement(
                    "INSERT INTO students(student_id,student_name,email,department,phone) VALUES (?,?,?,?,?) "+
                    "ON DUPLICATE KEY UPDATE student_name=VALUES(student_name),email=VALUES(email),department=VALUES(department),phone=VALUES(phone)");
                PreparedStatement insert=connection.prepareStatement(
                    "INSERT INTO registrations(student_id,event_id) VALUES (?,?)")){
                upsert.setString(1,studentId); upsert.setString(2,name); upsert.setString(3,email);
                upsert.setString(4,department); upsert.setString(5,phone); upsert.executeUpdate();
                insert.setString(1,studentId); insert.setInt(2,eventId); insert.executeUpdate();
                connection.commit();
            } catch(SQLException ex){
                connection.rollback();
                if(ex.getErrorCode()==1062) throw new SQLException("Duplicate registration: this student is already registered for the selected event.",ex);
                throw ex;
            } finally { connection.setAutoCommit(true); }
        }
    }
    public List<Registration> findAll(Integer eventId) throws SQLException,java.io.IOException{
        String sql=eventId==null
                ? "SELECT r.registration_id,r.student_id,s.student_name,s.email,s.department,s.phone,r.event_id,e.event_name,r.registration_date FROM registrations r JOIN students s ON s.student_id=r.student_id JOIN events e ON e.event_id=r.event_id ORDER BY r.registration_id"
                : "SELECT r.registration_id,r.student_id,s.student_name,s.email,s.department,s.phone,r.event_id,e.event_name,r.registration_date FROM registrations r JOIN students s ON s.student_id=r.student_id JOIN events e ON e.event_id=r.event_id WHERE r.event_id=? ORDER BY r.registration_id";
        List<Registration> resultList=new ArrayList<>();
        try(Connection connection=DatabaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            if(eventId!=null) statement.setInt(1,eventId);
            try(ResultSet result=statement.executeQuery()){
                while(result.next()){
                    Timestamp time=result.getTimestamp("registration_date");
                    resultList.add(new Registration(result.getInt("registration_id"),result.getString("student_id"),
                            result.getString("student_name"),result.getString("email"),result.getString("department"),
                            result.getString("phone"),result.getInt("event_id"),result.getString("event_name"),
                            time==null?null:time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
                }
            }
        }
        return resultList;
    }
    public boolean login(String username,String password) throws SQLException,java.io.IOException{
        String sql="SELECT 1 FROM committee_users WHERE username=? AND password_hash=?";
        try(Connection connection=DatabaseConnection.getConnection();
            PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1,username); statement.setString(2,password);
            try(ResultSet result=statement.executeQuery()){ return result.next(); }
        }
    }
}
