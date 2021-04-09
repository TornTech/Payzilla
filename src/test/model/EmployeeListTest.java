package model;

import exceptions.DuplicateEmployeeException;
import exceptions.EmployeeDoesNotExistException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeListTest {
    private EmployeeList emptyEmployeeList;
    private EmployeeList notEmptyEmployeeList;
    private Employee employee1;
    private Employee employee2;
    private Employee employee3;

    @BeforeEach
    void runBefore() {
        emptyEmployeeList = new EmployeeList();
        notEmptyEmployeeList = new EmployeeList();
        employee1 = new Employee("Bob", true, 30);
        employee1.recordWorkAmount(10);
        employee2 = new Employee("Brian", true, 50);
        employee2.recordWorkAmount(40);
        employee3 = new Employee("John", false, 100000);
        employee3.recordWorkAmount(1);
        try {
            notEmptyEmployeeList.addEmployee(employee1);
            notEmptyEmployeeList.addEmployee(employee2);
            notEmptyEmployeeList.addEmployee(employee3);
        } catch (DuplicateEmployeeException e) {
            fail("Should not have got a DuplicateEmployeeException");
        }
    }

    @Test
    void testGetAllEmployees() {
        // Check if two empty lists are the same
        List<Employee> newEmployeeList = new ArrayList<Employee>();
        assertTrue(emptyEmployeeList.getAllEmployees().equals(newEmployeeList));

        // Try adding an employee and make sure both lists are still identical
        newEmployeeList.add(employee1);
        try {
            emptyEmployeeList.addEmployee(employee1);
            assertTrue(emptyEmployeeList.getAllEmployees().equals(newEmployeeList));
        } catch (DuplicateEmployeeException e) {
            fail("Should not have received a DuplicateEmployeeException");
        }
    }

    @Test
    void testGetEmployee() {
        try {
            // Check if the employee returned is correct
            String nameToSearch = employee1.getName();
            Employee returnedEmployee = notEmptyEmployeeList.getEmployee(nameToSearch);
            assertTrue(returnedEmployee.equals(employee1));
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            fail("Should not have caught an EmployeeDoesNotExistException");
        }
    }

    @Test
    void testGetFakeEmployee() {
        try {
            // Check an employee doesn't exist
            Employee returnedEmployee = notEmptyEmployeeList.getEmployee("doesNotExist");
            fail("Should have thrown an EmployeeDoesNotExistException");
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            // Successfully caught an EmployeeDoesNotExistException
        }
    }

    @Test
    void testContains() {
        assertFalse(emptyEmployeeList.contains(employee1));
        assertFalse(emptyEmployeeList.contains(employee2));
        assertFalse(emptyEmployeeList.contains(employee3));
        assertTrue(notEmptyEmployeeList.contains(employee1));
        assertTrue(notEmptyEmployeeList.contains(employee2));
        assertTrue(notEmptyEmployeeList.contains(employee3));
    }

    @Test
    void testGetEmployeeCount() {
        try {
            // Check that an empty employee list returns 0
            assertEquals(0, emptyEmployeeList.getEmployeeCount());

            // Add one employee and check it returns 1
            emptyEmployeeList.addEmployee(employee1);
            assertEquals(1, emptyEmployeeList.getEmployeeCount());

            // Add two more employees and check it returns 3
            emptyEmployeeList.addEmployee(employee2);
            emptyEmployeeList.addEmployee(employee3);
            assertEquals(3, emptyEmployeeList.getEmployeeCount());
        } catch (DuplicateEmployeeException e) {
            fail("Should not have got a DuplicateEmployeeException");
        }
    }

    @Test
    void testAddEmployee() {
        try {
            // Add a new employee
            emptyEmployeeList.addEmployee(employee1);

            // Check if employee has been added by checking first index of returned array
            List<Employee> returnedList = emptyEmployeeList.getAllEmployees();
            assertTrue(employee1.equals(returnedList.get(0)));

            // Add a second employee
            emptyEmployeeList.addEmployee(employee2);

            // Check if employee has been added by checking second index of returned array
            returnedList = emptyEmployeeList.getAllEmployees();
            assertTrue(employee2.equals(returnedList.get(1)));
        } catch (DuplicateEmployeeException e) {
            fail("Should not have got a DuplicateEmployeeException");
        }
    }

    @Test
    void testAddEmployeeDuplicate() {
        try {
            // Add a new employee
            emptyEmployeeList.addEmployee(employee1);

            // Check if employee has been added by checking first index of returned array
            List<Employee> returnedList = emptyEmployeeList.getAllEmployees();
            assertTrue(employee1.equals(returnedList.get(0)));

            // Add a second employee
            emptyEmployeeList.addEmployee(employee2);

            // Check if employee has been added by checking second index of returned array
            returnedList = emptyEmployeeList.getAllEmployees();
            assertTrue(employee2.equals(returnedList.get(1)));

            // Try adding the second employee again
            emptyEmployeeList.addEmployee(employee2);
            fail("Should have got a DuplicateEmployeeException");
        } catch (DuplicateEmployeeException e) {
            // Successfully caught exception
            // Check that the list still contains the first two employees
            assertEquals(2, emptyEmployeeList.getEmployeeCount());
            List<Employee> returnedList = emptyEmployeeList.getAllEmployees();
            assertTrue(employee1.equals(returnedList.get(0)));
            assertTrue(employee2.equals(returnedList.get(1)));
        }
    }

    @Test
    void testDeleteEmployee() {
        try {
            // Check that employee list has 3 employees to begin with
            assertEquals(3, notEmptyEmployeeList.getEmployeeCount());

            // Delete one employee and check that there's 2 employees remaining
            notEmptyEmployeeList.deleteEmployee(employee1);
            assertEquals(2, notEmptyEmployeeList.getEmployeeCount());

            // Delete the remaining two employees and check that there's zero employees remaining
            notEmptyEmployeeList.deleteEmployee(employee2);
            notEmptyEmployeeList.deleteEmployee(employee3);
            assertEquals(0, notEmptyEmployeeList.getEmployeeCount());
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            fail("Should not have caught an EmployeeDoesNotExistException");
        }
    }

    @Test
    void testDeleteFakeEmployee() {
        try {
            // Attempt to delete an employee that does not exist
            emptyEmployeeList.deleteEmployee(employee1);
            fail("Should have thrown a EmployeeDoesNotExistException");
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            // Successfully caught exception
        }
    }

    @Test
    void testEmptyListToJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("employees", jsonArray);
        assertEquals(jsonObject.toString(), emptyEmployeeList.toJson().toString());
    }

    @Test
    void testNonEmptyListToJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(employee1.toJson());
        jsonArray.put(employee2.toJson());
        jsonArray.put(employee3.toJson());
        jsonObject.put("employees", jsonArray);
        assertEquals(jsonObject.toString(), notEmptyEmployeeList.toJson().toString());
    }
}
