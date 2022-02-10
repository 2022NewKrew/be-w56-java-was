package repository;

import model.User;

import java.util.Collection;
import java.util.Optional;

public interface Repository <T>{
    T create(T entity);
    Collection<T> findAll();
    Optional<T> findById(String id);
    T update(T entity);
    void delete(T entity);
}
