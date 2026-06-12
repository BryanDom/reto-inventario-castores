<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.castores.inventario.model.MovimientoDetalle" %>
<%@ page import="com.castores.inventario.model.Usuario" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    List<MovimientoDetalle> movimientos = (List<MovimientoDetalle>) request.getAttribute("movimientos");
    Integer idTipoMovimiento = (Integer) request.getAttribute("idTipoMovimiento");
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    ZoneId zonaUtc = ZoneId.of("UTC");
    ZoneId zonaMexico = ZoneId.of("America/Mexico_City");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial | Inventario Castores</title>
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
            width: min(1120px, calc(100% - 32px));
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
            margin-bottom: 24px;
        }

        .filter-form {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            align-items: end;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        label {
            font-weight: bold;
            font-size: 14px;
        }

        select {
            min-width: 180px;
            padding: 10px 11px;
            border: 1px solid #c7d1db;
            border-radius: 6px;
            font: inherit;
            background: #ffffff;
        }

        select:focus {
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

        .badge {
            display: inline-block;
            min-width: 72px;
            padding: 5px 9px;
            border-radius: 999px;
            text-align: center;
            font-size: 13px;
            font-weight: bold;
        }

        .badge-entrada {
            background: #e7f6ec;
            color: #247344;
        }

        .badge-salida {
            background: #f5e9e7;
            color: #9a3429;
        }

        .empty {
            padding: 26px;
            text-align: center;
            color: #6b7785;
        }

        @media (max-width: 700px) {
            .header,
            .filter-form {
                flex-direction: column;
                align-items: stretch;
            }

            .top-actions {
                justify-content: stretch;
            }

            select,
            button,
            .top-actions .nav-link {
                width: 100%;
                text-align: center;
            }
        }
    </style>
</head>
<body>
<main class="page">
    <header class="header">
        <div>
            <h1>Historial de movimientos</h1>
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

    <section class="panel">
        <form action="<%= request.getContextPath() %>/historico" method="get" class="filter-form">
            <div class="form-group">
                <label for="tipo">Tipo de movimiento</label>
                <select id="tipo" name="tipo">
                    <option value="todos" <%= idTipoMovimiento == null ? "selected" : "" %>>Todos</option>
                    <option value="1" <%= idTipoMovimiento != null && idTipoMovimiento == 1 ? "selected" : "" %>>Entrada</option>
                    <option value="2" <%= idTipoMovimiento != null && idTipoMovimiento == 2 ? "selected" : "" %>>Salida</option>
                </select>
            </div>
            <button type="submit">Filtrar</button>
        </form>
    </section>

    <section class="panel">
        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>Producto</th>
                    <th>Usuario</th>
                    <th>Tipo</th>
                    <th>Cantidad</th>
                    <th>Fecha/Hora</th>
                </tr>
                </thead>
                <tbody>
                <% if (movimientos == null || movimientos.isEmpty()) { %>
                    <tr>
                        <td class="empty" colspan="5">No hay movimientos para mostrar.</td>
                    </tr>
                <% } else { %>
                    <% for (MovimientoDetalle movimiento : movimientos) { %>
                        <tr>
                            <td><%= movimiento.getNombreProducto() %></td>
                            <td><%= movimiento.getNombreUsuario() %></td>
                            <td>
                                <% if (movimiento.getIdTipoMovimiento() == 1) { %>
                                    <span class="badge badge-entrada"><%= movimiento.getTipoMovimiento() %></span>
                                <% } else { %>
                                    <span class="badge badge-salida"><%= movimiento.getTipoMovimiento() %></span>
                                <% } %>
                            </td>
                            <td><%= movimiento.getCantidad() %></td>
                            <td><%= movimiento.getFechaHora() != null ? movimiento.getFechaHora().atZone(zonaUtc).withZoneSameInstant(zonaMexico).format(formatoFecha) : "" %></td>
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
