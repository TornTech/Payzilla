package persistence;

import exceptions.EmployeeDoesNotExistException;
import model.Employee;
import model.EmployeeList;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/thisFileDoesNotExist.json");
        try {
            EmployeeList employeeList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyEmployeeList() {
        JsonReader reader = new JsonReader("./data/testEmptyEmployeeList.json");
        try {
            EmployeeList employeeList = reader.read();
            assertEquals(0, employeeList.getEmployeeCount());
        } catch (IOException e) {
            fail("Unable to read from file");
        }
    }

    @Test
    void testReaderGenericEmployeeList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralEmployeeList.json");
        try {
            EmployeeList employeeList = reader.read();
            assertEquals(4, employeeList.getEmployeeCount());
            Employee employeeBen = employeeList.getEmployee("Ben");
            Employee employeeKyle = employeeList.getEmployee("Kyle");
            Employee employeeJordana = employeeList.getEmployee("Jordana");
            Employee employeeAlex = employeeList.getEmployee("Alex");
            checkEmployee(false, 0, "Ben", 8190, 100000, employeeBen);
            checkEmployee(true, 0, "Kyle", 16000, 40, employeeKyle);
            checkEmployee(false, 100, "Jordana", 846, 103000, employeeJordana);
            checkEmployee(true, 52000, "Alex", 0, 15, employeeAlex);
        } catch(IOException e) {
            fail("Unable to read from file");
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            fail("Caught EmployeeDoesNotExistException where not expected");
        }
    }

    @Test
    void testReaderDuplicateEmployeeList() {
        JsonReader reader = new JsonReader("./data/testReaderDuplicatesEmployeeList.json");
        try {
            EmployeeList employeeList = reader.read();
            assertEquals(4, employeeList.getEmployeeCount());
            Employee employeeBen = employeeList.getEmployee("Ben");
            Employee employeeKyle = employeeList.getEmployee("Kyle");
            Employee employeeJordana = employeeList.getEmployee("Jordana");
            Employee employeeAlex = employeeList.getEmployee("Alex");
            checkEmployee(false, 0, "Ben", 8190, 100000, employeeBen);
            checkEmployee(true, 0, "Kyle", 16000, 40, employeeKyle);
            checkEmployee(false, 100, "Jordana", 846, 103000, employeeJordana);
            checkEmployee(true, 52000, "Alex", 0, 15, employeeAlex);
        } catch(IOException e) {
            fail("Unable to read from file");
        } catch (EmployeeDoesNotExistException employeeDoesNotExistException) {
            fail("Caught EmployeeDoesNotExistException where not expected");
        }
    }
}
