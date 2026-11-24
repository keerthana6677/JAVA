import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptService {
    public String generate(String name, String id, String amountText) {
        FeeValidator.validateName(name); FeeValidator.validateId(id);
        BigDecimal amount = FeeValidator.validateAmount(amountText);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return "FEE PAYMENT RECEIPT\nName: " + name.trim() + "\nStudent ID: " + id.trim()
                + "\nAmount: Rs. " + amount + "\nStatus: SIMULATED SUCCESS\nDate: " + time;
    }
}
