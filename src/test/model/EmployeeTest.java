package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    private Employee testEmployee;

    @BeforeEach
    void runBefore() {
        testEmployee = new Employee("John", true, 35);
    }

    @Test
    void testConstructor() {
        assertEquals("John", testEmployee.getName());
        assertTrue(testEmployee.getHourlyStatus());
        assertEquals(35, testEmployee.getWage());
        assertEquals(0, testEmployee.getCurrentOwned());
        assertEquals(0, testEmployee.getTotalPaid());
    }

    @Test
    void testChangeName() {
        assertEquals("John", testEmployee.getName());
        testEmployee.changeName("Brian");
        assertEquals("Brian", testEmployee.getName());
    }

    @Test
    void testChangeWage() {
        assertEquals(35, testEmployee.getWage());
        testEmployee.changeWage(50);
        assertEquals(50, testEmployee.getWage());
    }

    @Test
    void testChangeHourlyStatus() {
        assertTrue(testEmployee.getHourlyStatus());
        assertEquals("Salary", testEmployee.changeHourlyStatus());
        assertEquals(35 * 2080, testEmployee.getWage());
        assertFalse(testEmployee.getHourlyStatus());
        assertEquals("Hourly", testEmployee.changeHourlyStatus());
        assertEquals(35, testEmployee.getWage());
        assertTrue(testEmployee.getHourlyStatus());
    }

    @Test
    void testRecordWorkAmount() {
        testEmployee.recordWorkAmount(30);
        assertEquals(30 * 35, testEmployee.getCurrentOwned());
        testEmployee.changeHourlyStatus();
        testEmployee.recordWorkAmount(5);
        assertEquals(5 * (35 * 2080 / 365) + (30 * 35), testEmployee.getCurrentOwned());
        testEmployee.changeHourlyStatus();
        testEmployee.changeWage(50);
        testEmployee.recordWorkAmount(40);
        assertEquals(5 * (35 * 2080 / 365) + (30 * 35) + 40 * 50, testEmployee.getCurrentOwned());
    }

    @Test
    void testPayEmployee() {
        testEmployee.recordWorkAmount(30);
        assertEquals(0, testEmployee.getTotalPaid());
        assertEquals(30 * 35, testEmployee.getCurrentOwned());
        assertEquals(30 * 35, testEmployee.payEmployee());
        assertEquals(0, testEmployee.payEmployee());
        testEmployee.recordWorkAmount(50);
        testEmployee.recordWorkAmount(40);
        assertEquals(90 * 35, testEmployee.payEmployee());
        assertEquals(0, testEmployee.getCurrentOwned());
    }

    @Test
    void testSetCurrentOwnedToEmployee() {
        assertEquals(0, testEmployee.getCurrentOwned());
        testEmployee.setCurrentOwnedToEmployee(300);
        assertEquals(300, testEmployee.getCurrentOwned());
        testEmployee.setCurrentOwnedToEmployee(25142);
        assertEquals(25142, testEmployee.getCurrentOwned());
    }

    @Test
    void testSetTotalPaidToEmployee() {
        assertEquals(0, testEmployee.getTotalPaid());
        testEmployee.setTotalPaidToEmployee(1);
        assertEquals(1, testEmployee.getTotalPaid());
        testEmployee.setTotalPaidToEmployee(593330);
        assertEquals(593330, testEmployee.getTotalPaid());
    }

    @Test
    void testReset() {
        testEmployee.reset("Brian", false, 80000);
        assertEquals(testEmployee.getName(), "Brian");
        assertEquals(testEmployee.getHourlyStatus(), false);
        assertEquals(testEmployee.getWage(), 80000);
    }

    @Test
    void testEqualsNullInput() {
        assertFalse(testEmployee.equals(null));
    }

    @Test
    void testEqualsDifferentObject() {
        assertFalse(testEmployee.equals(new EmployeeList()));
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(testEmployee.equals(testEmployee));
    }

    @Test
    void testEqualsSameEmployeeName() {
        Employee employee = new Employee("John", false, 30);
        assertTrue(testEmployee.equals(employee));
    }

    @Test
    void testEqualsDifferentEmployeeName() {
        Employee employee = new Employee("Bob", false, 30);
        assertFalse(testEmployee.equals(employee));
    }

    @Test
    void testToJson() {
        JSONObject json = new JSONObject();
        json.put("name", "John");
        json.put("hourlyStatus", true);
        json.put("wage", 35);
        json.put("currentOwnedToEmployee", 0);
        json.put("totalPaidToEmployee", 0);
        assertEquals(json.toString(), testEmployee.toJson().toString());
    }
}