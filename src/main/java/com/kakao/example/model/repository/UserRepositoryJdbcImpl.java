package com.kakao.example.model.repository;

import com.kakao.example.model.domain.User;
import com.kakao.example.util.DbUtil;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static framework.util.annotation.Component.ComponentType.REPOSITORY;

@Component(type = REPOSITORY)
public class UserRepositoryJdbcImpl implements UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryJdbcImpl.class);

    private final DataSource dataSource;

    @Autowired
    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();

            String sql = "INSERT INTO USERS (USER_ID, PASSWORD, NAME, EMAIL) \n"
                    + "VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        } finally {
            DbUtil.close(pstmt, conn);
        }
    }

    @Override
    public Optional<User> findUserById(String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = dataSource.getConnection();

            String sql = "SELECT USER_ID, PASSWORD, NAME, EMAIL \n"
                    + "FROM USERS \n"
                    + "WHERE USER_ID = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = User.builder()
                        .userId(rs.getString(1))
                        .password(rs.getString(2))
                        .name(rs.getString(3))
                        .email(rs.getString(4)).build();
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        } finally {
            DbUtil.close(rs, pstmt, conn);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findUserByLoginInfo(String userId, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = dataSource.getConnection();

            String sql = "SELECT USER_ID, PASSWORD, NAME, EMAIL \n"
                    + "FROM USERS \n"
                    + "WHERE USER_ID = ? AND PASSWORD = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = User.builder()
                        .userId(rs.getString(1))
                        .password(rs.getString(2))
                        .name(rs.getString(3))
                        .email(rs.getString(4)).build();
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        } finally {
            DbUtil.close(rs, pstmt, conn);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();

        try {
            conn = dataSource.getConnection();

            String sql = "SELECT USER_ID, PASSWORD, NAME, EMAIL \n"
                    + "FROM USERS";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                userList.add(User.builder()
                        .userId(rs.getString(1))
                        .password(rs.getString(2))
                        .name(rs.getString(3))
                        .email(rs.getString(4)).build());
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        } finally {
            DbUtil.close(rs, pstmt, conn);
        }

        return userList;
    }
}
