/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author asus
 */
public class DatabaseConnection {
    
    private Connection connection = null;

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/rpl", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("gah");
        }
    }

    public Connection getConnection() {
        return connection;
    }
    public static void main(String[] args) {
        DatabaseConnection a = new DatabaseConnection();
    }
}
