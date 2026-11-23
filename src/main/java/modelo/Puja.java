package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "pujas")
public class Puja {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_puja;
    private double monto;
    private boolean es_ganadora;
    @ManyToOne @JoinColumn(name = "id_articulo") private Articulo articulo;
    @ManyToOne @JoinColumn(name = "id_comprador") private Usuario comprador;
    public Puja() {}
    public int getId_puja() { return id_puja; }
    public void setId_puja(int id_puja) { this.id_puja = id_puja; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }
    public Usuario getComprador() { return comprador; }
    public void setComprador(Usuario comprador) { this.comprador = comprador; }
}