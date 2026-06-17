-- ============================================================
-- FILE: database_setup.sql
-- PURPOSE: Create the database and all required tables.
-- Run this file in MySQL Workbench or the MySQL CLI before
-- starting the Java application.
-- ============================================================

-- ── STEP 1: Create the database ──────────────────────────────
-- 'IF NOT EXISTS' prevents an error if you run this script twice
CREATE DATABASE IF NOT EXISTS employee_mgmt_db;

-- Select the database to use
USE employee_mgmt_db;

-- ============================================================
-- TABLE: departments
-- ============================================================
-- WHY CREATE DEPARTMENTS FIRST?
--   Because employees have a FOREIGN KEY pointing to departments.
--   The parent table (departments) must exist before the child
--   table (employees) that references it.
-- ============================================================
CREATE TABLE IF NOT EXISTS departments (

    department_id   INT          PRIMARY KEY AUTO_INCREMENT,
    -- AUTO_INCREMENT: MySQL automatically assigns 1, 2, 3... for new rows.
    -- You never insert this value manually.

    department_name VARCHAR(100) NOT NULL,
    -- NOT NULL: every department must have a name.

    location        VARCHAR(100) NOT NULL
    -- e.g. "Mumbai", "Bangalore", "Remote"
);

-- ============================================================
-- TABLE: employees
-- ============================================================
-- This is the main table. It stores one row per employee.
-- RELATIONSHIP: Many employees can belong to ONE department
--   → This is a Many-to-One (N:1) relationship.
-- ============================================================
CREATE TABLE IF NOT EXISTS employees (

    employee_id   INT            PRIMARY KEY AUTO_INCREMENT,

    name          VARCHAR(100)   NOT NULL,

    email         VARCHAR(150)   NOT NULL UNIQUE,
    -- UNIQUE: no two employees can share the same email address.
    -- This enforces data integrity at the DATABASE level — even if
    -- our Java code has a bug, MySQL will reject duplicate emails.

    phone         CHAR(10)       NOT NULL,
    -- CHAR(10) = always exactly 10 characters (Indian mobile numbers)
    -- VARCHAR would also work, but CHAR is more efficient for fixed-length.

    job_title     VARCHAR(100)   NOT NULL,

    salary        DECIMAL(12, 2) NOT NULL,
    -- DECIMAL(12,2): up to 12 digits total, 2 after the decimal point.
    -- e.g. 99999999.99  (don't use FLOAT for money — floating point errors!)

    department_id INT            NOT NULL,

    -- ── FOREIGN KEY CONSTRAINT ─────────────────────────────────
    -- This is the most important DB concept for relationships.
    -- It means: employee.department_id MUST match an existing
    -- departments.department_id row. You can't assign an employee
    -- to department 99 if department 99 doesn't exist.
    CONSTRAINT fk_department
        FOREIGN KEY (department_id)
        REFERENCES departments(department_id)
        ON DELETE RESTRICT   -- Prevent deleting a dept that still has employees
        ON UPDATE CASCADE    -- If dept ID changes, update all employee records too
);

-- ============================================================
-- SAMPLE DATA: Insert test departments and employees
-- Run this to have data to work with immediately.
-- ============================================================

INSERT INTO departments (department_name, location) VALUES
    ('Engineering',  'Bangalore'),
    ('Human Resources', 'Mumbai'),
    ('Finance',      'Delhi'),
    ('Marketing',    'Pune');

INSERT INTO employees (name, email, phone, job_title, salary, department_id) VALUES
    ('Aryan Sharma',   'aryan.sharma@ems.com',   '9876543210', 'Software Engineer',    75000.00, 1),
    ('Priya Mehta',    'priya.mehta@ems.com',    '9123456780', 'HR Manager',           65000.00, 2),
    ('Rohan Gupta',    'rohan.gupta@ems.com',    '9988776655', 'Backend Developer',    80000.00, 1),
    ('Sneha Patel',    'sneha.patel@ems.com',    '9876512340', 'Financial Analyst',    70000.00, 3),
    ('Vikram Singh',   'vikram.singh@ems.com',   '9001234567', 'Marketing Lead',       72000.00, 4);

-- ============================================================
-- USEFUL QUERIES (for reference and interview preparation)
-- ============================================================

-- Q1: View all employees with their department name (JOIN)
-- This is more useful than just seeing a department_id number.
SELECT
    e.employee_id,
    e.name          AS employee_name,
    e.email,
    e.job_title,
    e.salary,
    d.department_name,
    d.location
FROM employees e
INNER JOIN departments d
    ON e.department_id = d.department_id
ORDER BY e.employee_id;

-- Q2: Count employees per department
SELECT
    d.department_name,
    COUNT(e.employee_id) AS total_employees
FROM departments d
LEFT JOIN employees e
    ON d.department_id = e.department_id
GROUP BY d.department_id, d.department_name;

-- Q3: Find the highest-paid employee
SELECT name, job_title, salary
FROM employees
ORDER BY salary DESC
LIMIT 1;

-- Q4: Find all employees in Engineering department
SELECT e.name, e.job_title, e.salary
FROM employees e
JOIN departments d ON e.department_id = d.department_id
WHERE d.department_name = 'Engineering';

-- Q5: Average salary per department
SELECT
    d.department_name,
    AVG(e.salary) AS avg_salary
FROM employees e
JOIN departments d ON e.department_id = d.department_id
GROUP BY d.department_id, d.department_name;
