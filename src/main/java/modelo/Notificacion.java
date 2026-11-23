package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "notificaciones")
public class Notificacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_notificacion;
    private String mensaje;
    private boolean leida;
    @ManyToOne @JoinColumn(name = "id_usuario") private Usuario usuario;
    public Notificacion() {}
}