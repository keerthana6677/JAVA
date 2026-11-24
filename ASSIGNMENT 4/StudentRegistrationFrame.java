import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentRegistrationFrame extends JDialog {
    private final RegistrationService service=new RegistrationService();
    private final JTextField studentId=new JTextField(18);
    private final JTextField name=new JTextField(18);
    private final JTextField email=new JTextField(18);
    private final JTextField department=new JTextField(18);
    private final JTextField phone=new JTextField(18);
    private final JComboBox<Event> events=new JComboBox<>();
    public StudentRegistrationFrame(Frame owner){
        super(owner,"Student Registration",true);
        setSize(520,430); setLocationRelativeTo(owner);
        JPanel form=new JPanel(new GridLayout(7,2,8,8));
        form.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        form.add(new JLabel("Student ID")); form.add(studentId);
        form.add(new JLabel("Student Name")); form.add(name);
        form.add(new JLabel("Email")); form.add(email);
        form.add(new JLabel("Department")); form.add(department);
        form.add(new JLabel("Phone")); form.add(phone);
        form.add(new JLabel("Event")); form.add(events);
        JButton register=new JButton("Register"); JButton cancel=new JButton("Cancel");
        form.add(register); form.add(cancel);
        add(form);
        register.addActionListener(e->submit());
        cancel.addActionListener(e->dispose());
        loadEvents();
    }
    private void loadEvents(){
        new SwingWorker<List<Event>,Void>() {
            @Override protected List<Event> doInBackground() throws Exception{return service.events();}
            @Override protected void done(){
                try{for(Event event:get()) events.addItem(event);}
                catch(Exception ex){JOptionPane.showMessageDialog(StudentRegistrationFrame.this,
                        "Could not load events: "+ex.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);}
            }
        }.execute();
    }
    private void submit(){
        Event event=(Event)events.getSelectedItem();
        Integer eventId=event==null?null:event.id();
        new SwingWorker<Void,Void>() {
            @Override protected Void doInBackground() throws Exception{
                service.register(studentId.getText(),name.getText(),email.getText(),department.getText(),phone.getText(),eventId);
                return null;
            }
            @Override protected void done(){
                try{get();JOptionPane.showMessageDialog(StudentRegistrationFrame.this,
                        "Registration successful for "+event.name()+".","Confirmation",JOptionPane.INFORMATION_MESSAGE);}
                catch(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();
                    JOptionPane.showMessageDialog(StudentRegistrationFrame.this,cause.getMessage(),"Registration Failed",JOptionPane.ERROR_MESSAGE);}
            }
        }.execute();
    }
}
