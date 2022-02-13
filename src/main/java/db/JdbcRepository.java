package db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JdbcRepository {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("java-was-db");

    private JdbcRepository() {

    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
