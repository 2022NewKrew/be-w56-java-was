package db;

import java.util.List;

public interface H2RepositoryIfs<T, ID> {

    void save(T entity);

    T findById(ID id);

    List<T> findAll();
}
