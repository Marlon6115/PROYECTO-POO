package org.example.proyectopoo.conexion;

import java.sql.*;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost/gestor_estudiantes";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
