package com.ems.dao;

import com.ems.db.DatabaseConnection;
import com.ems.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE CLASS: MySQLEmployeeDAO
 *
 * PURPOSE:
 *   The actual implementation of EmployeeDAO using MySQL + JDBC.
 *   This class contains all raw SQL queries and JDBC boilerplate.
 *   The Service layer never sees this class directly — it only knows EmployeeDAO.
 *
 * KEY JDBC CONCEPTS USED (explain these in interview):
 *
 *   PreparedStatement vs Statement:
 *     - Statement: builds SQL by string concatenation → VULNERABLE to SQL Injection
 *       e.g.  "SELECT * FROM employees WHERE id = " + userInput  ← NEVER do this
 *     - PreparedStatement: uses ? placeholders → DB pre-compiles the query,
 *       user input is always treated as DATA, never as SQL code → SAFE
 *
 *   ResultSet:
 *     - Like a cursor pointing to the query results row by row.
 *     - rs.next() advances to the next row; returns false when no more rows.
 *     - rs.getString("column_name") reads a column value from current row.
 *
 *   try-with-resources:
 *     - Syntax: try (PreparedStatement ps = ...) { ... }
 *     - Automatically calls ps.close() when the block exits, even on exception.
 *     - Prevents resource leaks — very important in production code.
 */
public class MySQLEmployeeDAO implements EmployeeDAO {

    // ─────────────────────────────────────────────────────────────────────────
    // ADD EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Inserts a new row into the employees table.
     *
     * SQL used:
     *   INSERT INTO employees (name, email, phone, job_title, salary, department_id)
     *   VALUES (?, ?, ?, ?, ?, ?)
     *
     * We do NOT include employee_id because it's AUTO_INCREMENT — MySQL generates it.
     */
    @Override
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (name, email, phone, job_title, salary, department_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            // Bind each ? placeholder with the actual value
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getPhone());
            ps.setString(4, employee.getJobTitle());
            ps.setDouble(5, employee.getSalary());
            ps.setInt(6,    employee.getDepartmentId());

            // executeUpdate() runs INSERT/UPDATE/DELETE and returns rows affected
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;   // true = at least 1 row was inserted

        } catch (SQLException e) {
            System.err.println("[DAO] Error adding employee: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET ALL EMPLOYEES
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Fetches every row from the employees table.
     *
     * SQL used:
     *   SELECT * FROM employees ORDER BY employee_id
     *
     * Converts each ResultSet row into an Employee object and collects them.
     */
    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY employee_id";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {   // executeQuery() is for SELECT

            while (rs.next()) {
                // mapRowToEmployee() converts the current ResultSet row → Employee object
                employees.add(mapRowToEmployee(rs));
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error fetching employees: " + e.getMessage());
        }
        return employees;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET EMPLOYEE BY ID
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Searches for exactly one employee by primary key.
     *
     * SQL used:
     *   SELECT * FROM employees WHERE employee_id = ?
     */
    @Override
    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {            // if a row was found
                return mapRowToEmployee(rs);
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error finding employee: " + e.getMessage());
        }
        return null;  // null = not found — caller must handle this
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Updates all updatable fields of an existing employee.
     *
     * SQL used:
     *   UPDATE employees
     *   SET name=?, email=?, phone=?, job_title=?, salary=?, department_id=?
     *   WHERE employee_id=?
     *
     * The WHERE clause is CRITICAL — without it you'd update ALL rows!
     */
    @Override
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET name=?, email=?, phone=?, " +
                     "job_title=?, salary=?, department_id=? WHERE employee_id=?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getPhone());
            ps.setString(4, employee.getJobTitle());
            ps.setDouble(5, employee.getSalary());
            ps.setInt(6,    employee.getDepartmentId());
            ps.setInt(7,    employee.getEmployeeId());  // WHERE clause value

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Error updating employee: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE EMPLOYEE
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Permanently removes an employee row.
     *
     * SQL used:
     *   DELETE FROM employees WHERE employee_id = ?
     */
    @Override
    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPER: mapRowToEmployee
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Converts a single ResultSet row into an Employee object.
     *
     * WHY EXTRACT THIS INTO A PRIVATE METHOD?
     *   getAllEmployees() and getEmployeeById() both need the same mapping logic.
     *   Instead of duplicating 7 lines in each method, we extract once.
     *   This follows the DRY principle: Don't Repeat Yourself.
     *
     * @param rs  ResultSet positioned at the row to read
     * @return    a fully populated Employee object
     */
    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getInt("employee_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("job_title"),
            rs.getDouble("salary"),
            rs.getInt("department_id")
        );
    }
}
