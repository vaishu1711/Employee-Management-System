package com.ems.main;

import com.ems.dao.DepartmentDAO;
import com.ems.dao.EmployeeDAO;
import com.ems.dao.MySQLDepartmentDAO;
import com.ems.dao.MySQLEmployeeDAO;
import com.ems.db.DatabaseConnection;
import com.ems.model.Employee;
import com.ems.service.DepartmentService;
import com.ems.service.EmployeeService;
import com.ems.util.ConsoleDisplay;
import com.ems.util.InputValidator;

import java.util.Scanner;

/**
 * ENTRY POINT: Main
 *
 * PURPOSE:
 *   The starting class of the application. Contains the main() method.
 *   Responsible ONLY for:
 *     1. Wiring up all the layers (creating DAO, Service objects)
 *     2. Reading user input from the console (Scanner)
 *     3. Calling the appropriate Service method
 *     4. Displaying results via ConsoleDisplay
 *
 * THIS CLASS DOES NOT:
 *   - Contain any SQL
 *   - Contain any business rules
 *   - Directly touch model objects (mostly)
 *
 * DATA FLOW WALKTHROUGH (explain this in interview):
 *   User types "1" to add employee
 *   → Main reads name, email, phone… from Scanner
 *   → Main calls employeeService.addEmployee(...)
 *   → EmployeeService validates inputs
 *   → EmployeeService calls employeeDAO.addEmployee(employee)
 *   → MySQLEmployeeDAO runs PreparedStatement against MySQL
 *   → MySQL inserts row → returns rows affected = 1
 *   → DAO returns true → Service returns "SUCCESS: ..." → Main prints it
 */
public class Main {

    // Scanner reads user keystrokes from the terminal
    private static final Scanner scanner = new Scanner(System.in);

    // Service layer references (hold as interface type — polymorphism)
    private static EmployeeService   employeeService;
    private static DepartmentService departmentService;

    public static void main(String[] args) {

        // ── 1. Wire up the application (Dependency Injection manually) ────────
        // DAOs are created as concrete MySQL implementations
        EmployeeDAO   employeeDAO   = new MySQLEmployeeDAO();
        DepartmentDAO departmentDAO = new MySQLDepartmentDAO();

        // Services receive DAOs via their constructors
        employeeService   = new EmployeeService(employeeDAO, departmentDAO);
        departmentService = new DepartmentService(departmentDAO);

        System.out.println("\n  Connecting to database...");

        // ── 2. Test the DB connection early — fail fast if config is wrong ────
        try {
            DatabaseConnection.getConnection();
        } catch (Exception e) {
            System.err.println("\n  FATAL: Cannot connect to database.");
            System.err.println("  Check URL/username/password in DatabaseConnection.java");
            System.err.println("  Error: " + e.getMessage());
            return;  // exit the application
        }

        // ── 3. Main application loop ──────────────────────────────────────────
        int choice;
        do {
            ConsoleDisplay.printMainMenu();
            choice = InputValidator.parseIntSafe(scanner.nextLine());

            switch (choice) {
                case 1 -> employeeMenu();
                case 2 -> departmentMenu();
                case 0 -> System.out.println("\n  Goodbye! Closing application...");
                default -> System.out.println("  Invalid choice. Please enter 0, 1, or 2.");
            }
        } while (choice != 0);

        // ── 4. Clean up: close DB connection before exit ──────────────────────
        DatabaseConnection.closeConnection();
        scanner.close();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // EMPLOYEE MENU HANDLER
    // ═════════════════════════════════════════════════════════════════════════
    private static void employeeMenu() {
        int choice;
        do {
            ConsoleDisplay.printEmployeeMenu();
            choice = InputValidator.parseIntSafe(scanner.nextLine());

            switch (choice) {
                case 1 -> handleAddEmployee();
                case 2 -> handleViewAllEmployees();
                case 3 -> handleSearchEmployee();
                case 4 -> handleUpdateEmployee();
                case 5 -> handleDeleteEmployee();
                case 0 -> System.out.println("  Returning to main menu...");
                default -> System.out.println("  Invalid choice.");
            }
        } while (choice != 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ADD EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleAddEmployee() {
        System.out.println("\n  ── Add New Employee ──────────────────────────");

        // First, show available departments so user knows valid IDs
        System.out.println("  Available Departments:");
        ConsoleDisplay.printDepartments(departmentService.getAllDepartments());

        System.out.print("  Name       : ");
        String name = scanner.nextLine();

        System.out.print("  Email      : ");
        String email = scanner.nextLine();

        System.out.print("  Phone      : ");
        String phone = scanner.nextLine();

        System.out.print("  Job Title  : ");
        String jobTitle = scanner.nextLine();

        System.out.print("  Salary     : ");
        double salary = InputValidator.parseDoubleSafe(scanner.nextLine());

        System.out.print("  Dept ID    : ");
        int deptId = InputValidator.parseIntSafe(scanner.nextLine());

        // Service validates and returns a message string
        String result = employeeService.addEmployee(name, email, phone, jobTitle, salary, deptId);
        System.out.println("\n  " + result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // VIEW ALL EMPLOYEES
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleViewAllEmployees() {
        System.out.println("\n  ── All Employees ─────────────────────────────");
        ConsoleDisplay.printEmployees(employeeService.getAllEmployees());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SEARCH EMPLOYEE BY ID
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleSearchEmployee() {
        System.out.println("\n  ── Search Employee ───────────────────────────");
        System.out.print("  Enter Employee ID: ");
        int id = InputValidator.parseIntSafe(scanner.nextLine());

        Employee employee = employeeService.getEmployeeById(id);
        ConsoleDisplay.printEmployee(employee);  // handles null internally
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleUpdateEmployee() {
        System.out.println("\n  ── Update Employee ───────────────────────────");
        System.out.print("  Enter Employee ID to update: ");
        int id = InputValidator.parseIntSafe(scanner.nextLine());

        // Show current data first so user knows what's there
        Employee current = employeeService.getEmployeeById(id);
        if (current == null) {
            System.out.println("\n  ERROR: Employee ID " + id + " not found.");
            return;
        }

        System.out.println("\n  Current data shown in brackets. Press ENTER to keep, or type new value:\n");

        System.out.printf("  Name [%s]: ", current.getName());
        String name = scanner.nextLine();
        if (name.isBlank()) name = current.getName();

        System.out.printf("  Email [%s]: ", current.getEmail());
        String email = scanner.nextLine();
        if (email.isBlank()) email = current.getEmail();

        System.out.printf("  Phone [%s]: ", current.getPhone());
        String phone = scanner.nextLine();
        if (phone.isBlank()) phone = current.getPhone();

        System.out.printf("  Job Title [%s]: ", current.getJobTitle());
        String jobTitle = scanner.nextLine();
        if (jobTitle.isBlank()) jobTitle = current.getJobTitle();

        System.out.printf("  Salary [%.2f]: ", current.getSalary());
        String salaryStr = scanner.nextLine();
        double salary = salaryStr.isBlank()
            ? current.getSalary()
            : InputValidator.parseDoubleSafe(salaryStr);

        System.out.printf("  Dept ID [%d]: ", current.getDepartmentId());
        String deptStr = scanner.nextLine();
        int deptId = deptStr.isBlank()
            ? current.getDepartmentId()
            : InputValidator.parseIntSafe(deptStr);

        String result = employeeService.updateEmployee(id, name, email, phone, jobTitle, salary, deptId);
        System.out.println("\n  " + result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleDeleteEmployee() {
        System.out.println("\n  ── Delete Employee ───────────────────────────");
        System.out.print("  Enter Employee ID to delete: ");
        int id = InputValidator.parseIntSafe(scanner.nextLine());

        // Show employee so user can confirm before deleting
        Employee emp = employeeService.getEmployeeById(id);
        if (emp == null) {
            System.out.println("\n  ERROR: Employee ID " + id + " not found.");
            return;
        }

        ConsoleDisplay.printEmployee(emp);
        System.out.print("  Are you sure you want to delete this employee? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            String result = employeeService.deleteEmployee(id);
            System.out.println("\n  " + result);
        } else {
            System.out.println("\n  Delete cancelled.");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // DEPARTMENT MENU HANDLER
    // ═════════════════════════════════════════════════════════════════════════
    private static void departmentMenu() {
        int choice;
        do {
            ConsoleDisplay.printDepartmentMenu();
            choice = InputValidator.parseIntSafe(scanner.nextLine());

            switch (choice) {
                case 1 -> handleAddDepartment();
                case 2 -> handleViewAllDepartments();
                case 0 -> System.out.println("  Returning to main menu...");
                default -> System.out.println("  Invalid choice.");
            }
        } while (choice != 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ADD DEPARTMENT
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleAddDepartment() {
        System.out.println("\n  ── Add New Department ────────────────────────");

        System.out.print("  Department Name : ");
        String name = scanner.nextLine();

        System.out.print("  Location        : ");
        String location = scanner.nextLine();

        String result = departmentService.addDepartment(name, location);
        System.out.println("\n  " + result);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // VIEW ALL DEPARTMENTS
    // ─────────────────────────────────────────────────────────────────────────
    private static void handleViewAllDepartments() {
        System.out.println("\n  ── All Departments ───────────────────────────");
        ConsoleDisplay.printDepartments(departmentService.getAllDepartments());
    }
}
