package service;

import db.RepositoryArticleDbImpl;
import model.Article;
import model.Request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

public class ArticleService {

    private final RepositoryArticleDbImpl repositoryArticleDb;

    public ArticleService() {
        this.repositoryArticleDb = new RepositoryArticleDbImpl();
    }

    public void save(Request request) throws SQLException {
        repositoryArticleDb.save(request);
    }

    public byte[] articleListToByte() throws SQLException, IOException {
        byte[] htmlBytes = Files.readAllBytes(new File("./webapp" + "/qna/list.html").toPath());
        String htmlString = new String(htmlBytes);

        List<Article> articleList = repositoryArticleDb.findAll();
        StringBuilder sb = new StringBuilder();

        for (Article article : articleList) {
            sb.append("<tr>");
            sb.append("<td> " + article.getWriter() + " </td>");
            sb.append("<td> " + article.getTitle() + " </td>");
            sb.append("<td> " + article.getContents() + " </td>");
            sb.append("</tr>");
        }

        htmlString = htmlString.replace("{{articleList}}", sb.toString());
        return htmlString.getBytes();
    }
}
