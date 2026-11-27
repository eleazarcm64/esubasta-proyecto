package servicio;

import dao.ArticuloDAO;
import dao.UsuarioDAO;
import modelo.Articulo;
import modelo.Categoria;
import modelo.Usuario;
import java.util.Date;
import java.util.List;

public class ArticuloServicio {
    private final ArticuloDAO articuloDAO = new ArticuloDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Articulo publicar(Articulo articulo, int idVendedor, int idCategoria) {
        Usuario vendedor = usuarioDAO.buscarPorId(idVendedor);
        if (vendedor == null) {
            throw new IllegalArgumentException("Vendedor no encontrado.");
        }
        if ("comprador".equalsIgnoreCase(vendedor.getTipo())) {
            throw new IllegalArgumentException("Solo usuarios con rol vendedor o ambos pueden publicar.");
        }
        Categoria categoria = articuloDAO.buscarCategoriaPorId(idCategoria);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
        if (articulo.getPrecio_inicial() <= 0) {
            throw new IllegalArgumentException("El precio inicial debe ser mayor a 0.");
        }

        articulo.setVendedor(vendedor);
        articulo.setCategoria(categoria);
        articulo.setPrecio_actual(articulo.getPrecio_inicial());
        articulo.setEstado("activa");
        if (articulo.getFecha_fin() == null) {
            // Por defecto 3 días desde hoy si no se envía fecha_fin
            articulo.setFecha_fin(new Date(System.currentTimeMillis() + 3L * 24 * 60 * 60 * 1000));
        }
        return articuloDAO.guardar(articulo);
    }

    public List<Articulo> listarSubastasActivas() {
        List<Articulo> activos = articuloDAO.listarActivos();
        Date ahora = new Date();
        // Cerrar automáticamente las subastas cuya fecha_fin ya pasó
        for (Articulo a : activos) {
            if (a.getFecha_fin() != null && ahora.after(a.getFecha_fin())) {
                a.setEstado("finalizada");
                articuloDAO.guardar(a);
            }
        }
        return articuloDAO.listarActivos();
    }

    public Articulo buscarPorId(int id) {
        Articulo a = articuloDAO.buscarPorId(id);
        if (a == null) throw new IllegalArgumentException("Artículo no encontrado.");
        return a;
    }

    public List<Articulo> buscarPorTitulo(String titulo) {
        return articuloDAO.buscarPorTitulo(titulo);
    }

    public List<Categoria> listarCategorias() {
        return articuloDAO.listarCategorias();
    }
}