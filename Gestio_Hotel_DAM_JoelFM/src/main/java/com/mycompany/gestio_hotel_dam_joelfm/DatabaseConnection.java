/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.mycompany.gestio_hotel_dam_joelfm;

/**
 *
 * @author jonif
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hoteljoel";
    private static final String USER = "root"; // Canvia si tens un altre usuari
    private static final String PASSWORD = ""; // Escriu la contrasenya si en tens

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexió exitosa a la base de dades!");
        } catch (SQLException e) {
            System.out.println("❌ Error de connexió: " + e.getMessage());
        }
        return connection;
    }
}


