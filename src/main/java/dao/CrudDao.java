package dao;

import java.util.List;

public interface CrudDao<T, K> {

    T find(K id);
    List<T> find();

    K save(T entity);

    void update(T entity);
    void delete(K id);
}
