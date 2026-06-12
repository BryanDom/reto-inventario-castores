package com.castores.inventario.controller;

import com.castores.inventario.dao.MovimientoDAO;
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

@WebServlet("/salida")
public class SalidaServlet extends HttpServlet {
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final MovimientoDAO movimientoDAO = new MovimientoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obtenerUsuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (usuario.getIdRol() != 2) {
            response.sendRedirect(request.getContextPath() + "/inventario");
            return;
        }

        cargarProductos(request);
        request.getRequestDispatcher("/WEB-INF/views/salida.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = obtenerUsuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (usuario.getIdRol() != 2) {
            response.sendRedirect(request.getContextPath() + "/inventario");
            return;
        }

        try {
            int idProducto = obtenerEntero(request, "idProducto");
            int cantidad = obtenerEntero(request, "cantidad");
            String comentario = request.getParameter("comentario");

            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad de salida debe ser mayor a 0.");
            }

            boolean registrado = movimientoDAO.registrarSalida(
                    idProducto,
                    usuario.getIdUsuario(),
                    cantidad,
                    comentario
            );

            if (!registrado) {
                throw new IllegalArgumentException("La cantidad solicitada es mayor a la disponible.");
            }

            response.sendRedirect(request.getContextPath() + "/salida");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            cargarProductos(request);
            request.getRequestDispatcher("/WEB-INF/views/salida.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al registrar salida de producto.", e);
        }
    }

    private void cargarProductos(HttpServletRequest request) throws ServletException {
        try {
            List<Producto> productos = productoDAO.listarActivosConExistencia();
            request.setAttribute("productos", productos);
        } catch (SQLException e) {
            throw new ServletException("Error al listar productos disponibles para salida.", e);
        }
    }

    private int obtenerEntero(HttpServletRequest request, String parametro) {
        try {
            return Integer.parseInt(request.getParameter(parametro));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El parametro " + parametro + " debe ser numerico.");
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
