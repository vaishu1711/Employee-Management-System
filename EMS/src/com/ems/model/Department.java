package com.ems.model;

/**
 * MODEL CLASS: Department
 *
 * PURPOSE:
 *   Represents a single row from the 'departments' table in our database.
 *   This is a plain Java object (POJO) that holds department data in memory.
 *
 * OOP CONCEPTS USED:
 *   - Encapsulation: All fields are private. External code can only read/write
 *     them through public getter/setter methods. This protects data integrity.
 *   - Constructor overloading: Two constructors — one for creating a new
 *     department (no ID yet), one for loading an existing one from the DB.
 */
public class Department {

    // ── Fields (private = encapsulated) ──────────────────────────────────────
    private int    departmentId;   // Primary key in DB
    private String departmentName; // e.g. "Engineering", "HR"
    private String location;       // e.g. "Mumbai", "Bangalore"

    // ── Constructor 1: used when INSERTING a new department (DB assigns ID) ──
    public Department(String departmentName, String location) {
        this.departmentName = departmentName;
        this.location       = location;
    }

    // ── Constructor 2: used when READING an existing department from DB ───────
    public Department(int departmentId, String departmentName, String location) {
        this.departmentId   = departmentId;
        this.departmentName = departmentName;
        this.location       = location;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getDepartmentId()   { return departmentId;   }
    public String getDepartmentName() { return departmentName; }
    public String getLocation()       { return location;       }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setDepartmentId(int departmentId)       { this.departmentId   = departmentId;   }
    public void setDepartmentName(String departmentName){ this.departmentName = departmentName; }
    public void setLocation(String location)            { this.location       = location;       }

    // ── toString: useful for printing department info to the console ──────────
    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %-15s |",
                departmentId, departmentName, location);
    }
}
