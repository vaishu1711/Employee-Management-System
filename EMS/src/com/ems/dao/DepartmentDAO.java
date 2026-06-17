package com.ems.dao;

import com.ems.model.Department;
import java.util.List;

/**
 * INTERFACE: DepartmentDAO
 *
 * PURPOSE:
 *   Defines the contract for all department-related DB operations.
 *   Same design philosophy as EmployeeDAO — separates WHAT to do
 *   from HOW to do it.
 */
public interface DepartmentDAO {

    /**
     * Insert a new department into the database.
     * @param department Department object (no ID needed — DB auto-generates)
     * @return true if successful
     */
    boolean addDepartment(Department department);

    /**
     * Get all departments.
     * @return List of all Department objects
     */
    List<Department> getAllDepartments();

    /**
     * Find one department by its ID.
     * @param departmentId PK to search
     * @return Department or null
     */
    Department getDepartmentById(int departmentId);

    /**
     * Check whether a department ID actually exists in the DB.
     * Used for validating employee input before assigning a department.
     * @param departmentId the ID to check
     * @return true if it exists
     */
    boolean departmentExists(int departmentId);
}
