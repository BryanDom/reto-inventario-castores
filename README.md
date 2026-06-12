# Inventario Castores

Sistema web de inventario desarrollado en Java MVC con Maven, JSP, Servlets, MySQL y Apache Tomcat. El proyecto implementa autenticacion por roles, administracion de productos, registro de salidas e historial de movimientos.

## Entorno

- IDE: IntelliJ IDEA Community 2026.1
- Lenguaje: Java 17 (Eclipse Temurin 17.0.19)
- DBMS: MySQL 8.0.46
- Servidor: Apache Tomcat 9.0.118

## Pasos para correr la aplicacion

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/BryanDom/reto-inventario-castores.git
   ```

2. Tener instalado MySQL 8.0.46 y crear la base de datos:

   ```sql
   CREATE DATABASE inventario_castores;
   ```

3. Ejecutar el script de creacion de tablas:

   ```bash
   mysql -u root -p inventario_castores < SCRIPTS/creacion_tablas.sql
   ```

4. Ejecutar el script de datos iniciales:

   ```bash
   mysql -u root -p inventario_castores < SCRIPTS/datos_iniciales.sql
   ```

5. En IntelliJ IDEA, configurar Apache Tomcat 9:

   ```text
   File > Settings > Application Servers > agregar Tomcat 9
   ```

6. Configurar el despliegue de la aplicacion:

   ```text
   Run > Edit Configurations > Tomcat Local > pestaña Deployment
   ```

   Agregar:

   ```text
   inventario-castores:war exploded
   ```

   Y configurar:

   ```text
   Application context: /inventario-castores
   ```

7. Dar Run y abrir:

   [http://localhost:8080/inventario-castores](http://localhost:8080/inventario-castores)

## Usuarios de prueba

- Administrador: `admin@castores.com` / `admin123`
- Almacenista: `almacen@castores.com` / `almacen123`

## Modulos del sistema

- Login con autenticacion por rol.
- Inventario: consulta de productos disponible para Administrador y Almacenista. Las acciones de agregar productos, aumentar existencias, dar de baja y reactivar productos estan disponibles solo para Administrador.
- Salida de productos: registrar salida y restar inventario validando existencias disponibles. Disponible solo para Almacenista.
- Historial de movimientos con filtro por tipo de movimiento. Disponible solo para Administrador.

## Notas tecnicas

- La aplicacion usa Servlets con `javax.servlet`, compatible con Tomcat 9.
- La conexion a base de datos se realiza mediante JDBC y `mysql-connector-j`.
- El historial muestra la fecha/hora en zona horaria de Mexico (`America/Mexico_City`).
- El control de acceso se realiza validando el usuario guardado en sesion y su `idRol`.
