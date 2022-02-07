package webserver.repository;

import db.ConnectionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudRepository<User, Long> {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final ConnectionManager connectionManager = ConnectionManager.getInstance();

    @Override
    public void delete(User entity) {
        String sql = "delete from users where id = ?";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, entity.getId());
            pstmt.execute();

        } catch (SQLException e) {
            log.error("could not delete entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
    }

    @Override
    public Iterable<User> findAll() {
        String sql = "select u.id, u.user_id, u.name, u.email, u.password from users u";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            List<User> result = new ArrayList<>();
            while (rs.next()) {
                User user = mapUser(rs);
                result.add(user);
            }
            return result;

        } catch (SQLException e) {
            log.error("could not find entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select u.id, u.user_id, u.name, u.email, u.password from users u where u.id = ?";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = mapUser(rs);
                return Optional.of(user);
            }

        } catch (SQLException e) {
            log.error("could not find entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
        return Optional.empty();
    }

    public Optional<User> findByUserId(String userId) {
        String sql = "select u.id, u.user_id, u.name, u.email, u.password from users u where u.user_id = ?";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = mapUser(rs);
                return Optional.of(user);
            }

        } catch (SQLException e) {
            log.error("could not find entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public User save(User entity) {
        String sql = "insert into users (user_id, name, email, password) values ( ?, ?, ?, ? )";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, entity.getUserId());
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getEmail());
            pstmt.setString(4, entity.getPassword());
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                long id = rs.getLong(1);
                entity.setId(id);
            }

        } catch (SQLException e) {
            log.error("could not save entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
        return entity;
    }

    @Override
    public User update(User entity) {
        String sql = "update users set name = ?, email = ?, password = ? where id = ?";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getEmail());
            pstmt.setString(3, entity.getPassword());
            pstmt.setLong(4, entity.getId());
            pstmt.execute();

        } catch (SQLException e) {
            log.error("could not update entity : {}", e.getMessage());
            e.printStackTrace();
        } finally {
            connectionManager.retrieveConnection(conn);
        }
        return entity;
    }

    private static User mapUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String userId = rs.getString("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        return new User(id, userId, name, email, password);
    }
}
