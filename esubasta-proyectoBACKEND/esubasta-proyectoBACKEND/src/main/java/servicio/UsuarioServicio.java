package servicio;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.util.List;

public class UsuarioServicio {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario registrar(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
        if (usuario.getTipo() == null || usuario.getTipo().isBlank()) {
            usuario.setTipo("comprador");
        }
        return usuarioDAO.guardar(usuario);
    }

    public Usuario login(String email, String password) {
        Usuario u = usuarioDAO.buscarPorEmail(email);
        if (u == null || !u.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales incorrectas.");
        }
        return u;
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }
}