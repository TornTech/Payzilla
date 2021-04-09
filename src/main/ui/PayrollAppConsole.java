package ui;

import exceptions.DuplicateEmployeeException;
import exceptions.EmployeeDoesNotExistException;
import model.Employee;
import model.EmployeeList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

// Payroll application
public class PayrollAppConsole {
    private static final String JSON_STORE = "./data/employees.json";
    protected EmployeeList employeeList;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the payroll application
    public PayrollAppConsole() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runPayrollApp();
    }

    // EFFECTS: processes user input
    private void runPayrollApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
//            new PayrollAppRender();
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nThank you for using Payzilla. See you later!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("m")) {
            modifyEmployeeList();
        } else if (command.equals("r")) {
            recordHours();
        } else if (command.equals("p")) {
            payEmployee();
        } else if (command.equals("s")) {
            saveEmployeeList();
        } else if (command.equals("l")) {
            loadEmployeeList();
        } else {
            System.out.println("Selection not valid");
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
            System.out.println("Successfully saved employee list to: " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes employee list
    private void init() {
        employeeList = new EmployeeList();
//        loadEmployeeList();
        input = new Scanner(System.in);
    }

    // EFFECTS: returns the employee list
    public List<Employee> getEmployeeList() {
        return employeeList.getAllEmployees();
    }

    // EFFECTS: displays initial menu of options to user
    private void displayMenu() {
        System.out.println("\n--------------Welcome to Payzilla!--------------");
        System.out.println("Please select from one of the following options:");
        System.out.println("\tm -> modify or view employee list");
        System.out.println("\tr -> record hours worked for an employee");
        System.out.println("\tp -> pay an employee");
        System.out.println("\ts -> save employee data to file");
        System.out.println("\tl -> load employee data from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: edits the list of employees in the HR system
    private void modifyEmployeeList() {
        System.out.println("Please select the action you would like to perform:");
        System.out.println("\tv -> View list of current employees");
        System.out.println("\ta -> Add new employee");
        System.out.println("\tm -> Modify current employee");
        System.out.println("\td -> Delete employee");

        String command = input.next();
        command = command.toLowerCase();

        if (command.equals("v")) {
            viewEmployees();
        } else if (command.equals("a")) {
            addEmployee();
        } else if (command.equals("m")) {
            modifyEmployee();
        } else if (command.equals("d")) {
            deleteEmployee();
        } else {
            System.out.println("\nInvalid command!\n");
            modifyEmployeeList();
        }
    }

    // MODIFIES: this
    // EFFECT: Prompts user and adds a new employee to the employee list
    private void addEmployee() {
        // Ask user for the employee's name
        String employeeName = askEmployeeName("add");

        // Ask user for the employee's status (hourly or salaried)
        boolean hourlyStatus = askHourlyStatus();

        // Ask user for the employee's wage
        int wage = askWage(hourlyStatus);

        if (wage > 0) {
            // Add employee to employee list
            Employee employeeToAdd = new Employee(employeeName, hourlyStatus, wage);
            try {
                employeeList.addEmployee(employeeToAdd);
            } catch (DuplicateEmployeeException duplicateEmployeeException) {
                System.out.println("Can't add employee that already exists");
            }
            System.out.println(employeeName + " has been successfully added");
        } else {
            // User did not input a valid integer; show error to user
            System.out.println("Failed to add employee. Input must be an integer ");
        }
    }

    // EFFECTS: Prompts user for the new wage and returns their wage or -1 if error
    private int askWage(boolean hourlyStatus) {
        int wage = 0;

        try {
            while (wage <= 0) {
                if (hourlyStatus) {
                    System.out.println("What is the employee's hourly wage?");
                } else {
                    System.out.println("What is the employee's annual salary?");
                }
                wage = Integer.parseInt(input.next());
            }

            // catch error if user inputs a string that Java cannot parse as an int
        } catch (Exception e) {
            return -1;
        }

        return wage;
    }

    // EFFECTS: Prompts user for an employee name, checks if a specific employee
    //          is hourly and returns status as boolean
    private boolean askHourlyStatus() {
        boolean hourlyStatusBool = false;
        String hourlyStatusStr = "";

        while (!(hourlyStatusStr.equals("h") || hourlyStatusStr.equals("s"))) {
            System.out.println("\nIs the employee hourly (h) or salaried (s)?");
            hourlyStatusStr = input.next().toLowerCase();

            if (!(hourlyStatusStr.equals("h") || hourlyStatusStr.equals("s"))) {
                System.out.println("Invalid input");
            } else if (hourlyStatusStr.equals("h")) {
                hourlyStatusBool = true;
            } else if (hourlyStatusStr.equals("s")) {
                hourlyStatusBool = false;
            } else {
                System.out.println("Invalid input. Please tru again");
                askHourlyStatus();
            }
        }
        return hourlyStatusBool;
    }

    // EFFECTS: For every employee in employee list, prints name, wage/salary, and money owned
    private void viewEmployees() {
        if (employeeList.getEmployeeCount() == 0) {
            System.out.println("Employee list is currently empty");
            System.out.println("Start by adding a new employee to the system");
        } else {
            List<Employee> listOfEmployees = employeeList.getAllEmployees();

            for (Employee employee : listOfEmployees) {
                String employeeName = employee.getName();
                int employeeWage = employee.getWage();
                int employeeMoneyOwned = employee.getCurrentOwned();
                boolean employeeHourlyStatus = employee.getHourlyStatus();
                String currentPay = "";

                if (employeeHourlyStatus) {
                    currentPay = employeeWage + " Hourly";
                } else {
                    currentPay = employeeWage + " Annually";
                }

                System.out.println(employeeName + " | $" + currentPay + " | Currently owned: $" + employeeMoneyOwned);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts user for an employee name and deletes them from the employee list
    private void deleteEmployee() {
        if (employeeList.getEmployeeCount() == 0) {
            System.out.println("Employee list is empty. There is no one to delete.");
        } else {
            System.out.println("Please enter the name of the employee to delete:");
            String employeeNameToDelete = input.next();

            try {
                Employee employeeToDelete = employeeList.getEmployee(employeeNameToDelete);
                employeeList.deleteEmployee(employeeToDelete);
                System.out.printf(employeeNameToDelete + " has been successfully deleted from the system.");
            } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
                System.out.println("Unable to delete. No such employee exists in the system.");
            }
        }
    }

    // EFFECTS: Prompts user for an employee's name and returns that employee
    private String askEmployeeName(String action) {
        System.out.println("Please enter the employee name you would like to " + action + "?");
        String employeeName = input.next();
        return employeeName;
    }

    // MODIFIES: this
    // EFFECTS: Prompts user for an employee name and what they'd like to modify
    private void modifyEmployee() {
        String employeeName = askEmployeeName("modify");
        Employee employeeToModify = null;
        try {
            employeeToModify = employeeList.getEmployee(employeeName);
            System.out.println("\tn -> Change " + employeeName + "'s name");
            System.out.println("\ts -> Change " + employeeName + "'s hourly status");
            System.out.println("\tw -> Change " + employeeName + "'s wage");

            String modifyField = input.next();
            modifyField = modifyField.toLowerCase();

            if (modifyField.equals("n")) {
                modifyEmployeeName(employeeToModify);
            } else if (modifyField.equals("s")) {
                modifyEmployeeHourlyStatus(employeeToModify);
            } else if (modifyField.equals("w")) {
                modifyEmployeeWage(employeeToModify);
            } else {
                System.out.println("Invalid selection");
            }
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            System.out.println("Employee name not found");
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts user for a new employee name and modifies the employee name to the new name
    private void modifyEmployeeName(Employee employee) {
        String oldName = employee.getName();
        System.out.println("What would you like the new employee name to be?");
        String newName = input.next();
        employee.changeName(newName);
        System.out.println("Employee has been successfully renamed from " + oldName + " to " + newName);
    }

    // MODIFIES: this
    // EFFECTS: If employee is hourly, changes employee to be salaried,
    //          if employee is salary, changes employee to be hourly
    private void modifyEmployeeHourlyStatus(Employee employee) {

        String newHourlyStatus = employee.changeHourlyStatus();
        System.out.println("Employee has been successfully changed to " + newHourlyStatus);
    }

    // MODIFIES: this
    // EFFECTS: Prompts user for the new wage or salary and updates the employee's wage/salary to the new value
    private void modifyEmployeeWage(Employee employee) {
        String employeeName = employee.getName();
        int currentWage = employee.getWage();
        boolean employeeHourlyStatus = employee.getHourlyStatus();
        String employeePayTimeFrame;
        if (employeeHourlyStatus) {
            employeePayTimeFrame = "hour";
        } else {
            employeePayTimeFrame = "year";
        }
        System.out.println(employeeName + " currently makes $" + currentWage + " per " + employeePayTimeFrame);
        System.out.println("What would you like to change their new rate to be?");
        int newWage = input.nextInt();
        employee.changeWage(newWage);
        System.out.println(employeeName + "'s pay has been updated to $" + newWage + " per " + employeePayTimeFrame);
    }

    // MODIFIES: this
    // EFFECTS: Prompts user for an employee name and records hours or days worked
    private void recordHours() {
        String employeeName = askEmployeeName("record time for");
        Employee employeeToRecordHours = null;
        try {
            employeeToRecordHours = employeeList.getEmployee(employeeName);
            boolean employeeHourlyStatus = employeeToRecordHours.getHourlyStatus();

            if (employeeHourlyStatus) {
                System.out.println("How many hours would you like to record?");
            } else {
                System.out.println("How many days would you like to record??");
            }

            int timeToRecord = 0;

            try {
                timeToRecord = input.nextInt();
                employeeToRecordHours.recordWorkAmount(timeToRecord);
                System.out.println("Time worked has been successfully recorded for " + employeeName);
            } catch (Exception e) {
                System.out.println("Input must be an integer");
            }
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            System.out.println("No such employee exists in the system");
        }
    }

    // MODIFIES: this
    // EFFECTS: Prompts user for an employee name and pays out the money owned
    private void payEmployee() {
        String employeeName = askEmployeeName("pay");

        Employee employeeToPay = null;
        try {
            employeeToPay = employeeList.getEmployee(employeeName);
            employeeToPay.payEmployee();
            System.out.println(employeeToPay.getName() + " has been paid!");
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            System.out.println("Employee name not found in system");
        }
    }
}