import java.sql.SQLException;
import java.util.List;

public class RegistrationService {
    private final ValidationService validation=new ValidationService();
    private final RegistrationDAO dao=new RegistrationDAO();
    public void register(String studentId,String name,String email,String department,String phone,Integer eventId)
            throws SQLException,java.io.IOException{
        validation.validate(studentId,name,email,department,phone,eventId);
        dao.register(studentId.trim(),name.trim(),email.trim(),department.trim(),phone.trim(),eventId);
    }
    public List<Event> events() throws SQLException,java.io.IOException{return dao.findEvents();}
    public List<Registration> registrations(Integer eventId) throws SQLException,java.io.IOException{return dao.findAll(eventId);}
    public boolean login(String username,String password) throws SQLException,java.io.IOException{return dao.login(username,password);}
}
