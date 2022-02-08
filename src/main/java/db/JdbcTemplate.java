package db;

import com.mysql.cj.xdevapi.PreparableStatement;
import lombok.extern.slf4j.Slf4j;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcTemplate {
    private static final JdbcTemplate INSTANCE = new JdbcTemplate();

    public static JdbcTemplate getInstance() {
        return INSTANCE;
    }

    private JdbcTemplate() {
    }

    private final String url = "jdbc:mysql://kina-sandbox-db.ay1.krane.9rum.cc:3306/kina_was";
    private final String username = "kina";
    private final String password = "kusakina";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private PreparedStatement createPreparedStatement(Connection con, String sql, Object... args) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        for (int index = 0; index < args.length; index++) {
            Object obj = args[index];
            if (obj.getClass() == String.class) {
                ps.setString(index + 1, (String) obj);
            } else if (obj.getClass() == Integer.class) {
                ps.setInt(index + 1, (Integer) obj);
            } else {
                throw new SQLException();
            }
        }
        return ps;
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        log.debug("Executing SQL update [" + sql + "]");
        List<T> result = new ArrayList<>();
        try (
                Connection con = getConnection();
                PreparedStatement ps = createPreparedStatement(con, sql, args);
                ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs, rs.getRow()));
            }
            rs.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        log.debug("Executing SQL update [" + sql + "]");
        try (
                Connection con = getConnection();
                PreparedStatement ps = createPreparedStatement(con, sql, args);
                ResultSet rs = ps.executeQuery();
        ) {
            if (rs.next()) {
                return rowMapper.mapRow(rs, rs.getRow());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(final String sql, Object... args) {
        log.debug("Executing SQL update [" + sql + "]");
        try (
                Connection con = getConnection();
                PreparedStatement ps = createPreparedStatement(con, sql, args);
        ) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
