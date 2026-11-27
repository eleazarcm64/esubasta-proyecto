package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import modelo.Usuario;
import util.HibernateUtil;
import java.util.List;

public class UsuarioDAO {

    public Usuario guardar(Usuario usuario) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            return usuario;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorId(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorEmail(String email) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }
}