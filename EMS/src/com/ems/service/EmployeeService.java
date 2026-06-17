package com.ems.service;

import com.ems.dao.DepartmentDAO;
import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;
import com.ems.util.InputValidator;

import java.util.List;

/**
 * SERVICE CLASS: EmployeeService
 *
 * PURPOSE:
 *   The business logic layer. It sits BETWEEN the UI (Main) and the database (DAO).
 *
 * RESPONSIBILITIES:
 *   1. Validate input before passing to the DAO
 *   2. Enforce business rules (e.g. salary can't be negative)
 *   3. Coordinate between EmployeeDAO and DepartmentDAO when needed
 *   4. Return meaningful success/failure messages
 *
 * WHY HAVE A SERVICE LAYER AT ALL?
 *   Without it, you'd put validation logic in Main (messy!) or in the DAO (wrong layer).
 *   The service layer ensures clean separation of concerns:
 *     Main  → only handles UI (menus, Scanner input)
 *     Service → only handles business rules
 *     DAO   → only handles SQL
 *
 * LAYERED ARCHITECTURE DATA FLOW:
 *   User input → Main → Service → DAO → MySQL → back up the same chain
 */
public class EmployeeService {

    // The service holds references to DAOs via INTERFACES (not concrete classes)
    // This is the Dependency Injection principle — easy to swap implementations
    private final EmployeeDAO   employeeDAO;
    private final DepartmentDAO departmentDAO;

    // ── Constructor injection: DAOs are provided when the service is created ──
    public EmployeeService(EmployeeDAO employeeDAO, DepartmentDAO departmentDAO) {
        this.employeeDAO   = employeeDAO;
        this.departmentDAO = departmentDAO;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ADD EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Business rules enforced here:
     *   - Name must not be blank
     *   - Email must be in valid format
     *   - Phone must be 10 digits
     *   - Salary must be positive
     *   - Department must exist in the DB (referential integrity check at app level)
     */
    public String addEmployee(String name, String email, String phone,
                               String jobTitle, double salary, int departmentId) {

        // ── Validation ────────────────────────────────────────────────────────
        if (!InputValidator.isNotBlank(name))
            return "ERROR: Employee name cannot be blank.";

        if (!InputValidator.isValidEmail(email))
            return "ERROR: Invalid email format. Use: name@domain.com";

        if (!InputValidator.isValidPhone(phone))
            return "ERROR: Phone must be exactly 10 digits.";

        if (!InputValidator.isNotBlank(jobTitle))
            return "ERROR: Job title cannot be blank.";

        if (salary <= 0)
            return "ERROR: Salary must be a positive number.";

        if (!departmentDAO.departmentExists(departmentId))
            return "ERROR: Department ID " + departmentId + " does not exist. Add the department first.";

        // ── All validations passed — create the model object and pass to DAO ──
        Employee employee = new Employee(name, email, phone, jobTitle, salary, departmentId);
        boolean success = employeeDAO.addEmployee(employee);

        return success
            ? "SUCCESS: Employee '" + name + "' added successfully!"
            : "ERROR: Failed to add employee. Please try again.";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET ALL EMPLOYEES
    // ─────────────────────────────────────────────────────────────────────────
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET EMPLOYEE BY ID
    // ─────────────────────────────────────────────────────────────────────────
    public Employee getEmployeeById(int employeeId) {
        if (employeeId <= 0) {
            System.out.println("ERROR: Employee ID must be a positive number.");
            return null;
        }
        return employeeDAO.getEmployeeById(employeeId);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Before updating, we first check if the employee actually exists.
     * This prevents silent failures (updating 0 rows looks like success).
     */
    public String updateEmployee(int employeeId, String name, String email,
                                  String phone, String jobTitle,
                                  double salary, int departmentId) {

        // Check employee exists
        Employee existing = employeeDAO.getEmployeeById(employeeId);
        if (existing == null)
            return "ERROR: No employee found with ID " + employeeId + ".";

        // Validate new values
        if (!InputValidator.isNotBlank(name))          return "ERROR: Name cannot be blank.";
        if (!InputValidator.isValidEmail(email))        return "ERROR: Invalid email format.";
        if (!InputValidator.isValidPhone(phone))        return "ERROR: Phone must be 10 digits.";
        if (!InputValidator.isNotBlank(jobTitle))       return "ERROR: Job title cannot be blank.";
        if (salary <= 0)                                return "ERROR: Salary must be positive.";
        if (!departmentDAO.departmentExists(departmentId))
            return "ERROR: Department ID " + departmentId + " does not exist.";

        // Build updated Employee object (must include the ID for WHERE clause in SQL)
        Employee updated = new Employee(employeeId, name, email, phone, jobTitle, salary, departmentId);
        boolean success = employeeDAO.updateEmployee(updated);

        return success
            ? "SUCCESS: Employee ID " + employeeId + " updated successfully!"
            : "ERROR: Update failed.";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    public String deleteEmployee(int employeeId) {
        if (employeeDAO.getEmployeeById(employeeId) == null)
            return "ERROR: No employee found with ID " + employeeId + ".";

        boolean success = employeeDAO.deleteEmployee(employeeId);
        return success
            ? "SUCCESS: Employee ID " + employeeId + " deleted."
            : "ERROR: Delete failed.";
    }
}
