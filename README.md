# -Torneos-Deportivos-y-Gesti-n-de-Equipos
## Proyecto: Torneos Deportivos y Gestión de Equipos

**Objetivo:** Construir una aplicación para la administración integral de torneos deportivos (inicialmente fútbol) que permita registrar equipos, asignar jugadores y árbitros, gestionar partidos, actualizar rondas, generar cuadros de competición, mostrar tablas de posiciones y ofrecer reportes de desempeño de jugadores.

---

### 1. Actores y Roles

| Actor | Descripción | Permisos |
| --- | --- | --- |
| **Coordinador/Administrador** | Usuario principal que crea y configura torneos; registra equipos, jugadores, árbitros; ingresa resultados y controla el flujo de la competencia. | Alta: CRUD en torneos, equipos, jugadores, árbitros, partidos y resultados. |
| **Árbitro** | Consulta los partidos y horarios asignados. | Solo lectura de horarios, partidos y asignaciones. |
| **Jugador** | Consulta su perfil, posición, calendario y tabla de posiciones. | Solo lectura. |
| **Espectador** | (Opcional) Consulta tablas de posiciones y cuadros de competencia. | Solo lectura pública. |

---

### 2. Funcionalidades Principales

1. **Gestión de Equipos**
    - Registro de equipos: nombre, escudo, categoría.
    - Asignación de jugadores: datos personales (nombre, edad, contacto), posición (delantero, portero, defensa, etc.), número de uniforme.
2. **Gestión de Árbitros**
    - Registro de árbitros: nombre, datos de contacto, nivel o especialidad.
    - Asignación automática o manual de árbitros a partidos según disponibilidad.
3. **Configuración de Torneos**
    - Creación de torneos: nombre, tipo (eliminación directa, ida y vuelta, liguilla), fechas, rondas.
    - Generación de llaves (brackets) según el formato de competencia.
4. **Gestión de Partidos y Resultados**
    - Programación de partidos: fecha, hora, cancha, equipos participantes.
    - Registro de resultados: goles, tarjetas, estadísticas clave (asistencias, atajadas, etc.).
    - Actualización automática de rondas y avance de equipos.
5. **Tablas y Listados**
    - Catálogo de jugadores por equipo (filtrable por posición, número, etc.).
    - Catálogo de árbitros y sus asignaciones.
    - Cuadro o llaves de competencia (bracket view).
    - Tabla de posiciones: partidos jugados, ganados, empatados, perdidos, goles, diferencia de goles, puntos.
6. **Reportes de Desempeño**
    - Estadísticas individuales de jugadores: goles, asistencias, tarjetas, minutos jugados.
    - Rankings por rol o mejor jugador del torneo.
    - Reporte global por equipo: goles a favor/en contra, fair play.

### 3. Requerimientos No Funcionales

- **Persistencia**: Uso de PostgreSQL como sistema gestor de base de datos, con migraciones gestionadas por Flyway.
- **Disponibilidad y Rendimiento**: APIs REST con tiempos de respuesta <300ms; objetivo de 99.9% de uptime.
- **Seguridad**: Autenticación JWT, cifrado TLS para datos en tránsito y cifrado a nivel de columna en reposo en PostgreSQL.
- **Escalabilidad y Despliegue**: Contenedores Docker para cada servicio; orquestación con Kubernetes.
- **Mantenibilidad**: Código modular en Java (Spring Boot) y React (TypeScript), con estándares de estilo y revisión de código.

---

### 3. Casos de Uso Principales

1. **CU-01: Registrar equipo**
    - Actor: Coordinador.
    - Flujo: Completa formulario con nombre y categoría → Guarda en base de datos → Vista de confirmación.
2. **CU-02: Registrar jugador en equipo**
    - Actor: Coordinador.
    - Flujo: Selecciona equipo → Llena datos personales, posición y número → Asigna al equipo.
3. **CU-03: Registrar árbitro**
    - Actor: Coordinador.
    - Flujo: Formulario de datos de árbitro → Guarda.
4. **CU-04: Asignar árbitros a partido**
    - Actor: Coordinador.
    - Flujo: Selecciona partido → Elige árbitro(s) disponibles → Confirma asignación.
5. **CU-05: Crear torneo y generar llaves**
    - Actor: Coordinador.
    - Flujo: Define datos de torneo → Elige formato → Sistema genera cuadro inicial.
6. **CU-06: Registrar resultado de partido**
    - Actor: Coordinador.
    - Flujo: Selecciona partido jugado → Ingresa marcador y estadísticas → Sistema actualiza tabla de posiciones y cuadro.
7. **CU-07: Consultar tabla de posiciones**
    - Actor: Jugador/Árbitro/Espectador.
    - Flujo: Accede a sección de posiciones → Visualiza ranking actualizado.
8. **CU-08: Generar reporte de desempeño**
    - Actor: Coordinador.
    - Flujo: Selecciona torneo → Elige tipo de reporte → Descarga o visualiza.

---

### 4. Modelo de Datos (Entidades y Atributos)

- **Equipo**: idEquipo, nombre, categoría, escudoURL.
- **Jugador**: idJugador, nombre, fechaNacimiento, posición, número, contacto, idEquipo.
- **Árbitro**: idArbitro, nombre, nivel, contacto.
- **Torneo**: idTorneo, nombre, formato, fechaInicio, fechaFin.
- **Ronda**: idRonda, idTorneo, nombreRonda, orden.
- **Partido**: idPartido, idRonda, equipoLocal, equipoVisitante, fechaHora, cancha, idArbitro.
- **Resultado**: idResultado, idPartido, golesLocal, golesVisitante, detallesEstadisticas (JSON).

---

### 5. Arquitectura y Tecnologías Sugeridas

- **Backend**: Java 17+ con Spring Boot
    - Persistencia con Spring Data JPA y PostgreSQL
    - Migraciones de esquema con Flyway
    - Exposición de servicios REST
- **Base de Datos**: PostgreSQL
    - Esquema relacional normalizado
    - Índices en llaves foráneas y columnas de búsqueda frecuente
- **Frontend**: React con TypeScript
    - Consumo de APIs REST
    - Componentes reutilizables y responsive
- **Autenticación y Seguridad**: Spring Security con JWT
    - Roles: ADMIN, REFEREE, PLAYER, VIEWER
- **Pruebas**: JUnit y Mockito para backend; React Testing Library y Jest para frontend
- **Despliegue**: Docker Compose para desarrollo local y orquestación con Kubernetes en producción

---

### 6. Próximos Pasos

1. Definir esquema SQL y scripts de migración en PostgreSQL.
2. Completar formularios en `SistemaAdminFutbol.java` y transformarlos en controladores Spring REST.
3. Implementar entidades JPA y repositorios Spring Data.
4. Desarrollar componentes React y conectar con los servicios backend.
5. Configurar Spring Security y JWT.
6. Escribir pruebas unitarias e integración.
7. Automatizar despliegue con Docker y Kubernetes.

## Estructura

```
torneos-deportivos/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── torneos/
│   │   │   │           ├── config/                  # Configuraciones Spring
│   │   │   │           │   ├── SecurityConfig.java
│   │   │   │           │   └── SwaggerConfig.java
│   │   │   │           ├── controller/              # Controladores REST
│   │   │   │           │   ├── AuthController.java
│   │   │   │           │   ├── EquipoController.java
│   │   │   │           │   └── JugadorController.java
│   │   │   │           ├── dto/                     # DTOs (DTOs_para_API_REST.java)
│   │   │   │           ├── entity/                  # Entidades JPA (EntidadesJPA-*.java)
│   │   │   │           ├── repository/              # Repositorios (Repositorios_*.java)
│   │   │   │           ├── security/                # Configuración JWT
│   │   │   │           │   ├── JwtAuthenticationFilter.java
│   │   │   │           │   └── JwtService.java
│   │   │   │           ├── service/                 # Lógica de negocio
│   │   │   │           │   ├── EquipoService.java
│   │   │   │           │   └── JugadorService.java
│   │   │   │           └── TorneosApplication.java  # Clase principal
│   │   │   └── resources/
│   │   │       ├── db/
│   │   │       │   └── migration/                   # Flyway (V1__*.sql)
│   │   │       └── application.yml                  # Configuración
│   │   └── test/                                    # Pruebas
│   ├── Dockerfile                                   # Imagen Docker
│   └── pom.xml                                      # Dependencias Maven
│
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/              # Componentes React
│   │   │   ├── Equipo/
│   │   │   └── Jugador/
│   │   ├── services/                # Clientes API
│   │   │   └── api.ts
│   │   ├── types/                   # Tipos TypeScript
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── Dockerfile.dev               # Docker para desarrollo
│   └── package.json
│
├── scripts/
│   └── init-db.sql                 # Script inicialización BD
├── docker-compose.yml              # Orquestación servicios
└── README.md                       # Documentación
```
