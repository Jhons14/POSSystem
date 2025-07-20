-- -----------------------------------------------------
-- Table "CATEGORIAS"
-- -----------------------------------------------------
CREATE TABLE  CATEGORIAS (
  "id_categoria" SERIAL NOT NULL,
  "descripcion" VARCHAR(45) NOT NULL,
  "estado" BOOLEAN NOT NULL,
  "img" VARCHAR(150) NULL,
  PRIMARY KEY ("id_categoria"));


-- -----------------------------------------------------
-- Table "PRODUCTOS"
-- -----------------------------------------------------
CREATE TABLE  PRODUCTOS (
  "id_producto" SERIAL NOT NULL,
  "nombre" VARCHAR(45) NULL,
  "id_categoria" INT NOT NULL,
  "codigo_barras" VARCHAR(150) NULL,
  "precio_venta" DECIMAL(16,2) NULL,
  "cantidad_stock" INT NOT NULL,
  "img_path" VARCHAR(150) NULL,
  "estado" BOOLEAN NULL,
  PRIMARY KEY ("id_producto"),
  CONSTRAINT "fk_PRODUCTOS_CATEGORIAS"
    FOREIGN KEY ("id_categoria")
    REFERENCES CATEGORIAS ("id_categoria")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table "CLIENTES"
-- -----------------------------------------------------
CREATE TABLE CLIENTES (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(40),
    apellidos VARCHAR(100),
    celular VARCHAR(20),
    direccion VARCHAR(80),
    correo_electronico VARCHAR(70) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_login TIMESTAMP,
    intentos_login INTEGER DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    email_verificado BOOLEAN DEFAULT FALSE,
    cuenta_bloqueada BOOLEAN DEFAULT FALSE,
    token_verificacion VARCHAR(255),
    token_verificacion_expira TIMESTAMP,
    foto_perfil TEXT,
    fecha_nacimiento DATE,
    genero VARCHAR(20)
);


-- -----------------------------------------------------
-- Table "COMPRAS"
-- -----------------------------------------------------
CREATE TABLE  COMPRAS (
  "id_compra" SERIAL NOT NULL,
  "id_cliente" VARCHAR(20) NOT NULL,
  "fecha" TIMESTAMP NULL,
  "medio_pago" CHAR(1) NULL,
  "comentario" VARCHAR(300) NULL,
  "estado" CHAR(1) NULL,
  PRIMARY KEY ("id_compra"),
  CONSTRAINT "fk_COMPRAS_CLIENTES1"
    FOREIGN KEY ("id_cliente")
    REFERENCES CLIENTES ("id")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table "COMPRAS_PRODUCTOS"
-- -----------------------------------------------------
CREATE TABLE  COMPRAS_PRODUCTOS (
  "id_compra" INT NOT NULL,
  "id_producto" INT NOT NULL,
  "cantidad" INT NULL,
  "total" DECIMAL(16,2) NULL,
  "estado" BOOLEAN NULL,
  PRIMARY KEY ("id_compra", "id_producto"),
  CONSTRAINT "fk_COMPRAS_PRODUCTOS_PRODUCTOS1"
    FOREIGN KEY ("id_producto")
    REFERENCES PRODUCTOS ("id_producto")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT "fk_COMPRAS_PRODUCTOS_COMPRAS1"
    FOREIGN KEY ("id_compra")
    REFERENCES COMPRAS ("id_compra")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
