package repository;

import com.mysql.cj.result.LocalDateValueFactory;
import db.JdbcConnection;
import model.Memo;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class MemoRepository implements Repository<Memo>{

    private static final Logger log = LoggerFactory.getLogger(MemoRepository.class);

    @Override
    public Memo create(Memo memo) {
        String sql = "INSERT INTO MEMO (CONTENT, WRITER, CREATED_TIME) VALUES (?, ?, ?)";
        try {
            Connection conn = JdbcConnection.getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, memo.getContent());
            psmt.setString(2, memo.getWriter().getUserId());
            psmt.setTimestamp(3, Timestamp.valueOf(memo.getCreatedTime()));
            psmt.execute();

            JdbcConnection.close();
            psmt.close();
        } catch (SQLException e) {
            log.error("could not create memo : {}", e.getMessage());
            e.printStackTrace();
        }
        return memo;
    }

    @Override
    public Collection<Memo> findAll() {
        String sql = "SELECT CONTENT, WRITER, CREATED_TIME, " +
                "USER_ID, PASSWORD, NAME, EMAIL " +
                "FROM MEMO JOIN USER_WAS ON WRITER = USER_ID";
        try {
            Connection conn = JdbcConnection.getConnection();
            PreparedStatement psmt = conn.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();
            List<Memo> result = new ArrayList<>();
            if (rs.next()){
                Memo memo = memoMaker(rs);
                result.add(memo);
            }
            JdbcConnection.close();
            log.info("connection closed!");
            psmt.close();
            return result;

        } catch (SQLException e) {
            log.error("could not find memoList : {}", e.getMessage());
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Memo> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Memo update(Memo entity) {
        return null;
    }

    @Override
    public void delete(Memo entity) {

    }

    private Memo memoMaker(ResultSet rs) throws SQLException {
        return new Memo(
                rs.getString("CONTENT"),
                rs.getTimestamp("CREATED_TIME").toLocalDateTime(),
                new User(
                    rs.getString("USER_ID"),
                    rs.getString("PASSWORD"),
                    rs.getString("NAME"),
                    rs.getString("EMAIL")
                )
        );
    }
}
