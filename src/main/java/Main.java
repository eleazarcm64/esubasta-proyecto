import util.HibernateUtil;
import jakarta.persistence.EntityManager;
public class Main {
    public static void main(String[] args) {
        EntityManager em = HibernateUtil.getEntityManager();
        System.out.println("CONECTADO CON HIBERNATE!");
        Long total = em.createQuery("SELECT COUNT(a) FROM Articulo a", Long.class).getSingleResult();
        System.out.println("Total articulos: " + total);
        em.close();
    }
}