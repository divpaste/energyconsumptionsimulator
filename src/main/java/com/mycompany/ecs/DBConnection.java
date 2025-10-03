package com.mycompany.ecs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {
private static final String DB_URL = "jdbc:mysql://localhost:3309/storage";
private static final String USER = "root";
private static final String PASS = "";

public static Connection getConnection() {
Connection conn = null;
try {
conn = DriverManager.getConnection(DB_URL, USER, PASS);
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, "Failed to connect to the database.",
"Database Error", JOptionPane.ERROR_MESSAGE);
e.printStackTrace();
}

return conn;
}
}