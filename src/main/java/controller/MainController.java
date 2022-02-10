package controller;

import dao.ArticleDao;
import dao.ArticleDaoImpl;
import dao.connection.MysqlConnectionMaker;
import http.HttpStatus;
import http.request.HttpRequest;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import http.response.HttpResponseBody;
import http.response.HttpResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HtmlUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MainController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private static MainController INSTANCE;
    private final ArticleDao articleDao;
    private final Map<String, Function<HttpRequest, HttpResponse>> methodMap = new HashMap<>();

    {
        methodMap.put("GET /", this::index);
    }

    private MainController(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public static synchronized MainController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MainController(new ArticleDaoImpl(new MysqlConnectionMaker()));
        return INSTANCE;
    }

    @Override
    public HttpResponse processDynamic(HttpRequest request) {
        final HttpRequestLine requestLine = request.line();

        log.debug("{} {}", requestLine.method(), requestLine.path());
        if (methodMap.containsKey(requestLine.methodAndPath())) {
            log.debug("{} called", requestLine.methodAndPath());
            return methodMap.get(requestLine.methodAndPath()).apply(request);
        } else {
            log.debug("{} {} redirect to error page", requestLine.method(), requestLine.path());
            return errorPage();
        }
    }

    private HttpResponse index(HttpRequest request) {
        if ("true".equals(request.header().getCookie("logined"))) {
            log.debug("logined user"); // TODO
        }

        File file = new File(HttpResponseBody.STATIC_ROOT + "/index.html");

        try {
            StringBuilder sb = HtmlUtils.renderTemplate(file, articleDao.findAll());

            HttpResponseBody responseBody = HttpResponseBody.createFromStringBuilder(sb);
            HttpResponseHeader responseHeader = new HttpResponseHeader("/index.html", HttpStatus.OK, responseBody.length());

            return new HttpResponse(responseHeader, responseBody);
        } catch (SQLException sqle) {
            log.error("GET / failed. sql error_code = {}", sqle.getErrorCode());
        } catch (IOException ioe) {
            log.error("GET / failed. {}", ioe.getMessage());
        }

        log.debug("{}, redirect to index", request.line().path());
        return redirect("index.html");
    }
}
