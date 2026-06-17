package com.ems.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 
public class DatabaseConnection {

   
    private static final String URL      = "jdbc:mysql://localhost:3306/employee_mgmt_db";
    private static final String USER     = "root";      
    private static final String PASSWORD = "my_password"; 
   
    private static Connection connection = null;

  
    private DatabaseConnection() {}

   
    public static Connection getConnection() throws SQLException {
        try {
           
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
           
            throw new SQLException("MySQL JDBC Driver not found. " +
                    "Add mysql-connector-j-X.X.X.jar to your classpath.", e);
        }
        return connection;
    }

   
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error closing connection: " + e.getMessage());
        }
    }
}
