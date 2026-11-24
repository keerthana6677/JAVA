import java.util.regex.Pattern;

public class ValidationService {
    private static final Pattern EMAIL=Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE=Pattern.compile("^\\+?[0-9]{10,15}$");
    public void validate(String studentId,String name,String email,String department,String phone,Integer eventId){
        if(studentId==null || studentId.trim().isEmpty()) throw new IllegalArgumentException("Student ID is required.");
        if(!studentId.trim().matches("[A-Za-z0-9-]{3,20}")) throw new IllegalArgumentException("Student ID must be 3-20 letters/digits.");
        if(name==null || name.trim().isEmpty()) throw new IllegalArgumentException("Student name is required.");
        if(email==null || !EMAIL.matcher(email.trim()).matches()) throw new IllegalArgumentException("Enter a valid email address.");
        if(department==null || department.trim().isEmpty()) throw new IllegalArgumentException("Department is required.");
        if(phone==null || !PHONE.matcher(phone.trim()).matches()) throw new IllegalArgumentException("Phone number must contain 10-15 digits.");
        if(eventId==null) throw new IllegalArgumentException("Select an event.");
    }
}
