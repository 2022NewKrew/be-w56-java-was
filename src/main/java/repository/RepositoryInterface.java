package repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RepositoryInterface<T> {
    void join(T target) throws SQLException;

    void update(T target) throws SQLException;

    Optional<T> findOne(String index) throws SQLException;

    List<T> findAll() throws SQLException;

}
