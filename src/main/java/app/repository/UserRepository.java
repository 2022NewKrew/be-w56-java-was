package app.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import app.db.HibernateConfig;
import app.domain.User;

public class UserRepository {
    private static UserRepository instance = new UserRepository();

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return instance;
    }

    public void saveUser(User user) {
        EntityManager em = HibernateConfig.getEntityManger();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(app.domain.User.builder()
                                      .userId(user.getUserId())
                                      .password(user.getPassword())
                                      .name(user.getName())
                                      .email(user.getEmail())
                                      .build());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
    }

    public User getUser(String userId) {
        EntityManager em = HibernateConfig.getEntityManger();

        List<User> userList = em.createQuery("SELECT u from User AS u WHERE u.userId = :id", User.class)
                                .setParameter("id", userId)
                                .getResultList();

        em.close();
        return userList.stream().findAny().orElse(new User());
    }

    public List<User> getUserList() {
        EntityManager em = HibernateConfig.getEntityManger();

        List<User> userList = em.createQuery("SELECT u from User AS u", User.class)
                                .getResultList();

        em.close();
        return userList;
    }


}
