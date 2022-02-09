package controller;

import dao.ArticleDBConstants;
import dao.ArticleDao;
import dao.ArticleDaoImpl;
import dao.connection.MysqlConnectionMaker;
import http.HttpStatus;
import http.request.HttpRequest;
import http.request.HttpRequestBody;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HtmlUtils;
import util.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ArticleController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    private static ArticleController INSTANCE;
    private final ArticleDao articleDao;
    private final Map<String, Function<HttpRequest, HttpResponse>> methodMap = new HashMap<>();

    {
        methodMap.put("POST /articles/create", this::create);
    }

    private ArticleController(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public static synchronized ArticleController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ArticleController(new ArticleDaoImpl(new MysqlConnectionMaker()));
        return INSTANCE;
    }

    @Override
    public HttpResponse processDynamic(HttpRequest request) {
        final HttpRequestLine requestLine = request.line();

        if (methodMap.containsKey(requestLine.methodAndPath())) {
            log.debug("{} called", requestLine.methodAndPath());
            return methodMap.get(requestLine.methodAndPath()).apply(request);
        } else {
            log.debug("{} {} redirect to error page", requestLine.method(), requestLine.path());
            return errorPage();
        }
    }

    private HttpResponse create(HttpRequest request) {
        HttpRequestBody requestBody = request.body();
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(requestBody.content());

        System.out.println(new Timestamp(System.currentTimeMillis()));
        Article newArticle = new Article(
                queryString.get(ArticleDBConstants.COLUMN_TITLE),
                LocalDateTime.now().toString(),
                queryString.get(ArticleDBConstants.COLUMN_WRITER));

        try {
            articleDao.save(newArticle);
        } catch (SQLException e) {
            log.error("POST /articles/create failed. SQL error_code = {}", e.getErrorCode());
        }

        return redirect("/index.html");
    }
}
