package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "imagenes_articulo")
public class ImagenArticulo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_imagen;
    private String url_imagen;
    @ManyToOne @JoinColumn(name = "id_articulo") private Articulo articulo;
    public ImagenArticulo() {}
    public int getId_imagen() { return id_imagen; }
    public String getUrl_imagen() { return url_imagen; }
    public void setUrl_imagen(String url_imagen) { this.url_imagen = url_imagen; }
    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }
}