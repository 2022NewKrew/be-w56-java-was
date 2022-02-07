package bin.jayden.service;

import bin.jayden.model.Article;
import bin.jayden.repository.ArticleRepository;
import bin.jayden.util.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ArticleService {
    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public String getArticleListHtml() throws IOException {
        File file = new File(Constants.RESOURCE_PATH + "/index.html");
        byte[] htmlBytes = Files.readAllBytes(file.toPath());
        String htmlString = new String(htmlBytes);
        List<Article> articleList = repository.getArticleList();
        StringBuilder listHtml = new StringBuilder();
        for (Article article : articleList) {
            listHtml.append("<li>\n" + "<div class=\"wrap\">\n" + "<div class=\"main\">\n" + "<strong class=\"subject\">\n" + "<a>")
                    .append(article.getTitle())
                    .append("</a>\n" + "</strong>\n" + "<div class=\"auth-info\">\n" + "<i class=\"icon-add-comment\"></i>\n" + "<span class=\"time\">")
                    .append(article.getTime()).append("</span>\n" + "<a class=\"author\" >")
                    .append(article.getWriterName())
                    .append("</a>\n" + "</div>\n" + "</div>\n" + "</div>\n" + "</li>");

        }
        htmlString = htmlString.replace("{{list}}", listHtml.toString());

        return htmlString;

    }

    public int createArticle(Article article) {
        return repository.createArticle(article);
    }
}
