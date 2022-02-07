package webserver.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {

    void delete(T entity);

    Iterable<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    T update(T entity);

}
