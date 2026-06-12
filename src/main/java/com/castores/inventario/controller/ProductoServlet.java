package com.castores.inventario.controller;

import com.castores.inventario.dao.ProductoDAO;
import com.castores.inventario.model.Producto;
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

@WebServlet("/inventario")
public class ProductoServlet extends HttpServlet {
    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!usuarioPuedeVerInventario(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Producto> productos = productoDAO.listarTodos();
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("/WEB-INF/views/inventario.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al listar productos.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!usuarioEsAdministrador(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para administrar productos.");
            return;
        }

        String accion = request.getParameter("accion");

        try {
            if ("agregar".equals(accion)) {
                agregarProducto(request);
            } else if ("aumentar".equals(accion)) {
                aumentarInventario(request);
            } else if ("baja".equals(accion)) {
                darDeBaja(request);
            } else if ("reactivar".equals(accion)) {
                reactivar(request);
            }

            response.sendRedirect(request.getContextPath() + "/inventario");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al procesar producto.", e);
        }
    }

    private void agregarProducto(HttpServletRequest request) throws SQLException {
        String nombreProducto = request.getParameter("nombreProducto");
        String descripcion = request.getParameter("descripcion");

        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        Producto producto = new Producto();
        producto.setNombreProducto(nombreProducto.trim());
        producto.setDescripcion(descripcion);
        productoDAO.agregar(producto);
    }

    private void aumentarInventario(HttpServletRequest request) throws SQLException {
        int idProducto = obtenerEntero(request, "idProducto");
        int cantidad = obtenerEntero(request, "cantidad");

        if (!productoDAO.aumentarInventario(idProducto, cantidad)) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor a 0.");
        }
    }

    private void darDeBaja(HttpServletRequest request) throws SQLException {
        int idProducto = obtenerEntero(request, "idProducto");
        productoDAO.darDeBaja(idProducto);
    }

    private void reactivar(HttpServletRequest request) throws SQLException {
        int idProducto = obtenerEntero(request, "idProducto");
        productoDAO.reactivar(idProducto);
    }

    private int obtenerEntero(HttpServletRequest request, String parametro) {
        try {
            return Integer.parseInt(request.getParameter(parametro));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El parametro " + parametro + " debe ser numerico.");
        }
    }

    private boolean usuarioEsAdministrador(HttpServletRequest request) {
        Usuario usuario = obtenerUsuario(request);
        return usuario != null && usuario.getIdRol() == 1;
    }

    private boolean usuarioPuedeVerInventario(HttpServletRequest request) {
        Usuario usuario = obtenerUsuario(request);
        return usuario != null && (usuario.getIdRol() == 1 || usuario.getIdRol() == 2);
    }

    private Usuario obtenerUsuario(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        return (Usuario) session.getAttribute("usuario");
    }
}
