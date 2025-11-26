-- Script SQL para PostgreSQL - Tabla parqueadero_vehiculos
-- Basado en la entidad ParqueaderoVehiculo

-- Crear el schema si no existe (opcional)
CREATE SCHEMA IF NOT EXISTS prueba_bio_code;

-- Crear el tipo ENUM para los tipos de vehículo
CREATE TYPE prueba_bio_code.tipo_vehiculo AS ENUM (
    'CARRO',
    'MOTO',
    'CAMIONETA',
    'CAMION',
    'BICICLETA'
);

-- Crear la tabla parqueadero_vehiculos
CREATE TABLE prueba_bio_code.parqueadero_vehiculos (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(15) NOT NULL UNIQUE,
    tipo prueba_bio_code.tipo_vehiculo NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    color VARCHAR(30),
    fecha_entrada TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    fecha_salida TIMESTAMP WITH TIME ZONE,
    creado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    actualizado_en TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Crear índices para optimizar consultas
CREATE INDEX idx_parqueadero_vehiculos_placa ON prueba_bio_code.parqueadero_vehiculos(placa);
CREATE INDEX idx_parqueadero_vehiculos_tipo ON prueba_bio_code.parqueadero_vehiculos(tipo);
CREATE INDEX idx_parqueadero_vehiculos_fecha_entrada ON prueba_bio_code.parqueadero_vehiculos(fecha_entrada);
CREATE INDEX idx_parqueadero_vehiculos_fecha_salida ON prueba_bio_code.parqueadero_vehiculos(fecha_salida);

-- Índice para consultar vehículos actualmente parqueados (fecha_salida IS NULL)
CREATE INDEX idx_parqueadero_vehiculos_parqueados ON prueba_bio_code.parqueadero_vehiculos(fecha_salida)
WHERE fecha_salida IS NULL;

-- Trigger para actualizar automaticamente el campo actualizado_en
CREATE OR REPLACE FUNCTION prueba_bio_code.update_actualizado_en_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.actualizado_en = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_parqueadero_vehiculos_actualizado_en
    BEFORE UPDATE ON prueba_bio_code.parqueadero_vehiculos
    FOR EACH ROW
    EXECUTE FUNCTION prueba_bio_code.update_actualizado_en_column();

-- Comentarios en la tabla y columnas
COMMENT ON TABLE prueba_bio_code.parqueadero_vehiculos IS 'Tabla para gestionar el registro de vehículos en el parqueadero';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.id IS 'Identificador único del registro';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.placa IS 'Placa del vehículo (única)';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.tipo IS 'Tipo de vehículo (CARRO, MOTO, etc.)';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.marca IS 'Marca del vehículo';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.modelo IS 'Modelo del vehículo';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.color IS 'Color del vehículo';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.fecha_entrada IS 'Fecha y hora de entrada al parqueadero';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.fecha_salida IS 'Fecha y hora de salida del parqueadero (NULL si está parqueado)';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.creado_en IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN prueba_bio_code.parqueadero_vehiculos.actualizado_en IS 'Fecha y hora de última actualización';

-- Datos de prueba (opcional)
INSERT INTO prueba_bio_code.parqueadero_vehiculos (placa, tipo, marca, modelo, color) VALUES
('ABC123', 'CARRO', 'Toyota', 'Corolla', 'Blanco'),
('XYZ789', 'MOTO', 'Yamaha', 'R1', 'Negro'),
('DEF456', 'CAMIONETA', 'Ford', 'F-150', 'Azul');

SELECT id, placa, tipo, marca, modelo, color, fecha_entrada, fecha_salida, creado_en, actualizado_en
FROM prueba_bio_code.parqueadero_vehiculos;



-- Crear la tabla dispositivos
CREATE TABLE IF NOT EXISTS dispositivos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL CHECK (tipo IN (
        'COMPUTADORA', 'MONITOR', 'TECLADO', 'MOUSE', 'IMPRESORA',
        'PROYECTOR', 'TABLET', 'SMARTPHONE', 'ROUTER', 'SWITCH',
        'ACCESS_POINT', 'SERVIDOR', 'UPS', 'SCANNER', 'CAMARA',
        'MICROFONO', 'PARLANTES', 'OTROS'
    )),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    numero_serie VARCHAR(100) UNIQUE,
    codigo_inventario VARCHAR(50) UNIQUE,
    estado VARCHAR(20) NOT NULL CHECK (estado IN (
        'ACTIVO', 'INACTIVO', 'EN_MANTENIMIENTO', 'DAÑADO',
        'FUERA_DE_SERVICIO', 'EN_PRESTAMO', 'PERDIDO', 'DADO_DE_BAJA'
    )),
    ubicacion VARCHAR(100),
    responsable VARCHAR(100),
    fecha_adquisicion TIMESTAMP,
    fecha_ultimo_mantenimiento TIMESTAMP,
    observaciones TEXT,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_dispositivos_tipo ON dispositivos(tipo);
CREATE INDEX IF NOT EXISTS idx_dispositivos_estado ON dispositivos(estado);
CREATE INDEX IF NOT EXISTS idx_dispositivos_ubicacion ON dispositivos(ubicacion);
CREATE INDEX IF NOT EXISTS idx_dispositivos_responsable ON dispositivos(responsable);
CREATE INDEX IF NOT EXISTS idx_dispositivos_numero_serie ON dispositivos(numero_serie);
CREATE INDEX IF NOT EXISTS idx_dispositivos_codigo_inventario ON dispositivos(codigo_inventario);
CREATE INDEX IF NOT EXISTS idx_dispositivos_fecha_ultimo_mantenimiento ON dispositivos(fecha_ultimo_mantenimiento);

-- Función para actualizar automáticamente el campo actualizado_en
CREATE OR REPLACE FUNCTION update_actualizado_en_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.actualizado_en = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger para actualizar automáticamente actualizado_en
DROP TRIGGER IF EXISTS update_dispositivos_actualizado_en ON dispositivos;
CREATE TRIGGER update_dispositivos_actualizado_en
    BEFORE UPDATE ON dispositivos
    FOR EACH ROW
    EXECUTE FUNCTION update_actualizado_en_column();


ALTER TABLE prueba_bio_code.dispositivos
  ALTER COLUMN creado_en SET DEFAULT NOW(),
  ALTER COLUMN actualizado_en SET DEFAULT NOW();

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS trigger AS $$
BEGIN
  NEW.actualizado_en := NOW();
  RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER dispositivos_set_updated_at
BEFORE UPDATE ON prueba_bio_code.dispositivos
FOR EACH ROW EXECUTE FUNCTION set_updated_at();


-- Agregar comentarios a la tabla y columnas
COMMENT ON TABLE dispositivos IS 'Tabla para el control y gestión de dispositivos tecnológicos';
COMMENT ON COLUMN dispositivos.id IS 'Identificador único del dispositivo';
COMMENT ON COLUMN dispositivos.nombre IS 'Nombre descriptivo del dispositivo';
COMMENT ON COLUMN dispositivos.tipo IS 'Tipo de dispositivo tecnológico';
COMMENT ON COLUMN dispositivos.marca IS 'Marca del dispositivo';
COMMENT ON COLUMN dispositivos.modelo IS 'Modelo específico del dispositivo';
COMMENT ON COLUMN dispositivos.numero_serie IS 'Número de serie único del dispositivo';
COMMENT ON COLUMN dispositivos.codigo_inventario IS 'Código de inventario institucional';
COMMENT ON COLUMN dispositivos.estado IS 'Estado actual del dispositivo';
COMMENT ON COLUMN dispositivos.ubicacion IS 'Ubicación física del dispositivo';
COMMENT ON COLUMN dispositivos.responsable IS 'Persona responsable del dispositivo';
COMMENT ON COLUMN dispositivos.fecha_adquisicion IS 'Fecha de adquisición del dispositivo';
COMMENT ON COLUMN dispositivos.fecha_ultimo_mantenimiento IS 'Fecha del último mantenimiento realizado';
COMMENT ON COLUMN dispositivos.observaciones IS 'Observaciones y notas sobre el dispositivo';
COMMENT ON COLUMN dispositivos.creado_en IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN dispositivos.actualizado_en IS 'Fecha y hora de última actualización del registro';


INSERT INTO prueba_bio_code.dispositivos
(nombre, tipo, marca, modelo, numero_serie, codigo_inventario, estado, ubicacion, responsable,
 fecha_adquisicion, fecha_ultimo_mantenimiento, observaciones, actualizado_en)
VALUES
('Laptop Dell Inspiron 15','COMPUTADORA','Dell','Inspiron 15 3515','DL20240001','INV-COMP-001','ACTIVO','Aula de Informática 1','Prof. María García','2024-01-15 10:00:00','2024-06-15 14:30:00','Computadora principal para clases de programación', NOW()),
('Desktop HP Pavilion','COMPUTADORA','HP','Pavilion Desktop TP01','HP20240002','INV-COMP-002','ACTIVO','Laboratorio de Sistemas','Ing. Carlos Ruiz','2024-02-01 09:00:00','2024-07-01 16:00:00','Estación de trabajo para desarrollo', NOW()),
('Laptop Lenovo ThinkPad','COMPUTADORA','Lenovo','ThinkPad E14','LN20240003','INV-COMP-003','EN_MANTENIMIENTO','Taller de Mantenimiento','Técnico José Luis','2023-12-10 11:00:00','2024-05-20 10:00:00','Requiere cambio de disco duro', NOW()),
('MacBook Air M2','COMPUTADORA','Apple','MacBook Air M2','AP20240004','INV-COMP-004','ACTIVO','Oficina de Dirección','Director Ana López','2024-03-01 12:00:00','2024-08-01 09:00:00','Equipo para diseño gráfico', NOW()),
('Desktop Acer Aspire','COMPUTADORA','Acer','Aspire TC-1780','AC20240005','INV-COMP-005','DAÑADO','Depósito','Auxiliar Pedro Sánchez','2023-11-15 15:00:00','2024-04-15 11:00:00','Placa madre dañada, requiere reemplazo', NOW());


