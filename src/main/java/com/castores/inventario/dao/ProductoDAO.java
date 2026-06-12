package com.castores.inventario.dao;

import com.castores.inventario.model.Producto;
import com.castores.inventario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private static final String LISTAR_TODOS =
            "SELECT idProducto, nombreProducto, descripcion, cantidadActual, estatus, fechaCreacion " +
            "FROM PRODUCTOS ORDER BY nombreProducto";

    private static final String LISTAR_ACTIVOS_CON_EXISTENCIA =
            "SELECT idProducto, nombreProducto, descripcion, cantidadActual, estatus, fechaCreacion " +
            "FROM PRODUCTOS WHERE estatus = 1 AND cantidadActual > 0 ORDER BY nombreProducto";

    private static final String AGREGAR =
            "INSERT INTO PRODUCTOS (nombreProducto, descripcion, cantidadActual, estatus) VALUES (?, ?, 0, 1)";

    private static final String AUMENTAR_INVENTARIO =
            "UPDATE PRODUCTOS SET cantidadActual = cantidadActual + ? WHERE idProducto = ? AND estatus = 1";

    private static final String ACTUALIZAR_ESTATUS =
            "UPDATE PRODUCTOS SET estatus = ? WHERE idProducto = ?";

    public List<Producto> listarTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(LISTAR_TODOS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                productos.add(mapearProducto(resultSet));
            }
        }

        return productos;
    }

    public List<Producto> listarActivosConExistencia() throws SQLException {
        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(LISTAR_ACTIVOS_CON_EXISTENCIA);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                productos.add(mapearProducto(resultSet));
            }
        }

        return productos;
    }

    public boolean agregar(Producto producto) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(AGREGAR)) {

            statement.setString(1, producto.getNombreProducto());
            statement.setString(2, producto.getDescripcion());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean aumentarInventario(int idProducto, int cantidad) throws SQLException {
        if (cantidad <= 0) {
            return false;
        }

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(AUMENTAR_INVENTARIO)) {

            statement.setInt(1, cantidad);
            statement.setInt(2, idProducto);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean darDeBaja(int idProducto) throws SQLException {
        return actualizarEstatus(idProducto, 0);
    }

    public boolean reactivar(int idProducto) throws SQLException {
        return actualizarEstatus(idProducto, 1);
    }

    private boolean actualizarEstatus(int idProducto, int estatus) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(ACTUALIZAR_ESTATUS)) {

            statement.setInt(1, estatus);
            statement.setInt(2, idProducto);
            return statement.executeUpdate() > 0;
        }
    }

    private Producto mapearProducto(ResultSet resultSet) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(resultSet.getInt("idProducto"));
        producto.setNombreProducto(resultSet.getString("nombreProducto"));
        producto.setDescripcion(resultSet.getString("descripcion"));
        producto.setCantidadActual(resultSet.getInt("cantidadActual"));
        producto.setEstatus(resultSet.getInt("estatus"));

        Timestamp fechaCreacion = resultSet.getTimestamp("fechaCreacion");
        if (fechaCreacion != null) {
            producto.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }

        return producto;
    }
}
