package db;

import db.entity.H2Entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class H2Repository<T extends H2Entity, ID> implements H2RepositoryIfs<T, ID> {

    private final Class<T> classType;
    private static final String SELECT_QUERY = "select entity from %s entity";
    private static final String SELECT_ORDER_BY_QUERY = "select entity from %s entity order by entity.%s %s";

    public H2Repository(Class<T> classType) {
        this.classType = classType;
    }

    @Override
    public void save(T entity) {
        EntityManager em = H2EntityManagerFactory.emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(ID id) {
        EntityManager em = H2EntityManagerFactory.emf.createEntityManager();
        T find = em.find(classType, id);
        em.close();
        return find;
    }

    @Override
    public List<T> findAll() {
        EntityManager em = H2EntityManagerFactory.emf.createEntityManager();
        List<T> findAll = em.createQuery(String.format(SELECT_QUERY, classType.getName()), classType)
                .getResultList();
        em.close();
        return findAll;
    }

    @Override
    public List<T> findAll(Sort sort) {
        EntityManager em = H2EntityManagerFactory.emf.createEntityManager();
        Sort.Direction direction = sort.getDirection();
        String property = sort.getProperty();
        List<T> findAll = em.createQuery(String.format(SELECT_ORDER_BY_QUERY, classType.getName(), property, direction), classType)
                .getResultList();
        em.close();
        return findAll;
    }
}
