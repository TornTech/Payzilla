package persistence;

import model.Employee;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkEmployee(boolean hourlyStatus, int totalPaidToEmployee, String name, int currentOwnedToEmployee,
                                 int wage, Employee employee) {
        assertEquals(name, employee.getName());
        assertEquals(hourlyStatus, employee.getHourlyStatus());
        assertEquals(wage, employee.getWage());
        assertEquals(currentOwnedToEmployee, employee.getCurrentOwned());
        assertEquals(totalPaidToEmployee, employee.getTotalPaid());
    }
}
