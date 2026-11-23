package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "favoritos")
public class Favorito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_favorito;
    @ManyToOne @JoinColumn(name = "id_usuario") private Usuario usuario;
    @ManyToOne @JoinColumn(name = "id_articulo") private Articulo articulo;
    public Favorito() {}
}