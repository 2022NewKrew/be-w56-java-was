package bin.jayden.service;

import bin.jayden.model.Article;
import bin.jayden.repository.ArticleRepository;
import bin.jayden.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger("USERLOG");
    private final ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public String getArticleListHtml() throws IOException {
        byte[] htmlBytes = getClass().getResourceAsStream(Constants.TEMPLATE_PATH + "/index.html").readAllBytes();
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
        log.info("create Article (userId : {}, title : {})", article.getWriterId(), article.getTitle());
        return repository.createArticle(article);
    }
}
