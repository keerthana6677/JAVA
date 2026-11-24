# Task 2 Event Table and Pseudocode

| Event Source | Event Object | Listener | Action |
|---|---|---|---|
| Department JComboBox | ItemEvent/ActionEvent | ActionListener | Refresh doctor combo |
| Doctor JComboBox | ActionEvent | Item/Action listener | Store selected doctor |
| Date JSpinner | ChangeEvent | ChangeListener | Store selected date |
| Book JButton | ActionEvent | ActionListener | Validate and book |
| Clear JButton | ActionEvent | ActionListener | Reset form |
| JFrame | WindowEvent | WindowListener/adapter | Initialize and close |

## Doctor Refresh Pseudocode
```
selectedDepartment = department.getSelectedItem()
doctorList.removeAllItems()
if selectedDepartment == "Select Department":
    doctorList.setEnabled(false)
    return
doctors = map.get(selectedDepartment)
for each doctor in doctors:
    doctorList.addItem(doctor)
doctorList.setEnabled(true)
```

## Booking Pseudocode
```
read name, id, phone, department, doctor, date
validate each required field
if invalid: show error and stop
bookingKey = id + doctor + date
if bookings contains bookingKey: reject duplicate
add bookingKey
show confirmation
```

## Method Sequence
Main -> invokeLater -> constructor -> build components -> setVisible -> user events -> refresh/update -> window closing -> dispose.

## Swing/Applet Comparison
Swing starts with main and constructor, setVisible, EDT events, and JFrame close. Applet lifecycle uses init(), start(), paint(), stop(), and destroy(). Both react to events, but Swing has no mandatory applet browser lifecycle.
