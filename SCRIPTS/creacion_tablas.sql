SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS HISTORICO_MOVIMIENTOS;
DROP TABLE IF EXISTS ROL_PERMISOS;
DROP TABLE IF EXISTS TIPOS_MOVIMIENTO;
DROP TABLE IF EXISTS PERMISOS;
DROP TABLE IF EXISTS PRODUCTOS;
DROP TABLE IF EXISTS USUARIOS;
DROP TABLE IF EXISTS ROLES;
SET FOREIGN_KEY_CHECKS = 1;

-- 1) ROLES
CREATE TABLE ROLES (
  idRol INT NOT NULL,
  nombreRol VARCHAR(30) NOT NULL,
  estatus INT NOT NULL DEFAULT 1,
  PRIMARY KEY (idRol),
  UNIQUE KEY uq_roles_nombreRol (nombreRol)
) ENGINE=InnoDB;

-- 2) USUARIOS
CREATE TABLE USUARIOS (
  idUsuario INT NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(50) NOT NULL,
  contrasena VARCHAR(255) NOT NULL,
  idRol INT NOT NULL,
  estatus INT NOT NULL DEFAULT 1,
  PRIMARY KEY (idUsuario),
  UNIQUE KEY uq_usuarios_correo (correo),
  KEY ix_usuarios_idRol (idRol),
  CONSTRAINT fk_usuarios_roles
    FOREIGN KEY (idRol) REFERENCES ROLES(idRol)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 3) PERMISOS
CREATE TABLE PERMISOS (
  idPermiso INT NOT NULL AUTO_INCREMENT,
  nombrePermiso VARCHAR(100) NOT NULL,
  PRIMARY KEY (idPermiso),
  UNIQUE KEY uq_permisos_nombrePermiso (nombrePermiso)
) ENGINE=InnoDB;

-- 4) ROL_PERMISOS (puente)
CREATE TABLE ROL_PERMISOS (
  idRol INT NOT NULL,
  idPermiso INT NOT NULL,
  PRIMARY KEY (idRol, idPermiso),
  KEY ix_rol_permisos_idPermiso (idPermiso),
  CONSTRAINT fk_rol_permisos_roles
    FOREIGN KEY (idRol) REFERENCES ROLES(idRol)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_rol_permisos_permisos
    FOREIGN KEY (idPermiso) REFERENCES PERMISOS(idPermiso)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5) PRODUCTOS
CREATE TABLE PRODUCTOS (
  idProducto INT NOT NULL AUTO_INCREMENT,
  nombreProducto VARCHAR(120) NOT NULL,
  descripcion VARCHAR(255) NULL,
  cantidadActual INT NOT NULL DEFAULT 0,
  CONSTRAINT chk_cantidad_positiva CHECK (cantidadActual >= 0),
  estatus INT NOT NULL DEFAULT 1,
  fechaCreacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (idProducto),
  KEY ix_productos_estatus (estatus)
) ENGINE=InnoDB;

-- 6) TIPOS_MOVIMIENTO
CREATE TABLE TIPOS_MOVIMIENTO (
  idTipoMovimiento INT NOT NULL,
  nombreTipo VARCHAR(20) NOT NULL,
  PRIMARY KEY (idTipoMovimiento),
  UNIQUE KEY uq_tipos_mov_nombreTipo (nombreTipo)
) ENGINE=InnoDB;

-- 7) HISTORICO_MOVIMIENTOS
CREATE TABLE HISTORICO_MOVIMIENTOS (
  idMovimiento BIGINT NOT NULL AUTO_INCREMENT,
  idProducto INT NOT NULL,
  idUsuario INT NOT NULL,
  idTipoMovimiento INT NOT NULL,
  cantidad INT NOT NULL,
  fechaHora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  comentario VARCHAR(255) NULL,
  PRIMARY KEY (idMovimiento),
  KEY ix_hist_producto_fecha (idProducto, fechaHora),
  KEY ix_hist_usuario_fecha (idUsuario, fechaHora),
  KEY ix_hist_tipo_fecha (idTipoMovimiento, fechaHora),
  CONSTRAINT fk_hist_productos
    FOREIGN KEY (idProducto) REFERENCES PRODUCTOS(idProducto)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_hist_usuarios
    FOREIGN KEY (idUsuario) REFERENCES USUARIOS(idUsuario)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_hist_tipos
    FOREIGN KEY (idTipoMovimiento) REFERENCES TIPOS_MOVIMIENTO(idTipoMovimiento)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;


-- =========================
-- Datos iniciales (roles, permisos, rol_permisos, tipos_movimiento)
-- =========================

-- ROLES del sistema
INSERT INTO ROLES (idRol, nombreRol, estatus) VALUES
  (1, 'Administrador', 1),
  (2, 'Almacenista', 1);

-- TIPOS_MOVIMIENTO (para filtrar el histórico)
INSERT INTO TIPOS_MOVIMIENTO (idTipoMovimiento, nombreTipo) VALUES
  (1, 'Entrada'),
  (2, 'Salida');

-- PERMISOS (según tu lista). Aquí usamos nombrePermiso como texto descriptivo.
INSERT INTO PERMISOS (nombrePermiso) VALUES
  ('Ver módulo inventario'),
  ('Agregar nuevos productos'),
  ('Aumentar inventario'),
  ('Dar de baja/reactivar producto'),
  ('Ver módulo histórico'),
  ('Ver módulo salida de productos'),
  ('Sacar inventario del almacén');

-- Asignación de permisos por rol
-- Administrador
INSERT INTO ROL_PERMISOS (idRol, idPermiso)
SELECT 1, p.idPermiso
FROM PERMISOS p
WHERE p.nombrePermiso IN (
  'Ver módulo inventario',
  'Agregar nuevos productos',
  'Aumentar inventario',
  'Dar de baja/reactivar producto',
  'Ver módulo histórico'
);

-- Almacenista
INSERT INTO ROL_PERMISOS (idRol, idPermiso)
SELECT 2, p.idPermiso
FROM PERMISOS p
WHERE p.nombrePermiso IN (
  'Ver módulo inventario',
  'Ver módulo salida de productos',
  'Sacar inventario del almacén'
);
