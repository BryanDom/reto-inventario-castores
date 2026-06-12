package com.castores.inventario.controller;

import com.castores.inventario.dao.UsuarioDAO;
import com.castores.inventario.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        try {
            Usuario usuario = usuarioDAO.buscarPorCorreoYContrasena(correo, contrasena);

            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                if (usuario.getIdRol() == 1) {
                    response.sendRedirect(request.getContextPath() + "/inventario");
                } else if (usuario.getIdRol() == 2) {
                    response.sendRedirect(request.getContextPath() + "/salida");
                } else {
                    request.setAttribute("error", "Rol de usuario no autorizado.");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                }

                return;
            }

            request.setAttribute("error", "Correo o contrasena incorrectos.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error al autenticar el usuario.", e);
        }
    }
}
