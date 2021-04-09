package persistence;

import exceptions.DuplicateEmployeeException;
import model.Employee;
import model.EmployeeList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads employeeList from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public EmployeeList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseEmployeeList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses employeeList from JSON object and returns it
    private EmployeeList parseEmployeeList(JSONObject jsonObject) {
        EmployeeList employeeList = new EmployeeList();
        addEmployees(employeeList, jsonObject);
        return employeeList;
    }

    // MODIFIES: employeeList
    // EFFECTS: parses employees from JSON object and adds them to employeeList
    private void addEmployees(EmployeeList employeeList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("employees");

        for (Object json : jsonArray) {
            JSONObject nextEmployee = (JSONObject) json;
            addEmployee(employeeList, nextEmployee);
        }
    }

    // MODIFIES: employeeList
    // EFFECTS: parses employee from JSON object and adds it to employeeList
    private void addEmployee(EmployeeList employeeList, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        boolean hourlyStatus = jsonObject.getBoolean("hourlyStatus");
        int wage = jsonObject.getInt("wage");
        int currentOwnedToEmployee = jsonObject.getInt("currentOwnedToEmployee");
        int totalPaidToEmployee = jsonObject.getInt("totalPaidToEmployee");

        Employee employee = new Employee(name, hourlyStatus, wage);
        employee.setCurrentOwnedToEmployee(currentOwnedToEmployee);
        employee.setTotalPaidToEmployee(totalPaidToEmployee);
        try {
            employeeList.addEmployee(employee);
        } catch (DuplicateEmployeeException duplicateEmployeeException) {
            // Skip adding duplicate employee
        }
    }
}
