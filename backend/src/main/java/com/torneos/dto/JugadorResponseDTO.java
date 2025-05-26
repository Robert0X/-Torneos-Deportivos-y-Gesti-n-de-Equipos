package com.torneos.dto;

import com.torneos.entity.Jugador;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class JugadorResponseDTO {

    private Long idJugador;
    private Long idUsuario;
    private String nombre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;
    private Integer edad;
    private Jugador.Posicion posicion;
    private Integer numeroCamiseta;
    private String contactoEmergencia;
    private Boolean activo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaActualizacion;

    private EquipoBasicoDTO equipo;

    // Getters y Setters
}