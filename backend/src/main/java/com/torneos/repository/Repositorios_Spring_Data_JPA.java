package com.torneos.repository;

import com.torneos.entity.Equipo;
import com.torneos.entity.Jugador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ====== EQUIPO REPOSITORY ======
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    /**
     * Buscar equipo por nombre (case insensitive)
     */
    Optional<Equipo> findByNombreIgnoreCase(String nombre);

    /**
     * Buscar equipos por categoría
     */
    List<Equipo> findByCategoriaIgnoreCase(String categoria);

    /**
     * Buscar equipos activos
     */
    List<Equipo> findByActivoTrue();

    /**
     * Buscar equipos por categoría con paginación
     */
    Page<Equipo> findByCategoriaIgnoreCaseAndActivoTrue(String categoria, Pageable pageable);

    /**
     * Buscar equipos por nombre contenido (búsqueda parcial)
     */
    @Query("SELECT e FROM Equipo e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND e.activo = true")
    List<Equipo> findByNombreContainingIgnoreCaseAndActivoTrue(@Param("nombre") String nombre);

    /**
     * Contar equipos por categoría
     */
    @Query("SELECT COUNT(e) FROM Equipo e WHERE e.categoria = :categoria AND e.activo = true")
    Long countEquiposByCategoria(@Param("categoria") String categoria);

    /**
     * Verificar si existe equipo con nombre
     */
    boolean existsByNombreIgnoreCase(String nombre);
}

// ====== JUGADOR REPOSITORY ======
@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    /**
     * Buscar jugadores por equipo
     */
    List<Jugador> findByEquipoIdEquipoAndActivoTrue(Long idEquipo);

    /**
     * Buscar jugadores por posición
     */
    List<Jugador> findByPosicionAndActivoTrue(Jugador.Posicion posicion);

    /**
     * Buscar jugadores por equipo y posición
     */
    List<Jugador> findByEquipoIdEquipoAndPosicionAndActivoTrue(Long idEquipo, Jugador.Posicion posicion);

    /**
     * Buscar jugador por equipo y número de camiseta
     */
    Optional<Jugador> findByEquipoIdEquipoAndNumeroCamiseta(Long idEquipo, Integer numeroCamiseta);

    /**
     * Verificar si existe número de camiseta en un equipo
     */
    boolean existsByEquipoIdEquipoAndNumeroCamiseta(Long idEquipo, Integer numeroCamiseta);

    /**
     * Buscar jugadores por nombre (búsqueda parcial)
     */
    @Query("SELECT j FROM Jugador j WHERE LOWER(j.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND j.activo = true")
    List<Jugador> findByNombreContainingIgnoreCaseAndActivoTrue(@Param("nombre") String nombre);

    /**
     * Contar jugadores por equipo
     */
    @Query("SELECT COUNT(j) FROM Jugador j WHERE j.equipo.idEquipo = :idEquipo AND j.activo = true")
    Long countJugadoresByEquipo(@Param("idEquipo") Long idEquipo);

    /**
     * Obtener jugadores con información del equipo
     */
    @Query("SELECT j FROM Jugador j JOIN FETCH j.equipo WHERE j.activo = true ORDER BY j.equipo.nombre, j.numeroCamiseta")
    List<Jugador> findAllJugadoresConEquipo();

    /**
     * Buscar jugadores por rango de edad
     */
    @Query("SELECT j FROM Jugador j WHERE EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM j.fechaNacimiento) BETWEEN :edadMin AND :edadMax AND j.activo = true")
    List<Jugador> findByRangoEdad(@Param("edadMin") int edadMin, @Param("edadMax") int edadMax);

    /**
     * Obtener estadísticas por posición en un equipo
     */
    @Query("SELECT j.posicion, COUNT(j) FROM Jugador j WHERE j.equipo.idEquipo = :idEquipo AND j.activo = true GROUP BY j.posicion")
    List<Object[]> getEstadisticasPosicionesPorEquipo(@Param("idEquipo") Long idEquipo);
}
