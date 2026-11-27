package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "preguntas")
public class Pregunta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pregunta;
    private String pregunta;
    private String respuesta;
    @ManyToOne @JoinColumn(name = "id_articulo") private Articulo articulo;
    @ManyToOne @JoinColumn(name = "id_usuario") private Usuario usuario;
    public Pregunta() {}
}