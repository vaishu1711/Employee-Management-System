package com.ems.util;

import com.ems.model.Department;
import com.ems.model.Employee;

import java.util.List;

/**
 * UTILITY CLASS: ConsoleDisplay
 *
 * PURPOSE:
 *   All console output formatting lives here.
 *   Clean separation: Main handles INPUT, ConsoleDisplay handles OUTPUT.
 *
 *   If you ever add a GUI, you'd create a GuiDisplay class with the same
 *   method signatures — again, the separation pays off.
 */
public class ConsoleDisplay {

    private ConsoleDisplay() {}

    // ── Separator line ────────────────────────────────────────────────────────
    private static final String LINE =
        "=".repeat(110);

    // ─────────────────────────────────────────────────────────────────────────
    // PRINT EMPLOYEES TABLE
    // ─────────────────────────────────────────────────────────────────────────
    public static void printEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("\n  No employees found.\n");
            return;
        }
        System.out.println("\n" + LINE);
        System.out.printf("| %-5s | %-20s | %-25s | %-12s | %-20s | %-10s | %-5s |%n",
                "ID", "Name", "Email", "Phone", "Job Title", "Salary", "Dept");
        System.out.println(LINE);
        for (Employee e : employees) {
            System.out.println(e);  // calls Employee.toString()
        }
        System.out.println(LINE + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRINT SINGLE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    public static void printEmployee(Employee e) {
        if (e == null) {
            System.out.println("\n  Employee not found.\n");
            return;
        }
        System.out.println("\n  ── Employee Details ──────────────────────────");
        System.out.println("  ID        : " + e.getEmployeeId());
        System.out.println("  Name      : " + e.getName());
        System.out.println("  Email     : " + e.getEmail());
        System.out.println("  Phone     : " + e.getPhone());
        System.out.println("  Job Title : " + e.getJobTitle());
        System.out.printf ("  Salary    : ₹%.2f%n", e.getSalary());
        System.out.println("  Dept ID   : " + e.getDepartmentId());
        System.out.println("  ──────────────────────────────────────────────\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRINT DEPARTMENTS TABLE
    // ─────────────────────────────────────────────────────────────────────────
    public static void printDepartments(List<Department> departments) {
        if (departments.isEmpty()) {
            System.out.println("\n  No departments found.\n");
            return;
        }
        String deptLine = "=".repeat(52);
        System.out.println("\n" + deptLine);
        System.out.printf("| %-5s | %-20s | %-15s |%n", "ID", "Department Name", "Location");
        System.out.println(deptLine);
        for (Department d : departments) {
            System.out.println(d);  // calls Department.toString()
        }
        System.out.println(deptLine + "\n");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MENU HEADERS
    // ─────────────────────────────────────────────────────────────────────────
    public static void printMainMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   EMPLOYEE MANAGEMENT SYSTEM  v1.0   ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Employee Menu                     ║");
        System.out.println("║  2. Department Menu                   ║");
        System.out.println("║  0. Exit                              ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Enter choice: ");
    }

    public static void printEmployeeMenu() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│        EMPLOYEE OPERATIONS        │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Add New Employee              │");
        System.out.println("│  2. View All Employees            │");
        System.out.println("│  3. Search Employee by ID         │");
        System.out.println("│  4. Update Employee               │");
        System.out.println("│  5. Delete Employee               │");
        System.out.println("│  0. Back to Main Menu             │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("  Enter choice: ");
    }

    public static void printDepartmentMenu() {
        System.out.println("\n┌──────────────────────────────────┐");
        System.out.println("│       DEPARTMENT OPERATIONS       │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Add New Department            │");
        System.out.println("│  2. View All Departments          │");
        System.out.println("│  0. Back to Main Menu             │");
        System.out.println("└──────────────────────────────────┘");
        System.out.print("  Enter choice: ");
    }
}
