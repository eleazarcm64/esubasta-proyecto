import api.ApiServer;
import jakarta.persistence.EntityManager;
import util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        try {
            EntityManager em = HibernateUtil.getEntityManager();
            Long total = em.createQuery("SELECT COUNT(a) FROM Articulo a", Long.class).getSingleResult();
            System.out.println("CONECTADO CON HIBERNATE! Artículos en BD: " + total);
            em.close();

            ApiServer.iniciar(8080);
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}