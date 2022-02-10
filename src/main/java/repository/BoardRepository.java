package repository;

import model.Board;

import java.sql.SQLException;
import java.util.List;

public interface BoardRepository {

    Long save(Board board) throws SQLException;

    List<Board> findAll() throws SQLException;
}
