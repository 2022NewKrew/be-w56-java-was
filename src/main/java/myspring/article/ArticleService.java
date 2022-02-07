package myspring.article;

import myspring.interceptor.LoginInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotations.Autowired;
import webserver.annotations.Service;

import java.util.List;

import static java.lang.Math.min;

@Service
public class ArticleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void createArticle(Article article) {
        articleRepository.save(article);
    }

    public void updateArticle(Article article) {
        articleRepository.update(article);
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    public Article findBySeq(long seq) {
        return articleRepository.findBySeq(seq).orElseThrow();
    }

    public Article findByTitle(String title) {
        return articleRepository.findByTitle(title).orElseThrow();
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> getArticlesByRange(int startIndex, int endIndex) {

        List<Article> articles = articleRepository.findAll();
        if (startIndex >= endIndex || articles.size() < startIndex) throw new IllegalArgumentException("Invalid Page Index");
        return articleRepository.findAll().subList(startIndex, min(articles.size(),endIndex));
    }

    public int getAllArticlesCount() {
        return articleRepository.CountAll();
    }
}
