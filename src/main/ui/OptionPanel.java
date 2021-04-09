package ui;

import javax.swing.*;
import java.text.NumberFormat;

// Represents an option panel where users can add and modify employees
public class OptionPanel extends JPanel {

    private String[] hourlyTypes = {"Hourly", "Salary"};

    private JTextField employeeNameTextField;
    private JComboBox hourlyStatusSelector;
    private JFormattedTextField employeeWageField;

    public OptionPanel() {
        super();
        initializeFields();
    }

    private void initializeFields() {
        JLabel nameLabel = new JLabel("Employee name:");
        employeeNameTextField = new JTextField(10);

        JLabel hourlyLabel = new JLabel("Hourly status:");
        hourlyStatusSelector = new JComboBox(hourlyTypes);

        JLabel wageLabel = new JLabel("Employee wage:");
        NumberFormat format = NumberFormat.getInstance();
        IntegerFormatter integerFormatter = new IntegerFormatter(format);

        employeeWageField = new JFormattedTextField(integerFormatter);
        employeeWageField.setColumns(7);

        add(nameLabel);
        add(employeeNameTextField);
        add(hourlyLabel);
        add(hourlyStatusSelector);
        add(wageLabel);
        add(employeeWageField);
    }

    public JTextField getEmployeeNameTextField() {
        return employeeNameTextField;
    }

    public JComboBox getHourlyStatusSelector() {
        return hourlyStatusSelector;
    }

    public JFormattedTextField getEmployeeWageField() {
        return employeeWageField;
    }

    // MODIFIES: this
    // EFFECTS: sets all fields to empty
    public void resetFields() {
        employeeNameTextField.setText("");
        employeeWageField.setText("");
    }
}
