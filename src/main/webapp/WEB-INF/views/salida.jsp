<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.castores.inventario.model.Producto" %>
<%@ page import="com.castores.inventario.model.Usuario" %>
<%@ page import="java.util.List" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Salida de productos | Inventario Castores</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: Arial, sans-serif;
            background: #f3f6f9;
            color: #25313c;
        }

        .page {
            width: min(1100px, calc(100% - 32px));
            margin: 0 auto;
            padding: 28px 0 40px;
        }

        .header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
            margin-bottom: 22px;
        }

        h1 {
            margin: 0;
            font-size: 28px;
            color: #1f2d3d;
        }

        .user-info {
            color: #6b7785;
            font-size: 14px;
        }

        .top-actions {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            align-items: center;
            justify-content: flex-end;
        }

        .nav-link {
            display: inline-block;
            padding: 10px 13px;
            border-radius: 6px;
            background: #547089;
            color: #ffffff;
            font-size: 14px;
            font-weight: bold;
            text-decoration: none;
            white-space: nowrap;
        }

        .nav-link:hover {
            background: #435b70;
        }

        .logout-form {
            margin: 0;
        }

        .panel {
            background: #ffffff;
            border: 1px solid #d9e1e8;
            border-radius: 8px;
            box-shadow: 0 8px 22px rgba(31, 45, 61, 0.08);
            padding: 22px;
        }

        .panel h2 {
            margin: 0 0 16px;
            font-size: 20px;
            color: #1f2d3d;
        }

        .error {
            margin-bottom: 18px;
            padding: 11px 12px;
            border-radius: 6px;
            background: #fdecea;
            color: #a1271d;
            border: 1px solid #f5c2bd;
            font-size: 14px;
        }

        .table-wrapper {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: #ffffff;
        }

        th,
        td {
            padding: 12px;
            border-bottom: 1px solid #e4ebf1;
            text-align: left;
            vertical-align: middle;
        }

        th {
            background: #edf3f7;
            color: #314454;
            font-size: 14px;
        }

        tr:hover td {
            background: #f8fbfd;
        }

        .salida-form {
            display: grid;
            grid-template-columns: 90px minmax(160px, 1fr) auto;
            gap: 8px;
            align-items: center;
            margin: 0;
        }

        input {
            width: 100%;
            padding: 10px 11px;
            border: 1px solid #c7d1db;
            border-radius: 6px;
            font: inherit;
        }

        input:focus {
            outline: none;
            border-color: #1f7a8c;
            box-shadow: 0 0 0 3px rgba(31, 122, 140, 0.15);
        }

        button {
            border: none;
            border-radius: 6px;
            padding: 10px 13px;
            color: #ffffff;
            background: #1f7a8c;
            font-weight: bold;
            cursor: pointer;
            white-space: nowrap;
        }

        button:hover {
            background: #186575;
        }

        .btn-danger {
            background: #b9473f;
        }

        .btn-danger:hover {
            background: #97362f;
        }

        .empty {
            padding: 26px;
            text-align: center;
            color: #6b7785;
        }

        @media (max-width: 780px) {
            .header {
                flex-direction: column;
                align-items: stretch;
            }

            .top-actions {
                justify-content: stretch;
            }

            .top-actions .nav-link,
            .top-actions button {
                width: 100%;
                text-align: center;
            }

            .salida-form {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<main class="page">
    <header class="header">
        <div>
            <h1>Salida de productos</h1>
            <div class="user-info">
                Usuario: <%= usuario != null ? usuario.getNombre() : "Sin sesion" %>
            </div>
        </div>
        <nav class="top-actions">
            <% if (usuario != null && usuario.getIdRol() == 1) { %>
                <a class="nav-link" href="<%= request.getContextPath() %>/inventario">Inventario</a>
                <a class="nav-link" href="<%= request.getContextPath() %>/historico">Historial</a>
            <% } else if (usuario != null && usuario.getIdRol() == 2) { %>
                <a class="nav-link" href="<%= request.getContextPath() %>/inventario">Inventario</a>
                <a class="nav-link" href="<%= request.getContextPath() %>/salida">Salida</a>
            <% } %>
            <form action="<%= request.getContextPath() %>/logout" method="post" class="logout-form">
                <button type="submit" class="btn-danger">Cerrar sesion</button>
            </form>
        </nav>
    </header>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <section class="panel">
        <h2>Productos disponibles</h2>
        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Descripcion</th>
                    <th>Disponible</th>
                    <th>Salida</th>
                </tr>
                </thead>
                <tbody>
                <% if (productos == null || productos.isEmpty()) { %>
                    <tr>
                        <td class="empty" colspan="4">No hay productos activos con inventario disponible.</td>
                    </tr>
                <% } else { %>
                    <% for (Producto producto : productos) { %>
                        <tr>
                            <td><%= producto.getNombreProducto() %></td>
                            <td><%= producto.getDescripcion() != null ? producto.getDescripcion() : "" %></td>
                            <td><%= producto.getCantidadActual() %></td>
                            <td>
                                <form action="<%= request.getContextPath() %>/salida" method="post" class="salida-form">
                                    <input type="hidden" name="idProducto" value="<%= producto.getIdProducto() %>">
                                    <input type="number" name="cantidad" min="1" max="<%= producto.getCantidadActual() %>" required>
                                    <input type="text" name="comentario" maxlength="255" placeholder="Comentario">
                                    <button type="submit">Registrar salida</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                <% } %>
                </tbody>
            </table>
        </div>
    </section>
</main>
</body>
</html>
