package dao;

import jakarta.persistence.EntityManager;
import modelo.Articulo;
import modelo.Categoria;
import util.HibernateUtil;
import java.util.List;

public class ArticuloDAO {

    public Articulo guardar(Articulo articulo) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (articulo.getId_articulo() == 0) {
                em.persist(articulo);
            } else {
                articulo = em.merge(articulo);
            }
            em.getTransaction().commit();
            return articulo;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Articulo buscarPorId(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Articulo.class, id);
        } finally {
            em.close();
        }
    }

    public Categoria buscarCategoriaPorId(int id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public List<Articulo> listarActivos() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Articulo a WHERE a.estado = 'activa'", Articulo.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Articulo> buscarPorTitulo(String texto) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Articulo a WHERE LOWER(a.titulo) LIKE :texto", Articulo.class)
                    .setParameter("texto", "%" + texto.toLowerCase() + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Categoria> listarCategorias() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categoria c", Categoria.class).getResultList();
        } finally {
            em.close();
        }
    }
}