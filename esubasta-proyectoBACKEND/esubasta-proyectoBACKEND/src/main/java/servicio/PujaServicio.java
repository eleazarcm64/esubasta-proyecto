package servicio;

import dao.ArticuloDAO;
import dao.PujaDAO;
import dao.UsuarioDAO;
import modelo.Articulo;
import modelo.Puja;
import modelo.Usuario;
import java.util.Date;
import java.util.List;

public class PujaServicio {
    private final PujaDAO pujaDAO = new PujaDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Puja realizarPuja(int idArticulo, int idComprador, double monto) {
        Articulo articulo = articuloDAO.buscarPorId(idArticulo);
        if (articulo == null) {
            throw new IllegalArgumentException("Artículo/Subasta no encontrado.");
        }
        Usuario comprador = usuarioDAO.buscarPorId(idComprador);
        if (comprador == null) {
            throw new IllegalArgumentException("Comprador no encontrado.");
        }
        if ("vendedor".equalsIgnoreCase(comprador.getTipo())) {
            throw new IllegalArgumentException("Un usuario de tipo vendedor no puede realizar pujas.");
        }
        if (articulo.getVendedor().getId_usuario() == comprador.getId_usuario()) {
            throw new IllegalArgumentException("No puedes pujar por tu propio artículo.");
        }
        if (articulo.getFecha_fin() != null && new Date().after(articulo.getFecha_fin())) {
            articulo.setEstado("finalizada");
            articuloDAO.guardar(articulo);
            throw new IllegalArgumentException("La subasta ya finalizó por tiempo.");
        }
        if (!"activa".equalsIgnoreCase(articulo.getEstado())) {
            throw new IllegalArgumentException("La subasta no está activa.");
        }
        if (monto <= articulo.getPrecio_actual()) {
            throw new IllegalArgumentException(
                    "La puja debe ser estrictamente mayor al precio actual ($" + articulo.getPrecio_actual() + ")."
            );
        }

        Puja puja = new Puja();
        puja.setArticulo(articulo);
        puja.setComprador(comprador);
        puja.setMonto(monto);

        articulo.setPrecio_actual(monto);
        return pujaDAO.registrarPujaYActualizarArticulo(puja, articulo);
    }

    public List<Puja> historialPorArticulo(int idArticulo) {
        return pujaDAO.listarPorArticulo(idArticulo);
    }

    public List<Puja> historialPorComprador(int idComprador) {
        return pujaDAO.listarPorComprador(idComprador);
    }
}