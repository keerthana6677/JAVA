import javax.swing.SwingUtilities;

public class FeePaymentApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeePaymentFrame().setVisible(true));
    }
}
