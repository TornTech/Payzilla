package model;

import exceptions.DuplicateEmployeeException;
import exceptions.EmployeeDoesNotExistException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeList implements Writable {
    private List<Employee> employeeList;

    // EFFECTS: initiates a new employee list
    public EmployeeList() {
        employeeList = new ArrayList<Employee>();
    }

    // EFFECTS: returns the list of all employees
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    // EFFECTS: returns an employee based on the employee's name
    //          Throws EmployeeDoesNotExistException if no such employee exists
    public Employee getEmployee(String employeeName) throws EmployeeDoesNotExistException {
        Employee employeeToReturn = null;

        for (Employee employee : employeeList) {
            if (employee.getName().equals(employeeName)) {
                employeeToReturn = employee;
            }
        }

        if (Objects.isNull(employeeToReturn)) {
            throw new EmployeeDoesNotExistException();
        } else {
            return employeeToReturn;
        }
    }

    // EFFECTS: returns true if an employee already exists in the employee list
    public boolean contains(Employee employee) {
        return employeeList.contains(employee);
    }

    // EFFECTS: returns the count of the employees in the employeeList
    public int getEmployeeCount() {
        return employeeList.size();
    }

    // MODIFIES: this
    // EFFECTS: adds a new employee to employeeList
    //          throws DuplicateEmployeeException if employee with same name already exists
    public void addEmployee(Employee employee) throws DuplicateEmployeeException {
        if (employeeList.contains(employee)) {
            throw new DuplicateEmployeeException();
        } else {
            employeeList.add(employee);
        }
    }

    // MODIFIES: this
    // EFFECTS: deletes a given employee from the employeeList
    //          throws EmployeeDoesNotExistException if employee to delete does not exist
    public void deleteEmployee(Employee employee) throws EmployeeDoesNotExistException {
        if (!employeeList.contains(employee)) {
            throw new EmployeeDoesNotExistException();
        } else {
            employeeList.remove(employee);
        }
    }

    @Override
    // EFFECTS: Converts employeeList to JSON object and returns it
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("employees", employeesToJson());
        return json;
    }

    // EFFECTS: returns employees in this employeeList as a JSON array
    private JSONArray employeesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Employee employee : employeeList) {
            jsonArray.put(employee.toJson());
        }

        return jsonArray;
    }
}
