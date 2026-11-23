package modelo;
import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "articulos")
public class Articulo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_articulo;
    private String titulo;
    private String descripcion;
    private double precio_inicial;
    private double precio_actual;
    private Double precio_reserva;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha_fin;
    private String estado;
    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    public Articulo() {}
    // getters y setters basicos
    public int getId_articulo() { return id_articulo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public double getPrecio_actual() { return precio_actual; }
    public void setPrecio_actual(double precio_actual) { this.precio_actual = precio_actual; }
    public Usuario getVendedor() { return vendedor; }
    public void setVendedor(Usuario vendedor) { this.vendedor = vendedor; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}