package com.torneos.repository;

import com.torneos.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    // Validar existencia por equipo y número de camiseta
    boolean existsByEquipoIdEquipoAndNumeroCamiseta(Long idEquipo, Integer numeroCamiseta);

    // Listar jugadores activos por equipo
    List<Jugador> findByEquipoIdEquipoAndActivoTrue(Long idEquipo);

    // Búsqueda por nombre (insensible a mayúsculas)
    List<Jugador> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}