package db;

import model.Article;
import model.Request;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static query.ArticleQuery.*;


public class RepositoryArticleDbImpl {
    private static String url = "jdbc:mysql://muscle-db.ay1.krane.9rum.cc:3306/test";
    private static String userName = "root";
    private static String password = "1234";

    public static void save(Request request) throws SQLException {
        Map<String, String> queryString = request.getQueryString();
        Connection connection = DriverManager.getConnection(url, userName, password);

        PreparedStatement pstmt = connection.prepareStatement(INSERT_QUERY);
        pstmt.setString(1, queryString.get("writer"));
        pstmt.setString(2, queryString.get("title"));
        pstmt.setString(3, queryString.get("contents"));
        int result = pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public static List<Article> findAll() throws SQLException {
        List<Article> articleList = new ArrayList<>();
        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);

        while(resultSet.next()) {
            Article newArticle = Article.builder()
                    .id(resultSet.getLong(1))
                    .writer(resultSet.getString(2))
                    .title(resultSet.getString(3))
                    .contents(resultSet.getString(4))
                    .build();

            articleList.add(newArticle);
        }
        resultSet.close();
        statement.close();
        connection.close();

        return articleList;
    }
}
