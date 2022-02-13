package webserver.repository;

import db.JdbcRepository;
import model.user.User;
import model.user.UserId;

import javax.persistence.EntityManager;
import java.util.List;

public class UserRepository {

    private final static UserRepository userRepository = new UserRepository();
    private final EntityManager em;

    private UserRepository() {
        em = JdbcRepository.getEntityManager();
    }

    public static UserRepository getInstance() {
        return userRepository;
    }

    public User findById(String userId) {
        em.getTransaction().begin();
        User user = em.find(User.class, new UserId(userId));
        em.getTransaction().commit();
        //em.close();
        return user;
    }

    public List<User> findAll() {
        em.getTransaction().begin();
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();
        em.getTransaction().commit();
        //em.close();
        return users;
    }

    public User save(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        //em.close();
        return user;
    }
}
