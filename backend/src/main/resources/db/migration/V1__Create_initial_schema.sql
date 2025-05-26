-- Torneos Deportivos - Schema inicial
-- Versión: V1__Create_initial_schema.sql

-- Tabla de usuarios (para autenticación)
CREATE TABLE usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'REFEREE', 'PLAYER', 'VIEWER')),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de equipos
CREATE TABLE equipos (
    id_equipo BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    escudo_url VARCHAR(255),
    descripcion TEXT,
    fecha_fundacion DATE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de jugadores
CREATE TABLE jugadores (
    id_jugador BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT REFERENCES usuarios(id_usuario),
    id_equipo BIGINT REFERENCES equipos(id_equipo),
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    posicion VARCHAR(30) NOT NULL CHECK (posicion IN ('PORTERO', 'DEFENSA', 'MEDIO', 'DELANTERO')),
    numero_camiseta INTEGER NOT NULL,
    contacto_emergencia VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_equipo, numero_camiseta)
);

-- Tabla de árbitros
CREATE TABLE arbitros (
    id_arbitro BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT REFERENCES usuarios(id_usuario),
    nombre VARCHAR(100) NOT NULL,
    nivel VARCHAR(30) NOT NULL CHECK (nivel IN ('NACIONAL', 'REGIONAL', 'LOCAL')),
    especialidad VARCHAR(50),
    contacto VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de torneos
CREATE TABLE torneos (
    id_torneo BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    formato VARCHAR(30) NOT NULL CHECK (formato IN ('ELIMINACION_DIRECTA', 'IDA_VUELTA', 'LIGUILLA')),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    max_equipos INTEGER DEFAULT 16,
    estado VARCHAR(20) DEFAULT 'PROGRAMADO' CHECK (estado IN ('PROGRAMADO', 'EN_CURSO', 'FINALIZADO', 'CANCELADO')),
    descripcion TEXT,
    premiacion JSONB,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de rondas
CREATE TABLE rondas (
    id_ronda BIGSERIAL PRIMARY KEY,
    id_torneo BIGINT NOT NULL REFERENCES torneos(id_torneo),
    nombre_ronda VARCHAR(50) NOT NULL,
    orden_ronda INTEGER NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_torneo, orden_ronda)
);

-- Tabla de partidos
CREATE TABLE partidos (
    id_partido BIGSERIAL PRIMARY KEY,
    id_ronda BIGINT NOT NULL REFERENCES rondas(id_ronda),
    equipo_local BIGINT NOT NULL REFERENCES equipos(id_equipo),
    equipo_visitante BIGINT NOT NULL REFERENCES equipos(id_equipo),
    id_arbitro BIGINT REFERENCES arbitros(id_arbitro),
    fecha_hora TIMESTAMP NOT NULL,
    cancha VARCHAR(100),
    estado VARCHAR(20) DEFAULT 'PROGRAMADO' CHECK (estado IN ('PROGRAMADO', 'EN_CURSO', 'FINALIZADO', 'SUSPENDIDO', 'CANCELADO')),
    minuto_actual INTEGER DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (equipo_local != equipo_visitante)
);

-- Tabla de resultados
CREATE TABLE resultados (
    id_resultado BIGSERIAL PRIMARY KEY,
    id_partido BIGINT UNIQUE NOT NULL REFERENCES partidos(id_partido),
    goles_local INTEGER DEFAULT 0,
    goles_visitante INTEGER DEFAULT 0,
    tarjetas_amarillas_local INTEGER DEFAULT 0,
    tarjetas_rojas_local INTEGER DEFAULT 0,
    tarjetas_amarillas_visitante INTEGER DEFAULT 0,
    tarjetas_rojas_visitante INTEGER DEFAULT 0,
    detalles_estadisticas JSONB,
    observaciones TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de inscripciones (equipos en torneos)
CREATE TABLE inscripciones (
    id_inscripcion BIGSERIAL PRIMARY KEY,
    id_torneo BIGINT NOT NULL REFERENCES torneos(id_torneo),
    id_equipo BIGINT NOT NULL REFERENCES equipos(id_equipo),
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    UNIQUE(id_torneo, id_equipo)
);

-- Tabla de estadísticas de jugadores por partido
CREATE TABLE estadisticas_jugador (
    id_estadistica BIGSERIAL PRIMARY KEY,
    id_partido BIGINT NOT NULL REFERENCES partidos(id_partido),
    id_jugador BIGINT NOT NULL REFERENCES jugadores(id_jugador),
    goles INTEGER DEFAULT 0,
    asistencias INTEGER DEFAULT 0,
    tarjetas_amarillas INTEGER DEFAULT 0,
    tarjetas_rojas INTEGER DEFAULT 0,
    minutos_jugados INTEGER DEFAULT 0,
    atajadas INTEGER DEFAULT 0, -- Para porteros
    detalles_adicionales JSONB,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_partido, id_jugador)
);

-- Índices para optimizar consultas
CREATE INDEX idx_usuarios_username ON usuarios(username);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_jugadores_equipo ON jugadores(id_equipo);
CREATE INDEX idx_jugadores_posicion ON jugadores(posicion);
CREATE INDEX idx_partidos_fecha ON partidos(fecha_hora);
CREATE INDEX idx_partidos_ronda ON partidos(id_ronda);
CREATE INDEX idx_partidos_equipos ON partidos(equipo_local, equipo_visitante);
CREATE INDEX idx_resultados_partido ON resultados(id_partido);
CREATE INDEX idx_inscripciones_torneo ON inscripciones(id_torneo);
CREATE INDEX idx_estadisticas_partido ON estadisticas_jugador(id_partido);
CREATE INDEX idx_estadisticas_jugador ON estadisticas_jugador(id_jugador);

-- Trigger para actualizar fecha_actualizacion automáticamente
CREATE OR REPLACE FUNCTION update_fecha_actualizacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger a las tablas principales
CREATE TRIGGER trigger_usuarios_fecha_actualizacion
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION update_fecha_actualizacion();

CREATE TRIGGER trigger_equipos_fecha_actualizacion
    BEFORE UPDATE ON equipos
    FOR EACH ROW EXECUTE FUNCTION update_fecha_actualizacion();

CREATE TRIGGER trigger_jugadores_fecha_actualizacion
    BEFORE UPDATE ON jugadores
    FOR EACH ROW EXECUTE FUNCTION update_fecha_actualizacion();

CREATE TRIGGER trigger_arbitros_fecha_actualizacion
    BEFORE UPDATE ON arbitros
    FOR EACH ROW EXECUTE FUNCTION update_fecha_actualizacion();

CREATE TRIGGER trigger_torneos_fecha_actualizacion
    BEFORE UPDATE ON torneos
    FOR EACH ROW EXECUTE FUNCTION update_fecha_actualizacion();

CREATE TRIGGER trigger_partidos_fecha_actualizacion
    BEFORE UPDATE ON partidos
    FOR EACH ROW EXECUTE FUNCTION update_fecha_actualizacion();

CREATE TRIGGER trigger_resultados_fecha_actualizacion
    BEFORE UPDATE ON resultados
    FOR EACH ROW EXECUTE FUNCTION update_fecha_actualizacion();