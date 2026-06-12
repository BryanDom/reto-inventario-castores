package com.castores.inventario.controller;

import com.castores.inventario.dao.MovimientoDAO;
import com.castores.inventario.model.MovimientoDetalle;
import com.castores.inventario.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/historico")
public class HistoricoServlet extends HttpServlet {
    private final MovimientoDAO movimientoDAO = new MovimientoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obtenerUsuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (usuario.getIdRol() != 1) {
            response.sendRedirect(request.getContextPath() + "/salida");
            return;
        }

        Integer idTipoMovimiento = obtenerFiltroTipo(request);

        try {
            List<MovimientoDetalle> movimientos = movimientoDAO.listarDetalle(idTipoMovimiento);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("idTipoMovimiento", idTipoMovimiento);
            request.getRequestDispatcher("/WEB-INF/views/historico.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar historial de movimientos.", e);
        }
    }

    private Integer obtenerFiltroTipo(HttpServletRequest request) {
        String tipo = request.getParameter("tipo");
        if (tipo == null || tipo.trim().isEmpty() || "todos".equals(tipo)) {
            return null;
        }

        try {
            return Integer.parseInt(tipo);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Usuario obtenerUsuario(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        return (Usuario) session.getAttribute("usuario");
    }
}
