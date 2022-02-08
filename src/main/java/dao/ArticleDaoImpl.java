package dao;

import model.Article;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ArticleDaoImpl implements ArticleDao {
    private Connection connection;

    @Override
    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null)
            return;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://test-cafe-ed.ay1.krane.9rum.cc:3306/javawas",
                    "ed3",
                    "Ed1q2w3e4r!"
            );
        } catch (SQLException e) {
            throw new SQLException("failed to get connection");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("failed to load driver");
        }
    }

    @Override
    public void closeConnection() throws SQLException {
        try {
            if (connection != null && connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new SQLException("failed to close connection");
        }
    }

    @Override
    public void save(Article article) throws SQLException {

    }

    @Override
    public Article findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Article> findAll() throws SQLException {
        return null;
    }

    @Override
    public void delete(int id) throws SQLException {

    }
}
