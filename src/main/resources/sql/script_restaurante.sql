-- Crear base de datos para el restaurante
DROP DATABASE IF EXISTS bd_restaurante;
CREATE DATABASE bd_restaurante CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_restaurante;

-- Tabla de pedidos
CREATE TABLE pedidos (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         cliente_nombre VARCHAR(100) NOT NULL,
                         cliente_telefono VARCHAR(15) NOT NULL,
                         cliente_direccion VARCHAR(200) NOT NULL,
                         producto VARCHAR(100) NOT NULL,
                         categoria ENUM('Pollo', 'Pizza', 'Pasta') NOT NULL,
                         cantidad INT NOT NULL,
                         precio DECIMAL(10,2) NOT NULL,
                         estado ENUM('Recibido', 'En Preparación', 'En Camino', 'Entregado', 'Cancelado') DEFAULT 'Recibido',
                         observaciones TEXT,
                         fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo
INSERT INTO pedidos (cliente_nombre, cliente_telefono, cliente_direccion, producto, categoria, cantidad, precio, observaciones) VALUES
                                                                                                                                    ('Juan Pérez', '987654321', 'Av. Las Flores 123 - Miraflores', 'Pollo a la Brasa Familiar', 'Pollo', 1, 45.00, 'Con papas fritas y ensalada'),
                                                                                                                                    ('María García', '987654322', 'Jr. Los Olivos 456 - San Isidro', 'Pizza Hawaiana Grande', 'Pizza', 2, 58.00, 'Extra queso'),
                                                                                                                                    ('Carlos López', '987654323', 'Av. Arequipa 789 - Lince', 'Lasagna de Carne', 'Pasta', 1, 32.00, 'Sin espinacas'),
                                                                                                                                    ('Ana Torres', '987654324', 'Calle Lima 234 - Surco', 'Alitas BBQ', 'Pollo', 3, 36.00, 'Picante'),
                                                                                                                                    ('Roberto Díaz', '987654325', 'Av. Javier Prado 567 - La Molina', 'Pizza Pepperoni Mediana', 'Pizza', 1, 42.00, 'Bien crocante'),
                                                                                                                                    ('Laura Mendoza', '987654326', 'Jr. Los Pinos 890 - Barranco', 'Fettuccine Alfredo', 'Pasta', 2, 48.00, 'Con pollo a la plancha');

-- Verificar datos
SELECT * FROM pedidos;