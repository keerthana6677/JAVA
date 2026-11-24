import java.time.LocalDateTime;

public record Registration(int registrationId, String studentId, String studentName,
                           String email, String department, String phone,
                           int eventId, String eventName, LocalDateTime registrationDate) {}
