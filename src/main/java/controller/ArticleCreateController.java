package controller;

import dao.ArticleDao;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import model.Article;
import org.bson.types.ObjectId;
import util.MapUtil;

public class ArticleCreateController implements Controller {

    private static ArticleCreateController instance;

    public static ArticleCreateController getInstance() {
        if (instance == null) {
            instance = new ArticleCreateController();
        }
        return instance;
    }

    ArticleDao articleDao = ArticleDao.getInstance();

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {
        String logined = request.getCookie("logined");

        if (!Boolean.parseBoolean(logined)) {
            return HttpResponse.found(
                    "/",
                    MapUtil.getEmptyCookieMap(),
                    dos
            );
        }

        String content = request.getBodyData().get("content");
        String userId = request.getCookie("userId");

        articleDao.save(new Article(new ObjectId(), userId, content));

        return HttpResponse.found(
                "/",
                MapUtil.getEmptyCookieMap(),
                dos);
    }
}
