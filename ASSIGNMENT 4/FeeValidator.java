import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class FeeValidator {
    private static final Pattern ID_PATTERN = Pattern.compile("[A-Za-z0-9-]{3,20}");
    private FeeValidator() {}

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Student name is required.");
        if (name.trim().length() < 2) throw new IllegalArgumentException("Student name is too short.");
    }
    public static void validateId(String id) {
        if (id == null || !ID_PATTERN.matcher(id.trim()).matches())
            throw new IllegalArgumentException("Student ID must contain 3-20 letters, digits or hyphens.");
    }
    public static BigDecimal validateAmount(String amountText) {
        if (amountText == null || amountText.trim().isEmpty())
            throw new IllegalArgumentException("Amount is required.");
        try {
            BigDecimal amount = new BigDecimal(amountText.trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
            if (amount.scale() > 2) throw new IllegalArgumentException("Use at most two decimal places.");
            return amount;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Amount must be a valid number.");
        }
    }
}
