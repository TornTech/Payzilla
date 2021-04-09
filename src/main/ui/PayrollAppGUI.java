package ui;

import exceptions.DuplicateEmployeeException;
import exceptions.EmployeeDoesNotExistException;
import model.Employee;
import model.EmployeeList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import java.io.*;
import javax.sound.sampled.*;

public class PayrollAppGUI implements ListSelectionListener {

    // Fields to set JFrame width and height
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 500;

    // Fields for Swing components
    private JMenuBar menuBar;
    private JButton addButton;
    private JButton deleteButton;
    private JButton viewButton;
    private JButton modifyButton;
    private JButton recordButton;
    private JButton payButton;
    private JList list;
    private OptionPanel optionPanel;

    // Fields required for loading and saving the file
    private static final String JSON_STORE = "./data/employees.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Fields required to keep employee lists
    protected EmployeeList employeeList;
    private DefaultListModel listModelEmployeeObjects;
    private DefaultListModel listModelEmployeeNames;

    // Fields for sounds
    private SoundMaker soundMaker = new SoundMaker();

    public PayrollAppGUI() {

        // Set disk location for read/write operations
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        initializeGraphics();
        initializeActionListeners();
    }

    // MODIFIES: this
    // EFFECTS: initializes all graphical components
    private void initializeGraphics() {
        // Create Frame
        JFrame frame = new JFrame("Payzilla");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);

        initializeMenuBar();
        initializeOptionPanel();

        // Displays list of employees
        listModelEmployeeObjects = new DefaultListModel();
        listModelEmployeeNames = new DefaultListModel();

        loadEmployees();

        list = new JList(listModelEmployeeNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        // Add components to the frame
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
        frame.getContentPane().add(listScrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(optionPanel, BorderLayout.SOUTH);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        // Set location of frame
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int widthDimension = dimension.width / 2 - frame.getSize().width / 2;
        int heightDimension = dimension.height / 2 - frame.getSize().height / 2;
        frame.setLocation(widthDimension, heightDimension);

        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Create option panel at the bottom of the UI
    private void initializeOptionPanel() {
        optionPanel = new OptionPanel();

        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        viewButton = new JButton("View");
        modifyButton = new JButton("Modify");
        recordButton = new JButton("Record Hours");
        payButton = new JButton("Pay");

        optionPanel.add(addButton);
        optionPanel.add(deleteButton);
        optionPanel.add(viewButton);
        optionPanel.add(modifyButton);
        optionPanel.add(recordButton);
        optionPanel.add(payButton);
    }

    // MODIFIES: this
    // EFFECTS: Create navigation bar menu options
    private void initializeMenuBar() {
        menuBar = new JMenuBar();
        JMenu option1Menu = new JMenu("File");
        menuBar.add(option1Menu);
        JMenuItem option1SubMenuLoad = new JMenuItem("Load employee data");
        JMenuItem option1SubMenuSave = new JMenuItem("Save employee data");
        option1Menu.add(option1SubMenuLoad);
        option1Menu.add(option1SubMenuSave);

        option1SubMenuLoad.addActionListener(new ActionListener() {
            // MODIFIES: this
            // EFFECTS: Adds action listener that loads employees from file
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEmployees();
            }
        });

        option1SubMenuSave.addActionListener(new ActionListener() {
            // EFFECTS: Adds action listener that saves employees to file
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEmployeeList();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes the action listeners for the buttons
    private void initializeActionListeners() {
        setListenersAddButton();
        setListenersViewButton();
        setListenersDeleteButton();
        setListenerModifyButton();
        setListenersRecordButton();
        setListenersPayButton();
    }

    // MODIFIES: this
    // EFFECTS: adds listener to modify button
    private void setListenerModifyButton() {
        modifyButton.addActionListener(new ActionListener() {

            // MODIFIES: the employee selected by user
            // EFFECTS: modifies an employee based on user inputs
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Employee selectedEmployee = getSelectedEmployee();
                    new ModifyPanel(selectedEmployee, employeeList);
                    refreshEmployees();
                } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                    // Do nothing as no employee was selected
                }
            }
        });
    }

    // EFFECTS: returns the user selected employee
    private Employee getSelectedEmployee() {
        int selectedIndex = list.getSelectedIndex();
        return (Employee) listModelEmployeeObjects.getElementAt(selectedIndex);
    }

    // MODIFIES: this
    // EFFECTS: adds a listener to the delete button
    private void setListenersDeleteButton() {
        deleteButton.addActionListener(new ActionListener() {

            // MODIFIES: this
            // EFFECTS: deletes the selected employee from employee list
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Employee selectedEmployee = getSelectedEmployee();
                    employeeList.deleteEmployee(selectedEmployee);
                    refreshEmployees();
                    soundMaker.playDeleteSound();
                } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                    // Do nothing since nothing is selected
                } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
                    // Attempted to delete an employee that does not exist - should never reach this
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: adds a listener to the view button
    private void setListenersViewButton() {
        viewButton.addActionListener(new ActionListener() {

            // EFFECTS: Opens panel to view detailed employee information
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Employee selectedEmployee = getSelectedEmployee();
                    String name = "Name: " + selectedEmployee.getName() + "\n";
                    String hourly = "Hourly Employee: " + selectedEmployee.getHourlyStatus() + "\n";
                    String wage = "Wage: $" + selectedEmployee.getWage() + "\n";
                    String owned = "Current owned: $" + selectedEmployee.getCurrentOwned() + "\n";
                    String totalPaid = "Paid over lifetime: $" + selectedEmployee.getTotalPaid();
                    JOptionPane.showMessageDialog(new JFrame(), name + hourly + wage + owned + totalPaid);
                } catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
                    // Do nothing since nothing is selected
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: adds a listener to the add button
    private void setListenersAddButton() {
        addButton.addActionListener(new ActionListener() {

            // MODIFIES: this
            // EFFECTS: adds new employee to employee list based on user inputs
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = optionPanel.getEmployeeNameTextField().getText();
                    String hourlyStatusSelection = (String) optionPanel.getHourlyStatusSelector().getSelectedItem();
                    Boolean hourlyStatus = hourlyStatusSelection.equals("Hourly");

                    if (name.equals("") || optionPanel.getEmployeeWageField().getText().length() == 0) {
                        JOptionPane.showMessageDialog(new JFrame(), "All fields must be entered");
                    } else {
                        int wage = (int) optionPanel.getEmployeeWageField().getValue();
                        Employee employeeToAdd = new Employee(name, hourlyStatus, wage);
                        employeeList.addEmployee(employeeToAdd);
                        refreshEmployees();
                        soundMaker.playAddSound();
                        optionPanel.resetFields();
                    }
                } catch (DuplicateEmployeeException duplicateEmployeeException) {
                    JOptionPane.showMessageDialog(new JFrame(), "Can't add employee\n"
                            + "Another employee with the same name already exists");
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: adds a listener to the record button
    private void setListenersRecordButton() {
        recordButton.addActionListener(new ActionListener() {

            // MODIFIES: employee selected by user
            // EFFECTS: records time worked for an employee
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Employee selectedEmployee = getSelectedEmployee();
                    String timeFrame = selectedEmployee.getHourlyStatus() ? "hours" : "days";
                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("How many " + timeFrame + " would you like to record?"));
                    NumberFormat format = NumberFormat.getInstance();
                    IntegerFormatter integerFormatter = new IntegerFormatter(format);
                    JFormattedTextField timeWorkedField = new JFormattedTextField(integerFormatter);
                    panel.add(timeWorkedField);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Record Hours",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        int timeWorked = (int) timeWorkedField.getValue();
                        selectedEmployee.recordWorkAmount(timeWorked);
                    }
                } catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
                    // Do nothing since nothing is selected
                } catch (NullPointerException nullPointerException) {
                    // Do nothing since nothing is selected
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: adds a listener to the pay button
    private void setListenersPayButton() {
        payButton.addActionListener(new ActionListener() {

            // MODIFIES: employee selected by user
            // EFFECTS: pays the amount owning to the employees
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Employee selectedEmployee = getSelectedEmployee();
                    int amountToPay = selectedEmployee.getCurrentOwned();
                    if (amountToPay != 0) {
                        selectedEmployee.payEmployee();
                        soundMaker.playPaidSound();
                        JOptionPane.showMessageDialog(new JFrame(), "Employee has been paid $" + amountToPay);
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Employee does not have amount owning");
                    }
                } catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
                    // Do nothing since nothing is selected
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Loads employees from disc and refreshes employee list on screen
    private void loadEmployees() {
        // Reads employees from disc file
        loadEmployeeList();

        // Refreshes employees displayed on screen
        refreshEmployees();
    }

    // MODIFIES: this
    // EFFECTS: Refreshes the employees on screen with the latest data
    public void refreshEmployees() {
        listModelEmployeeNames.clear();
        listModelEmployeeObjects.clear();
        List<Employee> listOfEmployees = employeeList.getAllEmployees();
        for (Employee employee : listOfEmployees) {
            String employeeName = employee.getName();
            listModelEmployeeObjects.addElement(employee);
            listModelEmployeeNames.addElement(employeeName);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the employee list from a file
    protected void loadEmployeeList() {
        try {
            employeeList = jsonReader.read();
            System.out.println("Successfully loaded employee list from: " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the employee list to file
    private void saveEmployeeList() {
        try {
            jsonWriter.open();
            jsonWriter.write(employeeList);
            jsonWriter.close();
            soundMaker.playSaveSound();
            System.out.println("Successfully saved employee list to: " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: No effects. Required by interface to be added but not used
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // Does nothing
    }
}
