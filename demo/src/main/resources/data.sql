-- ========================================
-- Base de datos de prueba para inventario
-- ========================================
CREATE DATABASE IF NOT EXISTS dlclean;
USE dlclean;

-- ========================================
-- Tabla Producto
-- ========================================
CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    categoria VARCHAR(50),
    stock INT NOT NULL DEFAULT 0
);

INSERT INTO producto (codigo, nombre, precio, categoria, stock) VALUES
(1001, 'Teclado Mecánico', 1200.00, 'SUELTO', 10),
(1002, 'Mouse Gamer', 800.00, 'SUELTO', 15),
(1003, 'Monitor 24"', 25000.00, 'SUELTO', 5),
(1004, 'Notebook i5', 85000.00, 'SUELTO', 7),
(1005, 'Auriculares', 2500.00, 'SUELTO', 20);

-- ========================================
-- Tabla Venta
-- ========================================
CREATE TABLE IF NOT EXISTS venta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    total DOUBLE NOT NULL
);

-- Datos de prueba
INSERT INTO venta (fecha, total) VALUES
('2025-11-01 10:15:00', 4900.00),
('2025-11-02 14:30:00', 4900.00);

-- ========================================
-- Tabla Detalle_Venta
-- ========================================
CREATE TABLE IF NOT EXISTS detalle_venta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES venta(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Datos de prueba
INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario) VALUES
(1, 1, 2, 1200.00),
(1, 5, 1, 2500.00),
(2, 2, 3, 800.00),
(2, 3, 1, 2500.00);
