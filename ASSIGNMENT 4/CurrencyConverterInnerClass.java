import javax.swing.*;
import java.awt.event.*;
public class CurrencyConverterInnerClass extends CurrencyConverterFrameBase {
    private class ConvertHandler implements ActionListener {
        @Override public void actionPerformed(ActionEvent event) { convert(); }
    }
    public CurrencyConverterInnerClass() {
        super("Currency Converter - Named Inner Class");
        ConvertHandler handler=new ConvertHandler();
        convertButton.addActionListener(handler); amountField.addActionListener(handler);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterInnerClass().setVisible(true));
    }
}
