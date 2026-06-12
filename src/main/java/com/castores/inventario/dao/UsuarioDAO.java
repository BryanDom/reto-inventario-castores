package com.castores.inventario.dao;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private static final String BUSCAR_POR_CREDENCIALES =
            "SELECT idUsuario, nombre, correo, contrasena, idRol, estatus " +
            "FROM USUARIOS " +
            "WHERE correo = ? AND contrasena = ? AND estatus = 1";

    public Usuario buscarPorCorreoYContrasena(String correo, String contrasena) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(BUSCAR_POR_CREDENCIALES)) {

            statement.setString(1, correo);
            statement.setString(2, contrasena);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearUsuario(resultSet);
                }
            }
        }

        return null;
    }

    private Usuario mapearUsuario(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultSet.getInt("idUsuario"));
        usuario.setNombre(resultSet.getString("nombre"));
        usuario.setCorreo(resultSet.getString("correo"));
        usuario.setContrasena(resultSet.getString("contrasena"));
        usuario.setIdRol(resultSet.getInt("idRol"));
        usuario.setEstatus(resultSet.getInt("estatus"));
        return usuario;
    }
}
