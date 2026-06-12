package com.castores.inventario.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/inventario_castores?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        cargarDriver();
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    private static void cargarDriver() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de MySQL.", e);
        }
    }
}
