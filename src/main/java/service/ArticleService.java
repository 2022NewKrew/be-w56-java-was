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
    public static void save(Request request) throws SQLException {
        RepositoryArticleDbImpl.save(request);
    }

    public static byte[] articleListToByte() throws SQLException, IOException {
        byte[] htmlBytes = Files.readAllBytes(new File("./webapp" + "/qna/list.html").toPath());
        String htmlString = new String(htmlBytes);

        List<Article> articleList = RepositoryArticleDbImpl.findAll();
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
