package repository.mapper;

import model.Board;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardRowMapper implements MyRowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Board board = new Board();
        board.setTitle(rs.getString("title"));
        board.setWriter(rs.getString("writer"));
        board.setContents(rs.getString("contents"));
        return board;
    }
}
