import javax.swing.*;
import java.awt.*;

public class CommitteeLoginFrame extends JDialog {
    private final RegistrationService service=new RegistrationService();
    private final JTextField username=new JTextField(18);
    private final JPasswordField password=new JPasswordField(18);
    public CommitteeLoginFrame(Frame owner){
        super(owner,"Committee Login",true);
        setSize(400,220); setLocationRelativeTo(owner);
        JPanel panel=new JPanel(new GridLayout(3,2,8,8));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        panel.add(new JLabel("Username")); panel.add(username);
        panel.add(new JLabel("Password")); panel.add(password);
        JButton login=new JButton("Login"); JButton cancel=new JButton("Cancel");
        panel.add(login); panel.add(cancel); add(panel);
        login.addActionListener(e->attemptLogin(owner)); cancel.addActionListener(e->dispose());
    }
    private void attemptLogin(Frame owner){
        new SwingWorker<Boolean,Void>() {
            @Override protected Boolean doInBackground() throws Exception{
                return service.login(username.getText().trim(),new String(password.getPassword()));
            }
            @Override protected void done(){
                try{
                    if(get()) { dispose(); new CommitteeDashboard(owner).setVisible(true); }
                    else JOptionPane.showMessageDialog(CommitteeLoginFrame.this,"Invalid username or password.","Login Failed",JOptionPane.ERROR_MESSAGE);
                }catch(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();
                    JOptionPane.showMessageDialog(CommitteeLoginFrame.this,"Login error: "+cause.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);}
            }
        }.execute();
    }
}
