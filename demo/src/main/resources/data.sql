-- Activa las foreign keys
PRAGMA foreign_keys = ON;

-- ==============================
-- Tabla Producto
-- ==============================
CREATE TABLE IF NOT EXISTS producto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo INTEGER NOT NULL,
    nombre TEXT NOT NULL,
    precio REAL NOT NULL,
    categoria TEXT,
    stock INTEGER NOT NULL DEFAULT 0
);

INSERT INTO producto (codigo, nombre, precio, categoria, stock) VALUES
(1001, 'Teclado Mecánico', 1200.00, 'SUELTO', 10),
(1002, 'Mouse Gamer', 800.00, 'SUELTO', 15),
(1003, 'Monitor 24"', 25000.00, 'SUELTO', 5),
(1004, 'Notebook i5', 85000.00, 'SUELTO', 7),
(1005, 'Auriculares', 2500.00, 'SUELTO', 20);

-- ==============================
-- Tabla Venta
-- ==============================
CREATE TABLE IF NOT EXISTS venta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha TEXT NOT NULL,       -- Convertido DATETIME → TEXT
    total REAL NOT NULL,
    metodo_pago TEXT           -- Ya que agregaste este campo
);

INSERT INTO venta (fecha, total, metodo_pago) VALUES
('2025-11-01 10:15:00', 4900.00, 'MERCADOPAGO'),
('2025-11-02 14:30:00', 4900.00, 'TARJETA');

-- ==============================
-- Tabla Detalle_Venta
-- ==============================
CREATE TABLE IF NOT EXISTS detalle_venta (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    venta_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario REAL NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES venta(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario) VALUES
(1, 1, 2, 1200.00),
(1, 5, 1, 2500.00),
(2, 2, 3, 800.00),
(2, 3, 1, 2500.00);
