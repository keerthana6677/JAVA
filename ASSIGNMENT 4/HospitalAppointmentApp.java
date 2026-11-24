import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class HospitalAppointmentApp extends JFrame {
    private final JComboBox<String> department = new JComboBox<>();
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JSpinner date = new JSpinner(new SpinnerDateModel());
    private final JTextField patientName = new JTextField(18);
    private final JTextField patientId = new JTextField(18);
    private final JTextField phone = new JTextField(18);
    private final JTextArea confirmation = new JTextArea(6, 35);
    private final Map<String, List<String>> doctors = new LinkedHashMap<>();
    private final Set<String> bookings = new HashSet<>();

    public HospitalAppointmentApp() {
        super("Hospital Outpatient Appointment Desk");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 480); setLocationRelativeTo(null);
        buildData();
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        department.addItem("Select Department");
        doctors.keySet().forEach(department::addItem);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(date, "yyyy-MM-dd");
        date.setEditor(editor);
        form.add(new JLabel("Department")); form.add(department);
        form.add(new JLabel("Doctor")); form.add(doctor);
        form.add(new JLabel("Date")); form.add(date);
        form.add(new JLabel("Patient Name")); form.add(patientName);
        form.add(new JLabel("Patient ID")); form.add(patientId);
        form.add(new JLabel("Phone")); form.add(phone);
        department.addActionListener(e -> refreshDoctors());
        doctor.setEnabled(false);
        JButton book = new JButton("Book Appointment");
        JButton clear = new JButton("Clear");
        book.addActionListener(e -> bookAppointment());
        clear.addActionListener(e -> clearForm());
        JPanel buttons = new JPanel(new FlowLayout()); buttons.add(book); buttons.add(clear);
        confirmation.setEditable(false); confirmation.setBorder(BorderFactory.createTitledBorder("Confirmation"));
        add(form, BorderLayout.NORTH); add(buttons, BorderLayout.CENTER);
        add(new JScrollPane(confirmation), BorderLayout.SOUTH);
    }

    private void buildData() {
        doctors.put("Cardiology", List.of("Dr. Arjun", "Dr. Meera"));
        doctors.put("Dermatology", List.of("Dr. Kavya", "Dr. Ravi"));
        doctors.put("Neurology", List.of("Dr. Leela", "Dr. Sanjay"));
        doctors.put("Orthopedics", List.of("Dr. Nisha", "Dr. Vijay"));
    }
    private void refreshDoctors() {
        doctor.removeAllItems();
        String selected = (String) department.getSelectedItem();
        if (selected == null || "Select Department".equals(selected)) {
            doctor.setEnabled(false); return;
        }
        doctors.getOrDefault(selected, List.of()).forEach(doctor::addItem);
        doctor.setEnabled(true);
    }
    private void bookAppointment() {
        String name = patientName.getText().trim();
        String id = patientId.getText().trim();
        String phoneText = phone.getText().trim();
        String dept = (String) department.getSelectedItem();
        String selectedDoctor = (String) doctor.getSelectedItem();
        if (name.isEmpty()) { showError("Patient name is required."); return; }
        if (!id.matches("[A-Za-z0-9-]{3,20}")) { showError("Patient ID must be 3-20 letters/digits."); return; }
        if (!phoneText.matches("[0-9]{10}")) { showError("Phone number must contain exactly 10 digits."); return; }
        if (dept == null || "Select Department".equals(dept) || selectedDoctor == null) { showError("Select department and doctor."); return; }
        String dateText = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date.getValue());
        String key = id + "|" + selectedDoctor + "|" + dateText;
        if (!bookings.add(key)) { showError("This patient already has an appointment with this doctor on this date."); return; }
        confirmation.setText("Appointment confirmed\nPatient: " + name + "\nID: " + id
                + "\nDepartment: " + dept + "\nDoctor: " + selectedDoctor + "\nDate: " + dateText);
        JOptionPane.showMessageDialog(this, confirmation.getText(), "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }
    private void clearForm() {
        department.setSelectedIndex(0); patientName.setText(""); patientId.setText(""); phone.setText(""); confirmation.setText("");
    }
    private void showError(String message) {
        confirmation.setText("ERROR: " + message);
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HospitalAppointmentApp().setVisible(true));
    }
}
