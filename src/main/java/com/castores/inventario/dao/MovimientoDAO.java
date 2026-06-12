package com.castores.inventario.dao;

import com.castores.inventario.model.Movimiento;
import com.castores.inventario.model.MovimientoDetalle;
import com.castores.inventario.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {
    public static final int TIPO_SALIDA = 2;

    private static final String REGISTRAR_MOVIMIENTO =
            "INSERT INTO HISTORICO_MOVIMIENTOS (idProducto, idUsuario, idTipoMovimiento, cantidad, comentario) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String DESCONTAR_PRODUCTO =
            "UPDATE PRODUCTOS SET cantidadActual = cantidadActual - ? " +
            "WHERE idProducto = ? AND estatus = 1 AND cantidadActual >= ?";

    private static final String LISTAR_TODOS =
            "SELECT idMovimiento, idProducto, idUsuario, idTipoMovimiento, cantidad, fechaHora, comentario " +
            "FROM HISTORICO_MOVIMIENTOS ORDER BY fechaHora DESC";

    private static final String LISTAR_POR_TIPO =
            "SELECT idMovimiento, idProducto, idUsuario, idTipoMovimiento, cantidad, fechaHora, comentario " +
            "FROM HISTORICO_MOVIMIENTOS WHERE idTipoMovimiento = ? ORDER BY fechaHora DESC";

    private static final String LISTAR_DETALLE =
            "SELECT h.idMovimiento, p.nombreProducto, u.nombre AS nombreUsuario, " +
            "tm.idTipoMovimiento, tm.nombreTipo, h.cantidad, h.fechaHora " +
            "FROM HISTORICO_MOVIMIENTOS h " +
            "INNER JOIN PRODUCTOS p ON h.idProducto = p.idProducto " +
            "INNER JOIN USUARIOS u ON h.idUsuario = u.idUsuario " +
            "INNER JOIN TIPOS_MOVIMIENTO tm ON h.idTipoMovimiento = tm.idTipoMovimiento ";

    private static final String ORDEN_DETALLE = "ORDER BY h.fechaHora DESC";

    public boolean registrarSalida(int idProducto, int idUsuario, int cantidad, String comentario) throws SQLException {
        if (cantidad <= 0) {
            return false;
        }

        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            if (!descontarProducto(conexion, idProducto, cantidad)) {
                conexion.rollback();
                return false;
            }

            registrarMovimiento(conexion, idProducto, idUsuario, TIPO_SALIDA, cantidad, comentario);
            conexion.commit();
            return true;
        } catch (SQLException e) {
            if (conexion != null) {
                conexion.rollback();
            }
            throw e;
        } finally {
            if (conexion != null) {
                conexion.setAutoCommit(true);
                conexion.close();
            }
        }
    }

    public List<Movimiento> listarTodos() throws SQLException {
        List<Movimiento> movimientos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(LISTAR_TODOS);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                movimientos.add(mapearMovimiento(resultSet));
            }
        }

        return movimientos;
    }

    public List<Movimiento> listarPorTipoMovimiento(int idTipoMovimiento) throws SQLException {
        List<Movimiento> movimientos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(LISTAR_POR_TIPO)) {

            statement.setInt(1, idTipoMovimiento);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    movimientos.add(mapearMovimiento(resultSet));
                }
            }
        }

        return movimientos;
    }

    public List<MovimientoDetalle> listarDetalle(Integer idTipoMovimiento) throws SQLException {
        List<MovimientoDetalle> movimientos = new ArrayList<>();
        String sql = LISTAR_DETALLE;

        if (idTipoMovimiento != null) {
            sql += "WHERE h.idTipoMovimiento = ? ";
        }

        sql += ORDEN_DETALLE;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            if (idTipoMovimiento != null) {
                statement.setInt(1, idTipoMovimiento);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    movimientos.add(mapearMovimientoDetalle(resultSet));
                }
            }
        }

        return movimientos;
    }

    private boolean descontarProducto(Connection conexion, int idProducto, int cantidad) throws SQLException {
        try (PreparedStatement statement = conexion.prepareStatement(DESCONTAR_PRODUCTO)) {
            statement.setInt(1, cantidad);
            statement.setInt(2, idProducto);
            statement.setInt(3, cantidad);
            return statement.executeUpdate() > 0;
        }
    }

    private void registrarMovimiento(Connection conexion, int idProducto, int idUsuario, int idTipoMovimiento,
                                     int cantidad, String comentario) throws SQLException {
        try (PreparedStatement statement = conexion.prepareStatement(REGISTRAR_MOVIMIENTO)) {
            statement.setInt(1, idProducto);
            statement.setInt(2, idUsuario);
            statement.setInt(3, idTipoMovimiento);
            statement.setInt(4, cantidad);
            statement.setString(5, comentario);
            statement.executeUpdate();
        }
    }

    private Movimiento mapearMovimiento(ResultSet resultSet) throws SQLException {
        Movimiento movimiento = new Movimiento();
        movimiento.setIdMovimiento(resultSet.getLong("idMovimiento"));
        movimiento.setIdProducto(resultSet.getInt("idProducto"));
        movimiento.setIdUsuario(resultSet.getInt("idUsuario"));
        movimiento.setIdTipoMovimiento(resultSet.getInt("idTipoMovimiento"));
        movimiento.setCantidad(resultSet.getInt("cantidad"));
        movimiento.setComentario(resultSet.getString("comentario"));

        Timestamp fechaHora = resultSet.getTimestamp("fechaHora");
        if (fechaHora != null) {
            movimiento.setFechaHora(fechaHora.toLocalDateTime());
        }

        return movimiento;
    }

    private MovimientoDetalle mapearMovimientoDetalle(ResultSet resultSet) throws SQLException {
        MovimientoDetalle movimiento = new MovimientoDetalle();
        movimiento.setIdMovimiento(resultSet.getLong("idMovimiento"));
        movimiento.setNombreProducto(resultSet.getString("nombreProducto"));
        movimiento.setNombreUsuario(resultSet.getString("nombreUsuario"));
        movimiento.setIdTipoMovimiento(resultSet.getInt("idTipoMovimiento"));
        movimiento.setTipoMovimiento(resultSet.getString("nombreTipo"));
        movimiento.setCantidad(resultSet.getInt("cantidad"));

        Timestamp fechaHora = resultSet.getTimestamp("fechaHora");
        if (fechaHora != null) {
            movimiento.setFechaHora(fechaHora.toLocalDateTime());
        }

        return movimiento;
    }
}
