# 🗂️ Employee Management System

![Java](https://img.shields.io/badge/Java-26-orange?style=for-the-badge&logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Driver-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Complete-brightgreen?style=for-the-badge)

A console-based **Employee Management System** built using **Core Java, MySQL, and JDBC** that demonstrates real-world backend development skills including layered architecture, OOP principles, and database design.

---

## 👩‍💻 Developer

**Vaishali Upadhyay**  
B.Tech — Artificial Intelligence & Data Science  
🔗 GitHub: [@vaishu1711](https://github.com/vaishu1711)

---

## 📌 Project Overview

This project simulates a real-world HR system where a company can manage its employees and departments digitally. Instead of maintaining Excel sheets, HR teams can use this system to add, search, update, and delete employee records through a clean console interface.

### Problem Statement
Companies need a centralized system to store and manage employee data efficiently. Manual record-keeping is error-prone and hard to scale. This system solves that by providing a structured, database-backed solution.

### Real-World Use Case
- Small to medium companies managing 10–500 employees
- HR departments handling employee onboarding and offboarding
- Companies that need department-wise employee tracking

---

## ⚙️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java (Core) | 26 | Application logic, OOP |
| MySQL | 8.0 | Relational database |
| JDBC | mysql-connector-j-9.7.0 | Java–MySQL connectivity |
| SQL | — | DDL, DML, Joins, Constraints |
| Git & GitHub | — | Version control & hosting |

---

## ✨ Features

### Employee Operations
- ✅ Add new employee with full details
- ✅ View all employees in formatted table
- ✅ Search employee by ID
- ✅ Update employee details (with current value preview)
- ✅ Delete employee with confirmation prompt

### Department Operations
- ✅ Add new department
- ✅ View all departments
- ✅ Assign employee to department

### Technical Features
- ✅ Input validation (email regex, 10-digit phone, blank checks)
- ✅ SQL Injection prevention using PreparedStatement
- ✅ Exception handling with try-with-resources
- ✅ Singleton database connection pattern
- ✅ Referential integrity via Foreign Key constraints

---

## 🏗️ Project Architecture

This project follows a **3-Layer Architecture**:

```
User Input (Console)
        ↓
  ┌─────────────┐
  │  Main.java  │  ← Presentation Layer (Scanner, Menus)
  └─────────────┘
        ↓
  ┌─────────────────┐
  │  Service Layer  │  ← Business Logic + Input Validation
  └─────────────────┘
        ↓
  ┌─────────────┐
  │  DAO Layer  │  ← All SQL Queries (PreparedStatement)
  └─────────────┘
        ↓
  ┌─────────────┐
  │    MySQL    │  ← Database (employees + departments)
  └─────────────┘
```

---

## 📁 Folder Structure

```
EmployeeManagementSystem/
│
├── 📄 database_setup.sql          ← Run this first in MySQL
├── 📄 README.md
│
└── 📁 src/
    └── 📁 com/ems/
        │
        ├── 📁 model/
        │   ├── Person.java            ← Base class (Inheritance)
        │   ├── Employee.java          ← Extends Person
        │   └── Department.java        ← Department POJO
        │
        ├── 📁 db/
        │   └── DatabaseConnection.java  ← Singleton JDBC Connection
        │
        ├── 📁 dao/
        │   ├── EmployeeDAO.java         ← Interface (contract)
        │   ├── DepartmentDAO.java       ← Interface (contract)
        │   ├── MySQLEmployeeDAO.java    ← MySQL Implementation
        │   └── MySQLDepartmentDAO.java  ← MySQL Implementation
        │
        ├── 📁 service/
        │   ├── EmployeeService.java     ← Business Logic
        │   └── DepartmentService.java   ← Business Logic
        │
        ├── 📁 util/
        │   ├── InputValidator.java      ← Regex Validation
        │   └── ConsoleDisplay.java      ← Formatted Output
        │
        └── 📁 main/
            └── Main.java               ← Entry Point
```

---

## 🧠 OOP Concepts Used

### 1. Encapsulation
All model fields (`name`, `email`, `salary` etc.) are `private`. External classes access them only through public getters and setters — protecting data integrity.

### 2. Inheritance
`Employee` extends `Person`. The `Person` class holds common attributes (`name`, `email`) shared by any person in the system. `Employee` adds job-specific fields. This avoids code duplication.

```java
public class Person {
    private String name;
    private String email;
    // getters + setters
}

public class Employee extends Person {
    private String jobTitle;
    private double salary;
    // Employee IS-A Person
}
```

### 3. Polymorphism
`EmployeeDAO` is an interface. `EmployeeService` holds a reference of type `EmployeeDAO`, but at runtime the actual object is `MySQLEmployeeDAO`. Swapping databases requires changing only one line in `Main.java`.

### 4. Abstraction
`Main.java` calls `employeeService.addEmployee(...)` without knowing any SQL exists. The complexity is hidden behind the service and DAO layers.

---

## 🗄️ Database Design

### Tables

**departments**
| Column | Type | Constraint |
|---|---|---|
| department_id | INT | PRIMARY KEY, AUTO_INCREMENT |
| department_name | VARCHAR(100) | NOT NULL |
| location | VARCHAR(100) | NOT NULL |

**employees**
| Column | Type | Constraint |
|---|---|---|
| employee_id | INT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | NOT NULL, UNIQUE |
| phone | CHAR(10) | NOT NULL |
| job_title | VARCHAR(100) | NOT NULL |
| salary | DECIMAL(12,2) | NOT NULL |
| department_id | INT | FOREIGN KEY → departments |

### Relationship
```
departments (1) ──────< employees (Many)
One department can have many employees.
Foreign Key: employees.department_id → departments.department_id
ON DELETE RESTRICT | ON UPDATE CASCADE
```

---

## 🔑 Key SQL Queries

```sql
-- View all employees with department name (JOIN)
SELECT e.employee_id, e.name, e.job_title, e.salary,
       d.department_name, d.location
FROM employees e
INNER JOIN departments d ON e.department_id = d.department_id;

-- Count employees per department
SELECT d.department_name, COUNT(e.employee_id) AS total_employees
FROM departments d
LEFT JOIN employees e ON d.department_id = e.department_id
GROUP BY d.department_id;

-- Highest paid employee
SELECT name, job_title, salary
FROM employees
ORDER BY salary DESC LIMIT 1;

-- Average salary per department
SELECT d.department_name, AVG(e.salary) AS avg_salary
FROM employees e
JOIN departments d ON e.department_id = d.department_id
GROUP BY d.department_id;
```

---

## 🚀 How to Run

### Prerequisites
- Java JDK 17 or higher
- MySQL 8.0
- mysql-connector-j-9.7.0.jar

### Step 1: Database Setup
```sql
-- Open MySQL Workbench, run database_setup.sql
-- Or in MySQL CLI:
source /path/to/database_setup.sql;
```

### Step 2: Configure Database Connection
Open `src/com/ems/db/DatabaseConnection.java` and update:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/employee_mgmt_db";
private static final String USER     = "root";
private static final String PASSWORD = "your_mysql_password";
```

### Step 3: Add JDBC Driver
Download `mysql-connector-j-9.7.0.jar` from [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/)
and place it in the `lib/` folder.

### Step 4: Compile
```bash
# Windows
javac -cp ".;lib\mysql-connector-j-9.7.0.jar" -d out -sourcepath src src\com\ems\main\Main.java

# Linux / Mac
javac -cp ".:lib/mysql-connector-j-9.7.0.jar" -d out -sourcepath src src/com/ems/main/Main.java
```

### Step 5: Run
```bash
# Windows
java -cp "out;lib\mysql-connector-j-9.7.0.jar" com.ems.main.Main

# Linux / Mac
java -cp "out:lib/mysql-connector-j-9.7.0.jar" com.ems.main.Main
```

### Expected Output
```
Connecting to database...
[DB] Connection established successfully.

╔══════════════════════════════════════╗
║   EMPLOYEE MANAGEMENT SYSTEM  v1.0   ║
╠══════════════════════════════════════╣
║  1. Employee Menu                     ║
║  2. Department Menu                   ║
║  0. Exit                              ║
╚══════════════════════════════════════╝
  Enter choice:
```

---

## 🎨 Design Patterns Used

| Pattern | Where Used | Why |
|---|---|---|
| **Singleton** | DatabaseConnection.java | One DB connection shared across app |
| **DAO Pattern** | dao/ package | Separates SQL from business logic |
| **Layered Architecture** | Entire project | Clean separation of concerns |

---

## 🛡️ Security Features

- **PreparedStatement** used for ALL queries — prevents SQL Injection
- **Input Validation** — email regex, 10-digit phone, blank field checks
- **Foreign Key Constraints** — database-level referential integrity
- **Confirmation prompt** before delete — prevents accidental data loss

---

## 🔮 Future Improvements

- [ ] Migrate to **Spring Boot + Hibernate** (JPA/ORM)
- [ ] Build **REST API** with JSON responses
- [ ] Add **JWT-based Authentication** with Admin/Manager roles
- [ ] Implement **HikariCP Connection Pooling**
- [ ] Add **JUnit + Mockito** unit tests for Service layer
- [ ] Build **React.js frontend** to consume the REST API
- [ ] Add **pagination** for large employee lists
- [ ] Implement **search by name, department, salary range**

---

## 📊 Project Stats

| Metric | Value |
|---|---|
| Total Java Files | 13 |
| Total Lines of Code | ~900+ |
| Database Tables | 2 |
| OOP Concepts Demonstrated | 4 |
| Design Patterns Used | 2 |
| CRUD Operations | 7 |

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">

**⭐ If you found this project helpful, please give it a star! ⭐**

Made with ❤️ by [Vaishali Upadhyay](https://github.com/vaishu1711)

</div>
