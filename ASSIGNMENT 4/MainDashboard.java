import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    public MainDashboard(){
        super("Campus Event Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(620,320); setLocationRelativeTo(null);
        JMenuBar menu=new JMenuBar();
        JMenu application=new JMenu("Application");
        JMenuItem student=new JMenuItem("Student Registration");
        JMenuItem committee=new JMenuItem("Committee Login");
        JMenuItem exit=new JMenuItem("Exit");
        application.add(student); application.add(committee); application.addSeparator(); application.add(exit);
        menu.add(application); setJMenuBar(menu);
        JPanel panel=new JPanel(new GridBagLayout());
        JPanel actions=new JPanel(new GridLayout(2,1,12,12));
        JButton studentButton=new JButton("Student Registration");
        JButton committeeButton=new JButton("Committee Login");
        actions.add(studentButton); actions.add(committeeButton); panel.add(actions);
        JLabel title=new JLabel("Campus Event Registration",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD,24f));
        add(title,BorderLayout.NORTH); add(panel,BorderLayout.CENTER);
        studentButton.addActionListener(e->new StudentRegistrationFrame(this).setVisible(true));
        committeeButton.addActionListener(e->new CommitteeLoginFrame(this).setVisible(true));
        student.addActionListener(e->new StudentRegistrationFrame(this).setVisible(true));
        committee.addActionListener(e->new CommitteeLoginFrame(this).setVisible(true));
        exit.addActionListener(e->dispose());
    }
}
