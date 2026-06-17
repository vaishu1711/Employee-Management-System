package com.ems.dao;

import com.ems.model.Employee;
import java.util.List;

/**
 * INTERFACE: EmployeeDAO
 *
 * PURPOSE:
 *   Defines the CONTRACT for all employee database operations.
 *   Any class that implements this interface MUST provide all these methods.
 *
 * WHY USE AN INTERFACE HERE?
 *   This is the DAO (Data Access Object) design pattern.
 *   - The Service layer only talks to this interface, not the concrete class.
 *   - Tomorrow, if you switch from MySQL to PostgreSQL or MongoDB,
 *     you just write a new class (e.g. MongoEmployeeDAO) that implements
 *     this same interface. The Service and Main classes don't change at all.
 *   - This is the "Program to an interface, not an implementation" principle.
 *
 * OOP CONCEPT: Polymorphism via interfaces
 *   EmployeeService holds a reference of type EmployeeDAO.
 *   At runtime, the actual object is MySQLEmployeeDAO.
 *   This is runtime polymorphism.
 */
public interface EmployeeDAO {

    /**
     * Insert a new employee record into the database.
     * @param employee Employee object with all fields filled except ID
     * @return true if insert succeeded, false otherwise
     */
    boolean addEmployee(Employee employee);

    /**
     * Retrieve every employee from the database.
     * @return List of all Employee objects; empty list if none exist
     */
    List<Employee> getAllEmployees();

    /**
     * Find one employee by their primary key.
     * @param employeeId the ID to search for
     * @return Employee object if found, null if not found
     */
    Employee getEmployeeById(int employeeId);

    /**
     * Update an existing employee's details.
     * @param employee Employee object with updated fields (ID must be set)
     * @return true if update succeeded, false otherwise
     */
    boolean updateEmployee(Employee employee);

    /**
     * Delete an employee record permanently.
     * @param employeeId the ID of the employee to delete
     * @return true if delete succeeded, false otherwise
     */
    boolean deleteEmployee(int employeeId);
}
