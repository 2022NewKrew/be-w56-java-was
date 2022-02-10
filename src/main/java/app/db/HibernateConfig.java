package app.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateConfig {
    public static EntityManagerFactory emf;

    public static void initEntityMangerFactory(String name) {
        emf = Persistence.createEntityManagerFactory(name);
    }

    public static EntityManager getEntityManger() {
        return emf.createEntityManager();
    }
}
