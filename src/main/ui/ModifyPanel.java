package ui;

import model.Employee;
import model.EmployeeList;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

// Represents a panel that allows employees to modify selected employees
public class ModifyPanel extends JPanel {

    private Employee selectedEmployee;
    private EmployeeList employeeList;

    private JTextField employeeNameField;
    private JComboBox employeeHourlyField;
    private JFormattedTextField employeeWageField;

    private String[] hourlyTypes = {"Hourly", "Salary"};

    public ModifyPanel(Employee employee, EmployeeList employeeList) {
        super(new GridLayout(0, 1));
        this.selectedEmployee = employee;
        this.employeeList = employeeList;

        initializeFields();
        initializePanel();
        modifyEmployee();
    }

    // MODIFIES: this
    // EFFECTS: initializes fields required for the modify panel
    private void initializeFields() {
        employeeNameField = new JTextField(selectedEmployee.getName());
        NumberFormat format = NumberFormat.getInstance();
        IntegerFormatter integerFormatter = new IntegerFormatter(format);
        employeeWageField = new JFormattedTextField(integerFormatter);
    }

    // MODIFIES: this
    // EFFECTS: initializes the modify panel functions
    private void initializePanel() {
        add(new JLabel("Employee name:"));
        add(employeeNameField);

        add(new JLabel("Hourly status:"));
        employeeHourlyField = new JComboBox(hourlyTypes);

        if (selectedEmployee.getHourlyStatus()) {
            employeeHourlyField.setSelectedIndex(0);
        } else {
            employeeHourlyField.setSelectedIndex(1);
        }

        add(employeeHourlyField);
        add(new JLabel("Wage:"));
        employeeWageField.setValue(selectedEmployee.getWage());
        add(employeeWageField);

        int result = JOptionPane.showConfirmDialog(null, this, "Modify Employee",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            modifyEmployee();
        }
    }

    // MODIFIES: selectedEmployee
    // EFFECTS: If user presses OK, the selected employee will be modified with new fields
    private void modifyEmployee() {
        String hourlyStatusSelection = (String) employeeHourlyField.getSelectedItem();
        String newEmployeeName = employeeNameField.getText();
        boolean hourlyStatus = hourlyStatusSelection.equals("Hourly") ? true : false;
        int newEmployeeWage = (Integer) employeeWageField.getValue();
        Employee employeeToModify = new Employee(newEmployeeName, hourlyStatus, newEmployeeWage);
        boolean nameTaken = selectedEmployee.getName().equals(employeeNameField.getText());
        boolean listContainsEmployee = employeeList.contains(employeeToModify);
        if (!nameTaken && listContainsEmployee) {
            JOptionPane.showMessageDialog(new JPanel(), "ERROR: Employee with same name already exists");
        } else {
            selectedEmployee.reset(employeeNameField.getText(), hourlyStatus,
                    (Integer) employeeWageField.getValue());
        }
    }
}
