package modelo;
import jakarta.persistence.*;
@Entity @Table(name = "pagos")
public class Pago {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_pago;
    private String metodo;
    private double monto;
    private String estado;
    @ManyToOne @JoinColumn(name = "id_venta") private Venta venta;
    public Pago() {}
}