package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "valoraciones")
public class Valoracion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_valoracion;
    private int puntaje;
    private String comentario;
    @ManyToOne @JoinColumn(name = "id_venta") private Venta venta;
    @ManyToOne @JoinColumn(name = "id_usuario_califica") private Usuario califica;
    @ManyToOne @JoinColumn(name = "id_usuario_calificado") private Usuario calificado;
    public Valoracion() {}
}