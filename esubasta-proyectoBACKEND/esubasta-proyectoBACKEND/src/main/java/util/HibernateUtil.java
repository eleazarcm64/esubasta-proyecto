package util;
import jakarta.persistence.*;
public class HibernateUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("esubastaPU");
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}