<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Inventario Castores</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }

        .login-container {
            width: 100%;
            max-width: 400px;
            background: #ffffff;
            border: 1px solid #d9e1e8;
            border-radius: 8px;
            box-shadow: 0 10px 25px rgba(31, 45, 61, 0.12);
            padding: 32px;
        }

        .login-header {
            margin-bottom: 24px;
            text-align: center;
        }

        .login-header h1 {
            margin: 0 0 8px;
            font-size: 26px;
            color: #1f2d3d;
        }

        .login-header p {
            margin: 0;
            color: #6b7785;
            font-size: 14px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: bold;
            font-size: 14px;
        }

        input {
            width: 100%;
            padding: 11px 12px;
            border: 1px solid #c7d1db;
            border-radius: 6px;
            font-size: 15px;
        }

        input:focus {
            outline: none;
            border-color: #1f7a8c;
            box-shadow: 0 0 0 3px rgba(31, 122, 140, 0.15);
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

        button {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 6px;
            background: #1f7a8c;
            color: #ffffff;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #186575;
        }
    </style>
</head>
<body>
<main class="login-container">
    <div class="login-header">
        <h1>Inventario Castores</h1>
        <p>Ingresa tus credenciales para continuar</p>
    </div>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/login" method="post">
        <div class="form-group">
            <label for="correo">Correo</label>
            <input type="email" id="correo" name="correo" required autocomplete="username">
        </div>

        <div class="form-group">
            <label for="contrasena">Contrasena</label>
            <input type="password" id="contrasena" name="contrasena" required autocomplete="current-password">
        </div>

        <button type="submit">Iniciar sesion</button>
    </form>
</main>
</body>
</html>
