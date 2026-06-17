package com.ems.dao;

import com.ems.db.DatabaseConnection;
import com.ems.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE CLASS: MySQLDepartmentDAO
 *
 * PURPOSE:
 *   MySQL/JDBC implementation of DepartmentDAO.
 *   Handles all SQL operations for the departments table.
 */
public class MySQLDepartmentDAO implements DepartmentDAO {

    // ─────────────────────────────────────────────────────────────────────────
    // ADD DEPARTMENT
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public boolean addDepartment(Department department) {
        String sql = "INSERT INTO departments (department_name, location) VALUES (?, ?)";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            ps.setString(1, department.getDepartmentName());
            ps.setString(2, department.getLocation());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Error adding department: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET ALL DEPARTMENTS
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments ORDER BY department_id";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                departments.add(new Department(
                    rs.getInt("department_id"),
                    rs.getString("department_name"),
                    rs.getString("location")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error fetching departments: " + e.getMessage());
        }
        return departments;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET DEPARTMENT BY ID
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public Department getDepartmentById(int departmentId) {
        String sql = "SELECT * FROM departments WHERE department_id = ?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Department(
                    rs.getInt("department_id"),
                    rs.getString("department_name"),
                    rs.getString("location")
                );
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error finding department: " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DEPARTMENT EXISTS (validation helper)
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Used by EmployeeService to validate that a department_id exists
     * before allowing an employee to be assigned to it.
     *
     * SQL used:
     *   SELECT COUNT(*) FROM departments WHERE department_id = ?
     *
     * COUNT(*) returns a number, not rows of data.
     * If COUNT > 0, the department exists.
     */
    @Override
    public boolean departmentExists(int departmentId) {
        String sql = "SELECT COUNT(*) FROM departments WHERE department_id = ?";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {

            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // column index 1 = COUNT(*)
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Error checking department: " + e.getMessage());
        }
        return false;
    }
}
