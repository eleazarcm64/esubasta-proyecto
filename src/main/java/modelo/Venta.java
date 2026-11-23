package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "ventas")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_venta;
    private double monto_final;
    private String estado_pago;
    @ManyToOne @JoinColumn(name = "id_articulo") private Articulo articulo;
    @ManyToOne @JoinColumn(name = "id_ganador") private Usuario ganador;
    public Venta() {}
}