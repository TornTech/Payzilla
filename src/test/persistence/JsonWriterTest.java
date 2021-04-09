package persistence;

import exceptions.DuplicateEmployeeException;
import model.Employee;
import model.EmployeeList;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            EmployeeList employeeList = new EmployeeList();
            JsonWriter writer = new JsonWriter("./data/this\0isIllegal:AVOID.json");
            writer.open();
            fail("Expected IOException");
        } catch(IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyEmployeeList() {
        try {
            EmployeeList employeeList = new EmployeeList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyEmployeeList");
            writer.open();
            writer.write(employeeList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyEmployeeList");
            employeeList = reader.read();
            assertEquals(0, employeeList.getEmployeeCount());
        } catch (IOException e) {
            fail("IOException was thrown where no exception was expected");
        }
    }

    @Test
    void testWriterGeneralEmployeeList() {
        try {
            EmployeeList employeeList = new EmployeeList();
            Employee employeeBrian = new Employee("Brian", true, 30);
            employeeBrian.setTotalPaidToEmployee(123456);
            employeeBrian.setCurrentOwnedToEmployee(445);
            Employee employeeBob = new Employee("Bob", false, 55000);
            Employee employeeAshley = new Employee("Ashley", false, 110000);
            employeeAshley.setTotalPaidToEmployee(200);
            employeeAshley.setCurrentOwnedToEmployee(25000);
            employeeList.addEmployee(employeeBrian);
            employeeList.addEmployee(employeeBob);
            employeeList.addEmployee(employeeAshley);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralEmployeeList.json");
            writer.open();
            writer.write(employeeList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralEmployeeList.json");
            employeeList = reader.read();
            assertEquals(3, employeeList.getEmployeeCount());
            checkEmployee(true, 123456, "Brian", 445, 30, employeeBrian);
            checkEmployee(false, 0, "Bob", 0, 55000, employeeBob);
            checkEmployee(false, 200, "Ashley", 25000, 110000, employeeAshley);

        } catch (IOException e) {
            fail("IOException was thrown where no exception was expected");
        } catch (DuplicateEmployeeException e) {
            fail("DuplicateEmployeeException was thrown where no exception was expected");
        }
    }
}
