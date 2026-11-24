import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class CommitteeDashboard extends JDialog {
    private final RegistrationService service=new RegistrationService();
    private final JComboBox<Event> eventFilter=new JComboBox<>();
    private final DefaultTableModel model=new DefaultTableModel(
            new Object[]{"ID","Student ID","Name","Email","Department","Phone","Event","Date"},0);
    private final JTable table=new JTable(model);
    private final JLabel count=new JLabel("Registrations: 0");
    private List<Registration> current=List.of();
    public CommitteeDashboard(Frame owner){
        super(owner,"Committee Registration Dashboard",false);
        setSize(950,600); setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        JMenuBar menuBar=new JMenuBar();
        JMenu file=new JMenu("File"); JMenuItem export=new JMenuItem("Export Filtered to CSV");
        JMenuItem refresh=new JMenuItem("Refresh"); file.add(refresh); file.add(export); menuBar.add(file);
        setJMenuBar(menuBar);
        JPanel filter=new JPanel(new FlowLayout(FlowLayout.LEFT));
        eventFilter.addItem(new Event(-1,"All Events",null));
        filter.add(new JLabel("Event")); filter.add(eventFilter);
        JButton filterButton=new JButton("Filter"); JButton refreshButton=new JButton("Refresh");
        filter.add(filterButton); filter.add(refreshButton); filter.add(count);
        add(filter,BorderLayout.NORTH); table.setFillsViewportHeight(true);
        add(new JScrollPane(table),BorderLayout.CENTER);
        filterButton.addActionListener(e->load());
        refreshButton.addActionListener(e->load());
        refresh.addActionListener(e->load()); export.addActionListener(e->exportCsv());
        loadEvents(); load();
    }
    private void loadEvents(){
        new SwingWorker<List<Event>,Void>() {
            @Override protected List<Event> doInBackground() throws Exception{return service.events();}
            @Override protected void done(){try{for(Event event:get())eventFilter.addItem(event);}catch(Exception ex){showError(ex);}}
        }.execute();
    }
    private void load(){
        Event selected=(Event)eventFilter.getSelectedItem();
        Integer eventId=selected==null||selected.id()<0?null:selected.id();
        new SwingWorker<List<Registration>,Void>() {
            @Override protected List<Registration> doInBackground() throws Exception{return service.registrations(eventId);}
            @Override protected void done(){
                try{current=get(); model.setRowCount(0);
                    for(Registration r:current)model.addRow(new Object[]{r.registrationId(),r.studentId(),r.studentName(),
                            r.email(),r.department(),r.phone(),r.eventName(),r.registrationDate()});
                    count.setText("Registrations: "+current.size());
                    if(current.isEmpty())JOptionPane.showMessageDialog(CommitteeDashboard.this,"No registrations match the selected filter.","No Results",JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception ex){showError(ex);}
            }
        }.execute();
    }
    private void exportCsv(){
        if(current.isEmpty()){JOptionPane.showMessageDialog(this,"There are no registrations to export.","Export",JOptionPane.INFORMATION_MESSAGE);return;}
        JFileChooser chooser=new JFileChooser(); if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;
        File file=chooser.getSelectedFile();
        if(!file.getName().toLowerCase().endsWith(".csv"))file=new File(file.getAbsolutePath()+".csv");
        File target=file;
        new SwingWorker<Void,Void>() {
            @Override protected Void doInBackground() throws Exception{new CsvExporter().export(target,current);return null;}
            @Override protected void done(){try{get();JOptionPane.showMessageDialog(CommitteeDashboard.this,"Exported to "+target.getAbsolutePath(),"Export Complete",JOptionPane.INFORMATION_MESSAGE);}
                catch(Exception ex){showError(ex);}
            }
        }.execute();
    }
    private void showError(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();
        JOptionPane.showMessageDialog(this,"Operation failed: "+cause.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);}
}
