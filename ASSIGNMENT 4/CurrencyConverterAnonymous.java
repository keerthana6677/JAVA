import javax.swing.*;
import java.awt.event.*;
public class CurrencyConverterAnonymous extends CurrencyConverterFrameBase {
    public CurrencyConverterAnonymous() {
        super("Currency Converter - Anonymous Inner Class");
        ActionListener handler=new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) { convert(); }
        };
        convertButton.addActionListener(handler); amountField.addActionListener(handler);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterAnonymous().setVisible(true));
    }
}
