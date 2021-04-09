package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

public class Employee implements Writable {
    private String name;
    private boolean hourlyStatus;
    private int wage;
    private int currentOwnedToEmployee;
    private int totalPaidToEmployee;


    // REQUIRES: employeeName has a non-zero length and employeeWage is more than 0
    // EFFECTS: name on account is set to employeeName;
    //          hourlyStatus is set to hourlyStatus;
    //          wage is set to employeeWage;
    //          currentOwnedToEmployee and totalPaidToEmployee are set to 0.
    public Employee(String employeeName, boolean hourlyStatus, int employeeWage) {
        this.name = employeeName;
        this.hourlyStatus = hourlyStatus;
        this.wage = employeeWage;
        this.currentOwnedToEmployee = 0;
        this.totalPaidToEmployee = 0;
    }

    public String getName() {
        return name;
    }

    public boolean getHourlyStatus() {
        return hourlyStatus;
    }

    public int getWage() {
        return wage;
    }

    public int getCurrentOwned() {
        return currentOwnedToEmployee;
    }

    public int getTotalPaid() {
        return totalPaidToEmployee;
    }

    // REQUIRES: newName has a non-zero length
    // MODIFIES: this
    // EFFECTS: changes employee name to a newName
    public void changeName(String newName) {
        this.name = newName;
    }

    // REQUIRES: newWage must be more than 0
    // MODIFIES: this
    // EFFECTS: changes employee wage to a newWage
    public void changeWage(int newWage) {
        this.wage = newWage;
    }

    // MODIFIES: this
    // EFFECTS: changes employee status and wage from hourly to salary or vice versa
    //          and returns the new employee status as a string;
    //          assumes a full time employee works 2080 hours per year
    public String changeHourlyStatus() {
        if (hourlyStatus) {
            this.hourlyStatus = false;
            this.wage = this.wage * 2080;
            return "Salary";
        } else {
            this.hourlyStatus = true;
            this.wage = this.wage / 2080;
            return "Hourly";
        }
    }

    // REQUIRES: ownedToEmployee >= 0;
    // MODIFIES: this
    // EFFECTS: sets the amount amount owned to ownedToEmployee;
    public void setCurrentOwnedToEmployee(int ownedToEmployee) {
        this.currentOwnedToEmployee = ownedToEmployee;
    }

    // REQUIRES: totalPaid >= 0;
    // MODIFIES: this
    // EFFECTS: sets the amount amount paid to the employee to totalPaid;
    public void setTotalPaidToEmployee(int totalPaid) {
        this.totalPaidToEmployee = totalPaid;
    }

    // REQUIRES: if employee is hourly, timeWorked is the hours worked in a week,
    //           if employee is salaried, timeWorked is the number of days worked in pay period
    // MODIFIES: this
    // EFFECTS: adds the time worked to the sum owning to the employee
    public void recordWorkAmount(int timeWorked) {
        if (hourlyStatus) {
            this.currentOwnedToEmployee += this.wage * timeWorked;
        } else {
            this.currentOwnedToEmployee += this.wage / 365 * timeWorked;
        }
    }

    // MODIFIES: this
    // EFFECTS: pays out all amount owning to employee and returns the amount;
    //          sets current owned to employee back to 0
    public int payEmployee() {
        int amountPaid = this.currentOwnedToEmployee;
        this.totalPaidToEmployee += this.currentOwnedToEmployee;
        this.currentOwnedToEmployee = 0;
        return amountPaid;
    }

    // REQUIRES: name has a non-zero length and wage > 0
    // MODIFIES: this
    // EFFECTS: resets employee fields to inputted paramters
    public void reset(String name, boolean hourlyStatus, int wage) {
        this.name = name;
        this.hourlyStatus = hourlyStatus;
        this.wage = wage;
    }

    // EFFECTS: checks if another employee is equal to this employee
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return name.equals(employee.name);
    }

    @Override
    // EFFECTS: returns employee as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("hourlyStatus", hourlyStatus);
        json.put("wage", wage);
        json.put("currentOwnedToEmployee", currentOwnedToEmployee);
        json.put("totalPaidToEmployee", totalPaidToEmployee);
        return json;
    }
}
