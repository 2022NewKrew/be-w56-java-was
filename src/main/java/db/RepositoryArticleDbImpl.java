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

    private final Connection connection;

    public RepositoryArticleDbImpl() {
        try {
            this.connection = DbUtils.getDbConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("connection fail");
        }
    }

    public void save(Request request) throws SQLException {
        Map<String, String> queryString = request.getQueryString();

        PreparedStatement pstmt = connection.prepareStatement(INSERT_QUERY);
        pstmt.setString(1, queryString.get("writer"));
        pstmt.setString(2, queryString.get("title"));
        pstmt.setString(3, queryString.get("contents"));
        pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public List<Article> findAll() throws SQLException {
        List<Article> articleList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY);

        while (resultSet.next()) {
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
