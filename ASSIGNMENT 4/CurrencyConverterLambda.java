import javax.swing.*;
public class CurrencyConverterLambda extends CurrencyConverterFrameBase {
    public CurrencyConverterLambda() {
        super("Currency Converter - Lambda");
        convertButton.addActionListener(event -> convert());
        amountField.addActionListener(event -> convert());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterLambda().setVisible(true));
    }
}
