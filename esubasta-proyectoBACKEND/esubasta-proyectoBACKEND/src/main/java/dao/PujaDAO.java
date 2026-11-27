package dao;

import jakarta.persistence.EntityManager;
import modelo.Articulo;
import modelo.Puja;
import util.HibernateUtil;
import java.util.List;

public class PujaDAO {

    public Puja registrarPujaYActualizarArticulo(Puja nuevaPuja, Articulo articulo) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            // Quitar marca de ganadora a pujas anteriores de ese artículo
            em.createQuery("UPDATE Puja p SET p.es_ganadora = false WHERE p.articulo.id_articulo = :idArt")
                    .setParameter("idArt", articulo.getId_articulo())
                    .executeUpdate();

            nuevaPuja.setEs_ganadora(true);
            em.persist(nuevaPuja);
            em.merge(articulo);

            em.getTransaction().commit();
            return nuevaPuja;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Puja> listarPorArticulo(int idArticulo) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Puja p WHERE p.articulo.id_articulo = :idArt ORDER BY p.monto DESC", Puja.class)
                    .setParameter("idArt", idArticulo)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Puja> listarPorComprador(int idComprador) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Puja p WHERE p.comprador.id_usuario = :idComp ORDER BY p.id_puja DESC", Puja.class)
                    .setParameter("idComp", idComprador)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}