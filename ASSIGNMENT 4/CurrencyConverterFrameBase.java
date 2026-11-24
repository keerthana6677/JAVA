import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class CurrencyConverterFrameBase extends JFrame {
    protected final JTextField amountField=new JTextField(12);
    protected final JComboBox<String> fromCurrency=new JComboBox<>(CurrencyConverter.currencies());
    protected final JComboBox<String> toCurrency=new JComboBox<>(CurrencyConverter.currencies());
    protected final JLabel resultLabel=new JLabel("Result: ");
    protected final JButton convertButton=new JButton("Convert");
    protected final JButton clearButton=new JButton("Clear");
    protected CurrencyConverterFrameBase(String title) {
        super(title); setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(460,260); setLocationRelativeTo(null);
        JPanel panel=new JPanel(new GridLayout(5,2,8,8));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        panel.add(new JLabel("Amount")); panel.add(amountField);
        panel.add(new JLabel("From")); panel.add(fromCurrency);
        panel.add(new JLabel("To")); panel.add(toCurrency);
        panel.add(convertButton); panel.add(clearButton);
        panel.add(new JLabel("Result")); panel.add(resultLabel);
        add(panel);
        clearButton.addActionListener(e -> clear());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent event) {
                int choice=JOptionPane.showConfirmDialog(CurrencyConverterFrameBase.this,
                        "Exit "+title+"?","Confirm Exit",JOptionPane.YES_NO_OPTION);
                if(choice==JOptionPane.YES_OPTION) dispose();
            }
        });
    }
    protected void clear(){
        amountField.setText(""); resultLabel.setText("Result: "); amountField.requestFocus();
    }
    protected void convert() {
        String text=amountField.getText().trim();
        if(text.isEmpty()) { showError("Amount is required."); return; }
        try {
            double amount=Double.parseDouble(text);
            if(amount<=0) { showError("Amount must be greater than zero."); return; }
            String from=(String)fromCurrency.getSelectedItem();
            String to=(String)toCurrency.getSelectedItem();
            double result=CurrencyConverter.convert(amount,from,to);
            String message=String.format("%.2f %s = %.2f %s",amount,from,result,to);
            resultLabel.setText(message);
            JOptionPane.showMessageDialog(this,message,"Conversion Result",JOptionPane.INFORMATION_MESSAGE);
        } catch(NumberFormatException ex) {
            showError("Enter a valid numeric amount.");
        }
    }
    protected void showError(String message){
        resultLabel.setText("Error: "+message);
        JOptionPane.showMessageDialog(this,message,"Invalid Amount",JOptionPane.ERROR_MESSAGE);
    }
}
