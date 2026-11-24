import java.math.BigDecimal;

public class FeeService {
    public PaymentResult process(String name, String id, String amountText) {
        FeeValidator.validateName(name);
        FeeValidator.validateId(id);
        BigDecimal amount = FeeValidator.validateAmount(amountText);
        boolean accepted = amount.compareTo(BigDecimal.ZERO) > 0;
        return accepted
                ? new PaymentResult(true, "Payment successful: Rs. " + amount + " for " + name.trim())
                : new PaymentResult(false, "Payment rejected.");
    }
}
