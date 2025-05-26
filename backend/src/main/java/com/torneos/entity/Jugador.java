package com.torneos.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "jugadores", uniqueConstraints = @UniqueConstraint(columnNames = { "id_equipo", "numero_camiseta" }))
@EntityListeners(AuditingEntityListener.class)
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jugador")
    private Long idJugador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", nullable = false)
    @JsonBackReference
    private Equipo equipo;

    @NotBlank(message = "El nombre del jugador es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "posicion", nullable = false, length = 30)
    private Posicion posicion;

    @NotNull(message = "El número de camiseta es obligatorio")
    @Positive(message = "El número de camiseta debe ser positivo")
    @Column(name = "numero_camiseta", nullable = false)
    private Integer numeroCamiseta;

    @Size(max = 100, message = "El contacto de emergencia no puede exceder 100 caracteres")
    @Column(name = "contacto_emergencia", length = 100)
    private String contactoEmergencia;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación con estadísticas
    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstadisticaJugador> estadisticas = new ArrayList<>();

    // Constructores
    public Jugador() {
    }

    public Jugador(String nombre, LocalDate fechaNacimiento, Posicion posicion, Integer numeroCamiseta) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.activo = true;
    }

    public Jugador(String nombre, LocalDate fechaNacimiento, Posicion posicion,
            Integer numeroCamiseta, Equipo equipo) {
        this(nombre, fechaNacimiento, posicion, numeroCamiseta);
        this.equipo = equipo;
    }

    // Getters y Setters
    public Long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Long idJugador) {
        this.idJugador = idJugador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public Integer getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(Integer numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<EstadisticaJugador> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(List<EstadisticaJugador> estadisticas) {
        this.estadisticas = estadisticas;
    }

    // Métodos utilitarios
    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public boolean esPortero() {
        return Posicion.PORTERO.equals(this.posicion);
    }

    public boolean esDefensa() {
        return Posicion.DEFENSA.equals(this.posicion);
    }

    public boolean esMedio() {
        return Posicion.MEDIO.equals(this.posicion);
    }

    public boolean esDelantero() {
        return Posicion.DELANTERO.equals(this.posicion);
    }

    public String getNombreCompleto() {
        return String.format("%s (#%d - %s)", nombre, numeroCamiseta, posicion.getDescripcion());
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(idJugador, jugador.idJugador) &&
                Objects.equals(nombre, jugador.nombre) &&
                Objects.equals(numeroCamiseta, jugador.numeroCamiseta) &&
                Objects.equals(equipo, jugador.equipo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJugador, nombre, numeroCamiseta);
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "idJugador=" + idJugador +
                ", nombre='" + nombre + '\'' +
                ", posicion=" + posicion +
                ", numeroCamiseta=" + numeroCamiseta +
                ", equipo=" + (equipo != null ? equipo.getNombre() : "Sin equipo") +
                ", activo=" + activo +
                '}';
    }

    // Enum para posiciones
    public enum Posicion {
        PORTERO("Portero"),
        DEFENSA("Defensa"),
        MEDIO("Medio"),
        DELANTERO("Delantero");

        private final String descripcion;

        Posicion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }
}