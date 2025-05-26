package com.torneos.service;

import com.torneos.dto.JugadorRequestDTO;
import com.torneos.dto.JugadorResponseDTO;
import com.torneos.dto.EquipoBasicoDTO;
import com.torneos.entity.Equipo;
import com.torneos.entity.Jugador;
import com.torneos.exception.TorneosException;
import com.torneos.repository.EquipoRepository;
import com.torneos.repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    /**
     * Registrar nuevo jugador
     */
    public JugadorResponseDTO registrarJugador(JugadorRequestDTO request) {
        // Validar que existe el equipo
        Equipo equipo = equipoRepository.findById(request.getIdEquipo())
                .orElseThrow(() -> new TorneosException("Equipo no encontrado con ID: " + request.getIdEquipo()));

        // Validar número de camiseta único en el equipo
        if (jugadorRepository.existsByEquipoIdEquipoAndNumeroCamiseta(request.getIdEquipo(),
                request.getNumeroCamiseta())) {
            throw new TorneosException("Ya existe un jugador con el número " + request.getNumeroCamiseta() +
                    " en el equipo " + equipo.getNombre());
        }

        Jugador jugador = new Jugador();
        jugador.setIdUsuario(request.getIdUsuario());
        jugador.setNombre(request.getNombre());
        jugador.setFechaNacimiento(request.getFechaNacimiento());
        jugador.setPosicion(request.getPosicion());
        jugador.setNumeroCamiseta(request.getNumeroCamiseta());
        jugador.setContactoEmergencia(request.getContactoEmergencia());
        jugador.setEquipo(equipo);

        Jugador jugadorGuardado = jugadorRepository.save(jugador);
        return convertirAJugadorResponseDTO(jugadorGuardado);
    }

    /**
     * Obtener jugador por ID
     */
    @Transactional(readOnly = true)
    public JugadorResponseDTO obtenerJugador(Long idJugador) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new TorneosException("Jugador no encontrado con ID: " + idJugador));

        return convertirAJugadorResponseDTO(jugador);
    }

    /**
     * Listar jugadores por equipo
     */
    @Transactional(readOnly = true)
    public List<JugadorResponseDTO> listarJugadoresPorEquipo(Long idEquipo) {
        List<Jugador> jugadores = jugadorRepository.findByEquipoIdEquipoAndActivoTrue(idEquipo);
        return jugadores.stream()
                .map(this::convertirAJugadorResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar jugador
     */
    public JugadorResponseDTO actualizarJugador(Long idJugador, JugadorRequestDTO request) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new TorneosException("Jugador no encontrado con ID: " + idJugador));

        // Validar número de camiseta único si cambió
        if (!jugador.getNumeroCamiseta().equals(request.getNumeroCamiseta()) &&
                jugadorRepository.existsByEquipoIdEquipoAndNumeroCamiseta(
                        jugador.getEquipo().getIdEquipo(), request.getNumeroCamiseta())) {
            throw new TorneosException("El número " + request.getNumeroCamiseta() + " ya está en uso en este equipo");
        }

        jugador.setNombre(request.getNombre());
        jugador.setFechaNacimiento(request.getFechaNacimiento());
        jugador.setPosicion(request.getPosicion());
        jugador.setNumeroCamiseta(request.getNumeroCamiseta());
        jugador.setContactoEmergencia(request.getContactoEmergencia());

        Jugador jugadorActualizado = jugadorRepository.save(jugador);
        return convertirAJugadorResponseDTO(jugadorActualizado);
    }

    /**
     * Eliminar jugador (soft delete)
     */
    public void eliminarJugador(Long idJugador) {
        Jugador jugador = jugadorRepository.findById(idJugador)
                .orElseThrow(() -> new TorneosException("Jugador no encontrado con ID: " + idJugador));

        jugador.setActivo(false);
        jugadorRepository.save(jugador);
    }

    /**
     * Buscar jugadores por nombre
     */
    @Transactional(readOnly = true)
    public List<JugadorResponseDTO> buscarJugadoresPorNombre(String nombre) {
        List<Jugador> jugadores = jugadorRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
        return jugadores.stream()
                .map(this::convertirAJugadorResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir Entity a DTO
     */
    private JugadorResponseDTO convertirAJugadorResponseDTO(Jugador jugador) {
        JugadorResponseDTO dto = new JugadorResponseDTO();
        dto.setIdJugador(jugador.getIdJugador());
        dto.setNombre(jugador.getNombre());
        dto.setFechaNacimiento(jugador.getFechaNacimiento());
        dto.setEdad(calcularEdad(jugador.getFechaNacimiento()));
        dto.setPosicion(jugador.getPosicion());
        dto.setNumeroCamiseta(jugador.getNumeroCamiseta());
        dto.setContactoEmergencia(jugador.getContactoEmergencia());
        dto.setActivo(jugador.getActivo());
        dto.setFechaCreacion(jugador.getFechaCreacion());
        dto.setFechaActualizacion(jugador.getFechaActualizacion());

        // Mapear equipo básico
        EquipoBasicoDTO equipoDTO = new EquipoBasicoDTO();
        equipoDTO.setIdEquipo(jugador.getEquipo().getIdEquipo());
        equipoDTO.setNombre(jugador.getEquipo().getNombre());
        equipoDTO.setCategoria(jugador.getEquipo().getCategoria());
        equipoDTO.setEscudoUrl(jugador.getEquipo().getEscudoUrl());
        dto.setEquipo(equipoDTO);

        return dto;
    }

    /**
     * Calcular edad a partir de la fecha de nacimiento
     */
    private Integer calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}