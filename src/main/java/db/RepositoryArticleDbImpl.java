package db;

import model.Article;
import model.Request;
import util.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static query.ArticleQuery.FIND_ALL_QUERY;
import static query.ArticleQuery.INSERT_QUERY;


public class RepositoryArticleDbImpl {

    public static void save(Request request) throws SQLException {
        Map<String, String> queryString = request.getQueryString();
        Connection connection = DbUtils.getDbConnection();

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
        Connection connection = DbUtils.getDbConnection();
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
