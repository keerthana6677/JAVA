import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FeePaymentFrame extends JFrame {
    private final JTextField studentName = new JTextField(18);
    private final JTextField studentId = new JTextField(18);
    private final JTextField amount = new JTextField(18);
    private final JLabel result = new JLabel("Enter details and pay fees.");
    private final FeeService feeService = new FeeService();
    private final ReceiptService receiptService = new ReceiptService();

    public FeePaymentFrame() {
        super("Student Fee Payment");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 300);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        form.add(new JLabel("Student Name:")); form.add(studentName);
        form.add(new JLabel("Student ID:")); form.add(studentId);
        form.add(new JLabel("Amount (Rs.):")); form.add(amount);
        form.add(new JLabel("Status:")); form.add(result);

        JButton pay = new JButton("Pay Fees");
        JButton reset = new JButton("Reset");
        JButton receipt = new JButton("Generate Receipt");
        JButton exit = new JButton("Exit");
        pay.addActionListener(e -> payFees());
        reset.addActionListener(e -> resetForm());
        receipt.addActionListener(e -> generateReceipt());
        exit.addActionListener(e -> dispatchEvent(new java.awt.event.WindowEvent(this, java.awt.event.WindowEvent.WINDOW_CLOSING)));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(pay); buttons.add(reset); buttons.add(receipt); buttons.add(exit);
        add(form, BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
    }

    private void payFees() {
        try {
            PaymentResult payment = feeService.process(studentName.getText(), studentId.getText(), amount.getText());
            result.setText(payment.message());
            JOptionPane.showMessageDialog(this, payment.message(), "Payment", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            result.setText(ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        studentName.setText(""); studentId.setText(""); amount.setText("");
        result.setText("Enter details and pay fees."); studentName.requestFocus();
    }

    private void generateReceipt() {
        try {
            String receipt = receiptService.generate(studentName.getText(), studentId.getText(), amount.getText());
            JTextArea area = new JTextArea(receipt); area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Payment Receipt", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Receipt Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
