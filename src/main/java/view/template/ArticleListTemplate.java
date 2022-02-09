package view.template;

import dto.ArticleDto;
import java.util.List;
import java.util.Map;

public class ArticleListTemplate implements ViewTemplate {

    private static final ArticleListTemplate INSTANCE = new ArticleListTemplate();

    public static ArticleListTemplate getInstance() {
        return INSTANCE;
    }

    @Override
    public String getTemplate(Map<String, Object> model) {

        List<ArticleDto> articles = List.of();
        if (model.get("articles") instanceof List && !((List<?>) model.get("articles")).isEmpty()) {
            if (((List<?>) model.get("articles")).get(0) instanceof ArticleDto) {
                articles = (List<ArticleDto>) model.get("articles");
            }
        }

        StringBuilder result = new StringBuilder();
        for (ArticleDto article : articles) {
            result.append(
                    String.format(
                            articleBlock,
                            article.getContent(),
                            article.getCreateTime(),
                            article.getAuthor()));
        }

        return String.format(block, result);
    }

    public static final String block =
            "<div class=\"container\" id=\"main\">\n"
                    + "   <div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">\n"
                    + "      <div class=\"panel panel-default qna-list\">\n"
                    + "          <ul class=\"list\">\n"
                    + "%s" // articleBlock
                    + "          </ul>\n"
                    + "          <div class=\"row\">"
                    + "              <div class=\"col-md-3 qna-write\">\n"
                    + "                  <a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">질문하기</a>\n"
                    + "              </div>\n"
                    + "          </div>\n"
                    + "        </div>\n"
                    + "    </div>\n"
                    + "</div>";

    public static final String articleBlock =
            "<div class=\"wrap\">\n"
                    + "                      <div class=\"main\">\n"
                    + "                          <strong class=\"subject\">\n"
                    + "                              <a href=\"./qna/show.html\">%s</a>\n"
                    // content
                    + "                          </strong>\n"
                    + "                          <div class=\"auth-info\">\n"
                    + "                              <i class=\"icon-add-comment\"></i>\n"
                    + "                              <span class=\"time\">%s</span>\n" // createTime
                    + "                              <a href=\"./user/profile.html\" class=\"author\">%s</a>\n"
                    //author
                    + "                          </div>\n"
                    + "                      </div>\n"
                    + "                  </div>\n";
}
