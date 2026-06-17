package com.ems.service;

import com.ems.dao.DepartmentDAO;
import com.ems.model.Department;
import com.ems.util.InputValidator;

import java.util.List;

/**
 * SERVICE CLASS: DepartmentService
 *
 * PURPOSE:
 *   Business logic for department operations.
 *   Validates input and delegates to DepartmentDAO.
 */
public class DepartmentService {

    private final DepartmentDAO departmentDAO;

    public DepartmentService(DepartmentDAO departmentDAO) {
        this.departmentDAO = departmentDAO;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ADD DEPARTMENT
    // ─────────────────────────────────────────────────────────────────────────
    public String addDepartment(String departmentName, String location) {
        if (!InputValidator.isNotBlank(departmentName))
            return "ERROR: Department name cannot be blank.";

        if (!InputValidator.isNotBlank(location))
            return "ERROR: Location cannot be blank.";

        Department department = new Department(departmentName.trim(), location.trim());
        boolean success = departmentDAO.addDepartment(department);

        return success
            ? "SUCCESS: Department '" + departmentName + "' added!"
            : "ERROR: Failed to add department.";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET ALL DEPARTMENTS
    // ─────────────────────────────────────────────────────────────────────────
    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET DEPARTMENT BY ID
    // ─────────────────────────────────────────────────────────────────────────
    public Department getDepartmentById(int departmentId) {
        return departmentDAO.getDepartmentById(departmentId);
    }
}
