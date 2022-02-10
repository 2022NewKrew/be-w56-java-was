package db.article;

import db.DBConnection;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleRepositoryImpl implements ArticleRepository{
    private static final Logger log = LoggerFactory.getLogger(ArticleRepositoryImpl.class);
    private static final ArticleRepositoryImpl articleRepositoryImpl = new ArticleRepositoryImpl();
    private final DBConnection dbConnection = DBConnection.getInstance();

    private ArticleRepositoryImpl(){}

    public static ArticleRepositoryImpl getInstance(){
        return articleRepositoryImpl;
    }

    @Override
    public void addArticle(Article article) {
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = dbConnection.getConnection();
            String sql = "INSERT INTO articles (writer, title, contents) VALUES (?,?,?)";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, article.getWriter());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContents());

            pstmt.executeUpdate();
            log.info("[ArticleRepositoryImpl]: add Article Complete");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.close(pstmt, con);
        }
    }

    @Override
    public Optional<Article> findArticleByUserId(String userId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Article article = null;

        try{
            con = dbConnection.getConnection();
            String sql = "SELECT writer, title, contents FROM articles WHERE writer = ?";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            if(rs.next()){
                article = new Article(
                        rs.getString(1), rs.getString(2), rs.getString(3)
                );
            }

            log.info("[ArticleRepositoryImpl]: search Article by UserId Complete");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.close(rs, pstmt, con);
        }
        return Optional.ofNullable(article);
    }

    @Override
    public List<Article> findAll() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();

        try {
            con = dbConnection.getConnection();
            String sql = "SELECT writer, title, contents FROM articles";
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while(rs.next()){
                articles.add(
                        new Article(
                                rs.getString(1), rs.getString(2), rs.getString(3))
                );
            }

            log.info("[ArticleRepositoryImpl]: search All Article Complete");
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            dbConnection.close(rs, pstmt, con);
        }

        return articles;
    }
}
