import javax.swing.*;
import java.awt.event.*;
public class CurrencyConverterActionListener extends CurrencyConverterFrameBase implements ActionListener {
    public CurrencyConverterActionListener() {
        super("Currency Converter - Frame implements ActionListener");
        convertButton.addActionListener(this);
        amountField.addActionListener(this);
    }
    @Override public void actionPerformed(ActionEvent event) { convert(); }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterActionListener().setVisible(true));
    }
}
