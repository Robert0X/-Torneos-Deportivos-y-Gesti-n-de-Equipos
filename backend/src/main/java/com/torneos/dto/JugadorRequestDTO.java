package torneos.dto;

import com.torneos.entity.Jugador;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class JugadorRequestDTO {

    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String nombre;

    @NotNull(message = "Fecha de nacimiento requerida")
    @Past(message = "Fecha debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Posición requerida")
    private Jugador.Posicion posicion;

    @NotNull(message = "Número de camiseta requerido")
    @Min(value = 1, message = "Mínimo 1")
    @Max(value = 99, message = "Máximo 99")
    private Integer numeroCamiseta;

    @NotNull(message = "ID de equipo requerido")
    private Long idEquipo;

    @Size(max = 100, message = "Máximo 100 caracteres")
    private String contactoEmergencia;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public Jugador.Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Jugador.Posicion posicion) {
        this.posicion = posicion;
    }

    public Integer getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(Integer numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getContactoEmergencia() {
        return contactoEmergencia;
    }

    public void setContactoEmergencia(String contactoEmergencia) {
        this.contactoEmergencia = contactoEmergencia;
    }

    // Getters y Setters
}