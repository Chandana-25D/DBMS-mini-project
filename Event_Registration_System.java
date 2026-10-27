/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package event_registration_system;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Chandana D
 */
public class Event_Registration_System {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/event_db";
        String user = "root";
        String password = "12345678";

        try {
            Connection con = DriverManager.getConnection(
                url, user, password
            );

            System.out.println(
                "Connected to MySQL successfully!"
            );

            con.close();

        } catch (SQLException e) {
            System.out.println(
                "Database connection failed!"
            );
            e.printStackTrace();
        }
    }
    }
    
