-- Initial database schema for POS System
-- This migration creates the core tables with proper indexing

-- Categories table
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria SERIAL PRIMARY KEY,
    descripcion VARCHAR(45) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE IF NOT EXISTS productos (
    id_producto SERIAL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    id_categoria INTEGER NOT NULL,
    codigo_barras VARCHAR(150),
    precio_venta DECIMAL(16,2) NOT NULL,
    cantidad_stock INTEGER NOT NULL DEFAULT 0,
    estado BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_productos_categoria FOREIGN KEY (id_categoria) 
        REFERENCES categorias (id_categoria) ON DELETE RESTRICT
);

-- Customers table
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    correo_electronico VARCHAR(100) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50),
    celular VARCHAR(20),
    direccion VARCHAR(100),
    fecha_nacimiento DATE,
    estado BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Purchases table
CREATE TABLE IF NOT EXISTS compras (
    id_compra SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    medio_pago CHAR(1) NOT NULL CHECK (medio_pago IN ('E', 'T')), -- E = Efectivo, T = Tarjeta
    comentario VARCHAR(300),
    estado CHAR(1) NOT NULL DEFAULT 'P' CHECK (estado IN ('P', 'C', 'A')), -- P = Pendiente, C = Completado, A = Anulado
    total DECIMAL(16,2) NOT NULL DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_compras_cliente FOREIGN KEY (id_cliente) 
        REFERENCES clientes (id_cliente) ON DELETE RESTRICT
);

-- Purchase items table
CREATE TABLE IF NOT EXISTS compras_productos (
    id_compra INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(16,2) NOT NULL,
    total DECIMAL(16,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_compra, id_producto),
    CONSTRAINT fk_compras_productos_compra FOREIGN KEY (id_compra) 
        REFERENCES compras (id_compra) ON DELETE CASCADE,
    CONSTRAINT fk_compras_productos_producto FOREIGN KEY (id_producto) 
        REFERENCES productos (id_producto) ON DELETE RESTRICT
);

-- Security and audit tables
CREATE TABLE IF NOT EXISTS sesiones (
    id_sesion SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    token_jwt TEXT NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion TIMESTAMP NOT NULL,
    ip_address INET,
    user_agent TEXT,
    activa BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sesiones_cliente FOREIGN KEY (id_cliente) 
        REFERENCES clientes (id_cliente) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS intentos_login (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    ip_address INET NOT NULL,
    exitoso BOOLEAN NOT NULL,
    fecha_intento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mensaje_error TEXT
);

CREATE TABLE IF NOT EXISTS reset_password (
    id SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion TIMESTAMP NOT NULL,
    usado BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_reset_password_cliente FOREIGN KEY (id_cliente) 
        REFERENCES clientes (id_cliente) ON DELETE CASCADE
);

-- Performance indexes
CREATE INDEX idx_productos_categoria ON productos(id_categoria);
CREATE INDEX idx_productos_estado ON productos(estado);
CREATE INDEX idx_productos_codigo_barras ON productos(codigo_barras);
CREATE INDEX idx_productos_nombre ON productos(nombre);

CREATE INDEX idx_compras_cliente ON compras(id_cliente);
CREATE INDEX idx_compras_fecha ON compras(fecha);
CREATE INDEX idx_compras_estado ON compras(estado);

CREATE INDEX idx_compras_productos_producto ON compras_productos(id_producto);

CREATE INDEX idx_clientes_username ON clientes(username);
CREATE INDEX idx_clientes_email ON clientes(correo_electronico);
CREATE INDEX idx_clientes_estado ON clientes(estado);

CREATE INDEX idx_sesiones_cliente ON sesiones(id_cliente);
CREATE INDEX idx_sesiones_token ON sesiones(token_jwt);
CREATE INDEX idx_sesiones_activa ON sesiones(activa);
CREATE INDEX idx_sesiones_expiracion ON sesiones(fecha_expiracion);

CREATE INDEX idx_intentos_login_username ON intentos_login(username);
CREATE INDEX idx_intentos_login_ip ON intentos_login(ip_address);
CREATE INDEX idx_intentos_login_fecha ON intentos_login(fecha_intento);

-- Triggers for automatic timestamp updates
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_categorias_timestamp
    BEFORE UPDATE ON categorias
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trigger_update_productos_timestamp
    BEFORE UPDATE ON productos
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trigger_update_clientes_timestamp
    BEFORE UPDATE ON clientes
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER trigger_update_compras_timestamp
    BEFORE UPDATE ON compras
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();