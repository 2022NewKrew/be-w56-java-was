package webserver.jdbc;

import webserver.configures.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int update(String sql, Object... args) {
        try (Connection conn = dataSource.getConnection()){
            PreparedStatement pps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pps.setObject(i+1, args[i]);
            }
            return pps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection conn = dataSource.getConnection()){
            PreparedStatement pps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pps.setObject(i+1, args[i]);
            }
            ResultSet rs = pps.executeQuery();
            List<T> ret = new ArrayList<>();
            while (rs.next()) {
                ret.add(rowMapper.mapRow(rs, rs.getRow()));
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> int queryForObject(String sql, Class<T> obj) {
        try (Connection conn = dataSource.getConnection()){
            PreparedStatement pps = conn.prepareStatement(sql);
            ResultSet rs = pps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
