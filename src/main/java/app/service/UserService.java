package app.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import app.db.HibernateConfig;
import app.domain.User;

public class UserService {
    private static UserService instance = new UserService();

    private UserService() {
    }

    public static UserService getInstance() {
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

        return userList.stream().findAny().orElse(new User());
    }

    public String getUserListHtml() {
        EntityManager em = HibernateConfig.getEntityManger();

        List<User> userList = em.createQuery("SELECT u from User AS u", User.class)
                                .getResultList();

        StringBuilder sb = new StringBuilder();
        AtomicInteger index = new AtomicInteger(1);

        userList.stream()
                .map(user -> "<tr><th scope='row'>" + index.getAndIncrement()
                        + "</th><td>" + user.getUserId() + "</td>"
                        + "<td>" + user.getName() + "</td>"
                        + "<td>" + user.getEmail() + "</td>"
                        + "<td><a href='#' class='btn btn-success' role='button'>수정</a></td></tr>")
                .forEach(str -> sb.append(str + "\n"));

        return sb.toString();
    }
}
