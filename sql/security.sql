-- ================================================================
-- SCRIPT COMPLETO: TABLA CLIENTES CON SISTEMA DE AUTENTICACIÓN
-- ================================================================

-- 2. TABLA PARA RESETEO SEGURO DE CONTRASEÑAS
CREATE TABLE password_resets (
    id SERIAL PRIMARY KEY,
    cliente_id VARCHAR(20) REFERENCES CLIENTES(id) ON DELETE CASCADE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expira_en TIMESTAMP NOT NULL,
    usado BOOLEAN DEFAULT FALSE,
    ip_solicitud INET,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. TABLA PARA SESIONES (OPCIONAL PERO RECOMENDADA)
CREATE TABLE sesiones (
    id SERIAL PRIMARY KEY,
    cliente_id VARCHAR(20) REFERENCES CLIENTES(id) ON DELETE CASCADE,
    token_sesion VARCHAR(255) NOT NULL UNIQUE,
    ip_address INET,
    user_agent TEXT,
    expira_en TIMESTAMP NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_ultimo_uso TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. TABLA PARA LOG DE INTENTOS DE LOGIN (SEGURIDAD EXTRA)
CREATE TABLE login_attempts (
    id SERIAL PRIMARY KEY,
    cliente_id VARCHAR(20) REFERENCES CLIENTES(id) ON DELETE CASCADE,
    ip_address INET,
    exitoso BOOLEAN DEFAULT FALSE,
    razon_fallo VARCHAR(100), -- 'password_incorrecto', 'usuario_bloqueado', etc.
    user_agent TEXT,
    fecha_intento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ================================================================
-- ÍNDICES PARA RENDIMIENTO AUTOMÁTICO
-- ================================================================

-- Índices en tabla CLIENTES
CREATE INDEX idx_clientes_correo ON CLIENTES(correo_electronico);
CREATE INDEX idx_clientes_username ON CLIENTES(username);
CREATE INDEX idx_clientes_activo ON CLIENTES(activo);
CREATE INDEX idx_clientes_email_verificado ON CLIENTES(email_verificado);
CREATE INDEX idx_clientes_cuenta_bloqueada ON CLIENTES(cuenta_bloqueada);
CREATE INDEX idx_clientes_fecha_registro ON CLIENTES(fecha_registro);

-- Índices en password_resets
CREATE INDEX idx_password_resets_token ON password_resets(token);
CREATE INDEX idx_password_resets_expira ON password_resets(expira_en);
CREATE INDEX idx_password_resets_cliente ON password_resets(cliente_id);
CREATE INDEX idx_password_resets_usado ON password_resets(usado);

-- Índices en sesiones
CREATE INDEX idx_sesiones_token ON sesiones(token_sesion);
CREATE INDEX idx_sesiones_cliente ON sesiones(cliente_id);
CREATE INDEX idx_sesiones_activa ON sesiones(activa);
CREATE INDEX idx_sesiones_expira ON sesiones(expira_en);

-- Índices en login_attempts
CREATE INDEX idx_login_attempts_cliente ON login_attempts(cliente_id);
CREATE INDEX idx_login_attempts_ip ON login_attempts(ip_address);
CREATE INDEX idx_login_attempts_fecha ON login_attempts(fecha_intento);

-- ================================================================
-- FUNCIONES Y TRIGGERS AUTOMÁTICOS
-- ================================================================

-- Función para actualizar fecha_actualizacion automáticamente
CREATE OR REPLACE FUNCTION actualizar_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

-- Trigger que se ejecuta automáticamente en cada UPDATE de CLIENTES
CREATE TRIGGER trigger_actualizar_fecha_clientes
    BEFORE UPDATE ON CLIENTES
    FOR EACH ROW
    EXECUTE FUNCTION actualizar_fecha_modificacion();

-- Función para limpiar tokens expirados automáticamente
CREATE OR REPLACE FUNCTION limpiar_tokens_expirados()
RETURNS void AS $$
BEGIN
    -- Limpiar tokens de reseteo expirados
    DELETE FROM password_resets 
    WHERE expira_en < NOW() OR usado = TRUE;
    
    -- Limpiar sesiones expiradas
    DELETE FROM sesiones 
    WHERE expira_en < NOW();
    
    -- Limpiar intentos de login antiguos (más de 30 días)
    DELETE FROM login_attempts 
    WHERE fecha_intento < NOW() - INTERVAL '30 days';
END;
$$ LANGUAGE 'plpgsql';

-- ================================================================
-- EJEMPLOS DE USO
-- ================================================================

-- Insertar un cliente nuevo
INSERT INTO CLIENTES (
    id, nombre, apellidos, celular, direccion, correo_electronico, 
    username, password_hash
) VALUES (
    'CLI001', 
    'Juan', 
    'Pérez García', 
    '+57 300 123 4567', 
    'Calle 123 #45-67, Bogotá', 
    'juan@email.com',
    'juan_perez',
    '$2b$10$ejemplo_hash_bcrypt_aqui'
);

-- Solicitar reseteo de contraseña
INSERT INTO password_resets (cliente_id, token, expira_en, ip_solicitud)
VALUES (
    'CLI001', 
    'token_super_seguro_' || gen_random_uuid(), 
    NOW() + INTERVAL '1 hour',
    '192.168.1.1'::INET
);

-- Crear sesión después del login
INSERT INTO sesiones (cliente_id, token_sesion, ip_address, user_agent, expira_en)
VALUES (
    'CLI001',
    'session_' || gen_random_uuid(),
    '192.168.1.1'::INET,
    'Mozilla/5.0...',
    NOW() + INTERVAL '7 days'
);

-- ================================================================
-- CONSULTAS ÚTILES PARA EL SISTEMA
-- ================================================================

-- Verificar login (será súper rápido por los índices)
-- SELECT id, nombre, apellidos, password_hash, activo, cuenta_bloqueada
-- FROM CLIENTES 
-- WHERE (username = 'juan_perez' OR correo_electronico = 'juan@email.com')
--   AND activo = TRUE 
--   AND cuenta_bloqueada = FALSE;

-- Verificar token de reseteo (súper rápido)
-- SELECT pr.*, c.correo_electronico, c.nombre 
-- FROM password_resets pr 
-- JOIN CLIENTES c ON pr.cliente_id = c.id 
-- WHERE pr.token = 'token_aqui' 
--   AND pr.expira_en > NOW() 
--   AND pr.usado = FALSE;

-- Verificar sesión activa (súper rápido)
-- SELECT s.*, c.nombre, c.apellidos
-- FROM sesiones s
-- JOIN CLIENTES c ON s.cliente_id = c.id
-- WHERE s.token_sesion = 'session_token_aqui'
--   AND s.expira_en > NOW()
--   AND s.activa = TRUE
--   AND c.activo = TRUE
--   AND c.cuenta_bloqueada = FALSE;