-- Datos iniciales del sistema
-- Ejecutar despues de SCRIPTS/creacion_tablas.sql

-- ROLES
INSERT INTO ROLES (idRol, nombreRol, estatus) VALUES
  (1, 'Administrador', 1),
  (2, 'Almacenista', 1)
ON DUPLICATE KEY UPDATE
  nombreRol = VALUES(nombreRol),
  estatus = VALUES(estatus);

-- TIPOS DE MOVIMIENTO
INSERT INTO TIPOS_MOVIMIENTO (idTipoMovimiento, nombreTipo) VALUES
  (1, 'Entrada'),
  (2, 'Salida')
ON DUPLICATE KEY UPDATE
  nombreTipo = VALUES(nombreTipo);

-- PERMISOS
INSERT IGNORE INTO PERMISOS (nombrePermiso) VALUES
  ('Ver modulo inventario'),
  ('Agregar nuevos productos'),
  ('Aumentar inventario'),
  ('Dar de baja/reactivar producto'),
  ('Ver modulo historico'),
  ('Ver modulo salida de productos'),
  ('Sacar inventario del almacen');

-- PERMISOS DEL ADMINISTRADOR
INSERT IGNORE INTO ROL_PERMISOS (idRol, idPermiso)
SELECT 1, p.idPermiso
FROM PERMISOS p
WHERE p.nombrePermiso IN (
  'Ver modulo inventario',
  'Agregar nuevos productos',
  'Aumentar inventario',
  'Dar de baja/reactivar producto',
  'Ver modulo historico'
);

-- PERMISOS DEL ALMACENISTA
INSERT IGNORE INTO ROL_PERMISOS (idRol, idPermiso)
SELECT 2, p.idPermiso
FROM PERMISOS p
WHERE p.nombrePermiso IN (
  'Ver modulo salida de productos',
  'Sacar inventario del almacen'
);

-- USUARIOS DE PRUEBA
INSERT INTO USUARIOS (idUsuario, nombre, correo, contrasena, idRol, estatus) VALUES
  (1, 'Administrador', 'admin@castores.com', 'admin123', 1, 1),
  (2, 'Almacenista', 'almacen@castores.com', 'almacen123', 2, 1)
ON DUPLICATE KEY UPDATE
  nombre = VALUES(nombre),
  correo = VALUES(correo),
  contrasena = VALUES(contrasena),
  idRol = VALUES(idRol),
  estatus = VALUES(estatus);
