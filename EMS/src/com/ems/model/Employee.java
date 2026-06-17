package com.ems.model;

/**
 * MODEL CLASS: Employee
 *
 * PURPOSE:
 *   Represents a single row from the 'employees' table.
 *   Also demonstrates INHERITANCE — Employee extends Person,
 *   inheriting name and email fields without rewriting them.
 *
 * OOP CONCEPTS USED:
 *   - Inheritance : Employee IS-A Person (extends Person).
 *   - Encapsulation: Employee-specific fields are private.
 *   - Constructor chaining: Uses super(...) to call Person's constructor.
 *   - Method Overriding: Overrides toString() from Object class.
 */
public class Employee extends Person {

    // ── Employee-specific fields (Person already has name + email) ────────────
    private int    employeeId;    // Primary key
    private String phone;         // Contact number
    private String jobTitle;      // e.g. "Software Engineer"
    private double salary;        // Monthly salary
    private int    departmentId;  // Foreign key → departments.department_id

    // ── Constructor 1: for INSERT (no ID yet — DB auto-generates it) ──────────
    public Employee(String name, String email, String phone,
                    String jobTitle, double salary, int departmentId) {
        super(name, email);               // call Person constructor
        this.phone        = phone;
        this.jobTitle     = jobTitle;
        this.salary       = salary;
        this.departmentId = departmentId;
    }

    // ── Constructor 2: for SELECT (all fields including DB-generated ID) ──────
    public Employee(int employeeId, String name, String email, String phone,
                    String jobTitle, double salary, int departmentId) {
        super(name, email);
        this.employeeId   = employeeId;
        this.phone        = phone;
        this.jobTitle     = jobTitle;
        this.salary       = salary;
        this.departmentId = departmentId;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getEmployeeId()   { return employeeId;   }
    public String getPhone()        { return phone;        }
    public String getJobTitle()     { return jobTitle;     }
    public double getSalary()       { return salary;       }
    public int    getDepartmentId() { return departmentId; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setEmployeeId(int employeeId)     { this.employeeId   = employeeId;   }
    public void setPhone(String phone)            { this.phone        = phone;        }
    public void setJobTitle(String jobTitle)      { this.jobTitle     = jobTitle;     }
    public void setSalary(double salary)          { this.salary       = salary;       }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    // ── Override toString for formatted console output ────────────────────────
    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %-25s | %-12s | %-20s | %-10.2f | %-5d |",
                employeeId, getName(), getEmail(), phone, jobTitle, salary, departmentId);
    }
}
