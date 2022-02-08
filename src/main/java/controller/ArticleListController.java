package controller;

import dao.ArticleDao;
import dto.ArticleDto;
import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;
import mapper.ArticleMapper;
import util.MapUtil;

public class ArticleListController implements Controller {

    private static ArticleListController instance;

    public static ArticleListController getInstance() {
        if (instance == null) {
            instance = new ArticleListController();
        }
        return instance;
    }

    @Override
    public HttpResponse run(HttpRequest request, DataOutputStream dos) {

        ArticleDao dao = ArticleDao.getInstance();
        List<ArticleDto> articleDtos = ArticleMapper.instance.articlesToDtos(dao.find());

        return HttpResponse.ok(
                request.getUrl(),
                Map.of("articles", articleDtos),
                MapUtil.getEmptyCookieMap(),
                dos);
    }
}
